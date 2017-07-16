package com.casic.simulation.overflow.manager;


import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.device.domain.Device;
import com.casic.simulation.device.domain.DeviceTypeEnum;
import com.casic.simulation.device.dto.DeviceDTO;
import com.casic.simulation.overflow.domain.AdDjLiquid;
import com.casic.simulation.overflow.domain.YSPipeLine;
import com.casic.simulation.overflow.dto.BlockPipeDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class AdDjLiquidManager extends HibernateEntityDao<AdDjLiquid> {


     public Map getLiquidInfo(String roadName,int page, int rows){
         List<Device> devices = this.getYSLiquidDevice();

         List<DeviceDTO> dtos = this.getYSLiquidRate(devices);
         //组装hql语句跟参数map
         String sql = " from YSPipeLine where code is not null ";
         Map<String, Object> paraMap3 = new HashMap<String, Object>();
         if (!StringUtils.isEmpty(roadName)) {
             sql = sql + " and roadName = :roadName";
             paraMap3.put("roadName", roadName);
         }
         List<YSPipeLine> results = (List<YSPipeLine>) createQuery(sql, paraMap3).list();

         List<BlockPipeDTO> blocks = makeBlockPipeDTOList(results, dtos);
         Map<String, Object> resultMap = new HashMap<String, Object>();
         resultMap.put("rows", cutList(blocks, page, rows));
         resultMap.put("total", blocks.size());

         return resultMap;
     }

    private List<Device> getYSLiquidDevice() {
        String hql = "from Device t where t.turnX = '雨水管线附属物' and t.deviceType.typeName='"+ DeviceTypeEnum.LIQUID.getName() +"'"+
                " and t.active = 1";
        Map<String, Object> map0 = new HashMap<String, Object>();
        List<Device> devices = createQuery(hql, map0).list();
        return devices;
    }
    private List<DeviceDTO> getYSLiquidRate(List<Device> devices) {

        List<DeviceDTO> dtos = new ArrayList<DeviceDTO>();
        for (Device dev : devices) {
            DeviceDTO dto = new DeviceDTO();
            dto.setAttachFeature(dev.getTurnY());
            dto.setDevCode(dev.getDevCode());

            String hql0 = "from AdDjLiquid t where t.devId = :devCode order by t.uptime desc";
            Map<String, Object> paraMap = new HashMap<String, Object>();
            paraMap.put("devCode", dev.getDevCode());
            AdDjLiquid ad = findUnique(hql0, paraMap);
            if (ad != null) {
                dto.setShowData(ad.getLiquidData());
            } else {
                dto.setShowData("0");
            }

            //更新井深数据
            String hql1 = "select t.井深 from 雨水管线附属物 t where t.编号 = '" + dev.getTurnY() + "'";
            Map<String, Object> paraMap2 = new HashMap<String, Object>();

            List<String> depthList = this.getJdbcTemplate().queryForList(hql1, String.class);
            String depth = "";
            if (depthList.size() == 0) {
                depth = "";
            } else {
                depth = depthList.get(0);
            }
            depth = depth == "" ? "0" : depth;
            float ratio = Float.parseFloat(dto.getShowData()) / (Float.parseFloat(depth) + (float) 0.0001);
            ratio = ratio > 1 ? 1 : ratio;
            ratio = ratio < 0 ? 0 : ratio;
            DecimalFormat df=new DecimalFormat("0.00");
            dto.setLiquidDepth(dto.getShowData());
            dto.setWellDepth(depth);
            dto.setShowData(df.format(Double.parseDouble(ratio+"")));
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * 组装BlockPipeDTO列表，且按照概率进行排序
     *
     * @param results
     * @param dtos
     * @return
     */
    private List<BlockPipeDTO> makeBlockPipeDTOList(List<YSPipeLine> results, List<DeviceDTO> dtos) {
        results = sort(results, new Comparator<YSPipeLine>() {
            @Override
            public int compare(YSPipeLine o1, YSPipeLine o2) {
                return selfCompare(o1.getRelateWell(), o2.getRelateWell());
            }
        });
        dtos = sort(dtos, new Comparator<DeviceDTO>() {
            @Override
            public int compare(DeviceDTO o1, DeviceDTO o2) {
                return selfCompare(o1.getAttachFeature(), o2.getAttachFeature());
            }
        });
        List<BlockPipeDTO> blocks = new ArrayList<BlockPipeDTO>();
        DecimalFormat df = new DecimalFormat("0.00");
        int index = 0;
        for (YSPipeLine result : results) {

            BlockPipeDTO blockPipeDTO = new BlockPipeDTO();

            blockPipeDTO.setCode(result.getCode());
            blockPipeDTO.setDiameter(result.getDiameter());
            blockPipeDTO.setStartDepth(df.format(Double.parseDouble(result.getStartDepth())));
            blockPipeDTO.setEndDepth(df.format(Double.parseDouble(result.getEndDepth())));
            blockPipeDTO.setOwner(result.getOwner());
            blockPipeDTO.setRoadName(null == result.getRoadName() ? "" : result.getRoadName());
            blockPipeDTO.setRelateWell(result.getRelateWell());
            blockPipeDTO.setTexture(result.getTexture());

            while(index < dtos.size() && selfCompare(result.getRelateWell(), dtos.get(index).getAttachFeature()) > 0){
                index ++;
            }
            String source;
            if (index < dtos.size() && (source = dtos.get(index).getAttachFeature()) != null && source.equals(result.getRelateWell())) {
                blockPipeDTO.setWellDepth(dtos.get(index).getWellDepth());
                blockPipeDTO.setLiquidDepth(dtos.get(index).getLiquidDepth());
                blockPipeDTO.setProba(dtos.get(index).getShowData());
            } else {
                blockPipeDTO.setWellDepth("--");
                blockPipeDTO.setLiquidDepth("--");
                blockPipeDTO.setProba("0.00");
            }
            blocks.add(blockPipeDTO);
        }
        return sort(blocks, new Comparator<BlockPipeDTO>() {
            @Override
            public int compare(BlockPipeDTO o1, BlockPipeDTO o2) {
                return o2.getProba().compareTo(o1.getProba());
            }
        });
    }

    private <E> List<E> sort(List<E> list, Comparator<E> comparator) {
        E[] array = (E[])list.toArray();
        Arrays.sort(array, comparator);
        return Arrays.asList(array);
    }

    /**
     * 两个String进行对比，o比t大返回值大于0，o比t小返回值小于0
     *
     * @param o
     * @param t
     * @return
     */
    private int selfCompare(String o, String t) {
        if (o == null && t != null) return -1;
        if (o == null && t == null) return 0;
        if (t == null) return 1;
        return o.compareTo(t);
    }

    /**
     * 根据页数跟每页数量进行结果截取
     *
     * @param list
     * @param page
     * @param rows
     * @param <E>
     * @return
     */
    private <E> List<E> cutList(List<E> list, int page, int rows) {
        List<E> result = new ArrayList<E>();
        int start = (page - 1) * rows;
        int end = start + rows;
        end = end > list.size() ? list.size() : end;
        for (int i = start; i < end; i ++) {
            result.add(list.get(i));
        }
        return result;
    }

}

