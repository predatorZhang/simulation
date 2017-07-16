package com.casic.simulation.dma.model.web;

import com.casic.simulation.dma.model.domain.DMAInfo;
import com.casic.simulation.dma.model.domain.PosDMA;
import com.casic.simulation.dma.model.domain.PositionInfo;
import com.casic.simulation.dma.model.json.DeviceJSON;
import com.casic.simulation.dma.model.json.PositionInfoJSON;
import com.casic.simulation.dma.model.json.WaterPipelineRegionDataJSON;
import com.casic.simulation.dma.model.json.WaterPipelineRegionTreeJSON;
import com.casic.simulation.dma.model.manager.DMAInfoManager;
import com.casic.simulation.dma.model.manager.DevPosManager;
import com.casic.simulation.dma.model.manager.PosDMAManager;
import com.casic.simulation.dma.model.manager.PositionInfoManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("WaterPipelineRegionScan")
public class WaterPipelineRegionScanController {
    @Resource
    private DMAInfoManager dmaInfoManager;

    @Resource
    private PosDMAManager posDMAManager;

    @Resource
    private PositionInfoManager positionInfoManager;

    @Resource
    private DevPosManager devPosManager;

    /**
     * 获取所有分区列表，及分区下的监测点{@link com.casic.simulation.dma.model.domain.PosDMA}列表
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("getWaterPipelineRegionTreeData")
    @ResponseBody
    @POST
    public Map<String, Object> getWaterPipelineRegionTreeData
            (HttpServletResponse response) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        List<WaterPipelineRegionTreeJSON> nodeList = new ArrayList<WaterPipelineRegionTreeJSON>();
        nodeList.add(WaterPipelineRegionTreeJSON.root);
        try {
            List<DMAInfo> dmaList = dmaInfoManager.findAllActiveDmaInfo();
            for (DMAInfo dma : dmaList) {
                WaterPipelineRegionTreeJSON node = DMAInfo.convertToJSON(dma);
                nodeList.add(node);
                List<PosDMA> posDmaList = dma.getPosInDmaList();
                Collections.sort(posDmaList, new PosDmaNameComparator());
                for (PosDMA posDMA : posDmaList) {
                    node = PosDMA.convertToJSON(posDMA, dma);
                    if (null != node) {
                        nodeList.add(node);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", e.getMessage());
        }

        map.put("total", nodeList.size());
        map.put("rows", nodeList);
        return map;
    }

    static class PosDmaNameComparator implements Comparator {
        public int compare(Object object1, Object object2) {// 实现接口中的方法
            PosDMA p1 = (PosDMA) object1;
            PosDMA p2 = (PosDMA) object2;
            String name1 = p1.getPositionInfo().getName();
            String name2 = p2.getPositionInfo().getName();
            return name1.compareTo(name2);
        }
    }

    /**
     * 根据分区NO获取子分区列表
     * @param regionid
     * @param pageNum
     * @param rows
     * @return
     * @throws IOException
     */
    @RequestMapping("querySubDMAByID")
    @ResponseBody
    @POST
    public Map<String, Object> querySubDMAByID(
            @RequestParam(value = "regionId", required = true) Long regionid,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "5") Integer rows
    ) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = dmaInfoManager.querySubDMAByID(regionid, pageNum, rows);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("total", 0);
            map.put("error", e.getMessage());
            map.put("rows", new ArrayList<WaterPipelineRegionDataJSON>());
        }
        return map;
    }

    /**
     * 根据子分区，获取该分区下分配的监测点 {@link com.casic.simulation.dma.model.domain.PosDMA}
     * @param regionid
     * @param pageNum
     * @param rows
     * @return
     * @throws IOException
     */
    @RequestMapping("queryPositionInfoByID")
    @ResponseBody
    @POST
    public Map<String, Object> queryPositionInfoByID(
            @RequestParam(value = "regionId", required = true) Long regionid,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "5") Integer rows
    ) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = posDMAManager.findPositionDTOByDma(regionid, pageNum, rows);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("total", 0);
            map.put("error", e.getMessage());
            map.put("rows", new ArrayList<PositionInfoJSON>());
        }
        return map;
    }

    /**
     * 针对某一分区，获取该分区未添加的监测点{@link com.casic.simulation.dma.model.domain.PositionInfo}列表
     * @param regionid
     * @return
     */
    @RequestMapping("queryPosition")
    @ResponseBody
    @POST
    public List<PositionInfoJSON> queryPosition(
            @RequestParam(value = "regionId", required = true) Long regionid
    ) {
        Set<Long> excludePosIdSet = new HashSet<Long>();

        if (regionid != WaterPipelineRegionTreeJSON.root.getId()) {
            DMAInfo dmaInfo = dmaInfoManager.getDMAByID(regionid);
            for (PosDMA posDma : dmaInfo.getPosInDmaList()) {
                PositionInfo positionInfo = posDma.getPositionInfo();
                if (positionInfo.getActive()) {
                    excludePosIdSet.add(positionInfo.getID());
                }
            }
        }

        return PositionInfoJSON.convertTo(
                positionInfoManager.findActivePositionInfoAndNotIn(excludePosIdSet)
        );
    }

    /**
     * 获取指定监测点下的设备列表
     * @param posID
     * @param pageNum
     * @param rows
     * @return
     * @throws IOException
     */
    @RequestMapping("queryEquipmentInfoByID")
    @ResponseBody
    @POST
    public Map<String, Object> queryEquipmentInfoByID(
            @RequestParam(value = "positionID", required = false, defaultValue = "-1") Long posID,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "5") Integer rows
    ) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = devPosManager.findDeviceDTOByPos(posID, pageNum, rows);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", e.getMessage());
            map.put("total", 0);
            map.put("rows", new ArrayList<DeviceJSON>());
        }
        return map;
    }

}
