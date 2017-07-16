package com.casic.simulation.event.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.page.Page;
import com.casic.simulation.core.util.StringUtils;
import com.casic.simulation.event.domain.AlarmEvent;
import com.casic.simulation.event.dto.AlarmEventDto;
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
public class AlarmEventManager extends HibernateEntityDao<AlarmEvent> {

    @Resource
    private FlowConnector flowConnector;

    public Map pageQueryAlarmEvent(int page, int rows, String messageStatus, String beginDate, String endDate,Long personId) {
        try {
            Map<String, Object> resultMap = new HashMap<String, Object>();

            StringBuilder hql = new StringBuilder();
            Map<String, Object> map = new HashMap<String, Object>();
            hql.append("from AlarmEvent where 1=1");

            if (StringUtils.isNotBlank(messageStatus) && !messageStatus.equals("全部")) {
                hql.append(" and messageStatus = :messageStatus ");
                map.put("messageStatus",
                        MessageStatusEnum.getByName(messageStatus).getIndex()
                );
            }else{//如果messageStatus为空，则查询确认状态后的，硬编码
                hql.append(" and messageStatus > :messageStatus ");
                map.put("messageStatus",
                        MessageStatusEnum.CONFIRM.getIndex()
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

            hql.append(" order by recordDate desc ");

            Page p = pagedQuery(hql.toString(), page, rows, map);
            List<AlarmEvent> alarmEventList = (List<AlarmEvent>) p.getResult();
            List<AlarmEventDto> alarmEventDTOList = AlarmEventDto.ConvertToDTOs(alarmEventList);
           if(personId!=null)
               for(AlarmEventDto alarmEventDto:alarmEventDTOList) {
                   List<OperationEnum> operations = flowConnector.getOperations(EventSourceEnum.ALARM_EVENT, alarmEventDto.getId(), personId);
                   if (operations.size() > 0) alarmEventDto.setOperate("<a href='#'>"+operations.get(0).getOperationName()+"</a>");
               }
            resultMap.put("rows", alarmEventDTOList);
            resultMap.put("total", p.getTotalCount());

            return resultMap;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
