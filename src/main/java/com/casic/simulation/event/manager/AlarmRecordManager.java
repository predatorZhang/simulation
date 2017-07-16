package com.casic.simulation.event.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.page.Page;
import com.casic.simulation.core.util.StringUtils;
import com.casic.simulation.event.domain.AlarmRecord;
import com.casic.simulation.event.dto.AlarmRecordDTO;
import com.casic.simulation.flow.bean.EventSourceEnum;
import com.casic.simulation.flow.bean.OperationEnum;
import com.casic.simulation.flow.web.FlowConnector;
import com.casic.simulation.util.MessageStatusEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/4/25.
 */
@Service
public class AlarmRecordManager extends HibernateEntityDao<AlarmRecord> {

    @Resource
    private FlowConnector flowConnector;

    //只要messageStatus大于确认的值，就能确保是设备报警状态了（已经确认过，肯定是在设备报警处理的）
    public Map pageQueryAlarmRecord(int page, int rows, String messageStatus, String beginDate, String endDate,String deviceTypeName,String road,String typeName, String devcode,Long personId) {
        try {
            Map<String, Object> resultMap = new HashMap<String, Object>();

            StringBuilder hql = new StringBuilder();
            Map<String, Object> map = new HashMap<String, Object>();
            hql.append("from AlarmRecord where 1=1");

            if (StringUtils.isNotBlank(messageStatus)) {
                hql.append(" and messageStatus = :messageStatus ");
                map.put("messageStatus",
                        MessageStatusEnum.getByName(messageStatus).getIndex()
                );
            }
            if (StringUtils.isNotBlank(beginDate)) {
                hql.append(" and to_char(recordDate,'yyyy-mm-dd')>=:beg ");
                map.put("beg", beginDate);
            }
            if (StringUtils.isNotBlank(endDate)) {
                hql.append(" and to_char(recordDate,'yyyy-mm-dd')<=:end ");
                map.put("end", endDate);
            }
            if (StringUtils.isNotBlank(typeName)) {
                String[] devTypeNameList = typeName.split(",");
                hql.append(" and (");
                for(String devTypeName : devTypeNameList) {
                    hql.append(" device.deviceType.typeName='");
                    hql.append(devTypeName);
                    hql.append("' or ");

                }
                hql = hql.replace(hql.length()-3, hql.length()-1, ")");
            }
            if(StringUtils.isNotBlank(deviceTypeName)){
                hql.append(" and deviceTypeName = :deviceTypeName");
                map.put("deviceTypeName",deviceTypeName);
            }
            if (StringUtils.isNotBlank(road)) {
                hql.append(" and device.factory like :road");
                map.put("road", "%" + road + "%");
            }
            if (StringUtils.isNotBlank(devcode)) {
                hql.append(" and device.devCode like :devcode");
                map.put("devcode", "%" + devcode + "%");
            }
            hql.append(" order by recordDate desc ");

            Page p = pagedQuery(hql.toString(), page, rows, map);
            List<AlarmRecord> alarmRecordList = (List<AlarmRecord>) p.getResult();
            List<AlarmRecordDTO> alarmRecordDTOList = AlarmRecordDTO.ConvertToDTOs(alarmRecordList);
            if(personId!=null)
                for(AlarmRecordDTO alarmRecordDTO:alarmRecordDTOList){
                    List<OperationEnum> operations = flowConnector.getOperations(EventSourceEnum.ALARM_RECORD, alarmRecordDTO.getId(), personId);
//                     operations.add(OperationEnum.RECORD);
                    if (operations.size() > 0) alarmRecordDTO.setOperate("<a href='#'>"+operations.get(0).getOperationName()+"</a>");
            }
            resultMap.put("rows", alarmRecordDTOList);
            resultMap.put("total", p.getTotalCount());

            return resultMap;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
