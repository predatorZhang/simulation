package com.casic.simulation.event.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.page.Page;
import com.casic.simulation.core.util.StringUtils;
import com.casic.simulation.event.domain.PadEvent;
import com.casic.simulation.event.dto.PadEventDto;
import com.casic.simulation.flow.bean.EventSourceEnum;
import com.casic.simulation.flow.bean.OperationEnum;
import com.casic.simulation.flow.web.FlowConnector;
import com.casic.simulation.util.MessageStatusEnum;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/4/25.
 */
@Service
public class PadEventManager extends HibernateEntityDao<PadEvent>{
    @Resource
    private FlowConnector flowConnector;

    public Map pageQueryPadEvent(int page, int rows, String messageStatus, String beginDate, String endDate,Long personId) {
        try {
            Map<String, Object> resultMap = new HashMap<String, Object>();

            StringBuilder hql = new StringBuilder();
            Map<String, Object> map = new HashMap<String, Object>();
            hql.append("from PadEvent where 1=1");

            if (StringUtils.isNotBlank(messageStatus) && !messageStatus.equals("全部")) {
                hql.append(" and status = :messageStatus ");
                map.put("messageStatus",
                        MessageStatusEnum.getByName(messageStatus).getIndex()
                );
            }else{//如果messageStatus为空，则查询确认状态后的，硬编码
                hql.append(" and status > :messageStatus ");
                map.put("messageStatus",
                        MessageStatusEnum.CONFIRM.getIndex()
                );
            }
            if (StringUtils.isNotBlank(beginDate)) {
                hql.append(" and to_char(eventTime,'yyyy-mm-dd')>=:beg ");
                map.put("beg", beginDate);
            }
            if (StringUtils.isNotBlank(endDate)) {
                hql.append(" and to_char(eventTime,'yyyy-mm-dd')<=:end ");
                map.put("end", endDate);
            }

            hql.append(" order by eventTime desc ");

            Page p = pagedQuery(hql.toString(), page, rows, map);
            List<PadEvent> padEventList = (List<PadEvent>) p.getResult();
            List<PadEventDto> padEventDTOList = PadEventDto.ConvertToDTOs(padEventList);
            for(PadEventDto padEventDto:padEventDTOList){
                padEventDto.setPatrolName(getPatrolerNameByTaskId(padEventDto.getTaskId()));
            }
            if(personId!=null)
                for(PadEventDto padEventDto:padEventDTOList){
                    List<OperationEnum> operations = flowConnector.getOperations(EventSourceEnum.FEEDBACK, padEventDto.getDbId(), personId);
                    if (operations.size() > 0) padEventDto.setOperate("<a href='#'>"+operations.get(0).getOperationName()+"</a>");
                }
            resultMap.put("rows", padEventDTOList);
            resultMap.put("total", p.getTotalCount());

            return resultMap;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getPatrolerNameByTaskId(Long taskId){
        if(taskId==null) return "";
        String sql = "select username from xj_patrol where id in (select patrol_id from xj_task where id ="+taskId+")";
        SQLQuery sqlQuery = this.getSession().createSQLQuery(sql);
        List<String> usernames = sqlQuery.list();
        if(usernames.size()<1) return "";
        return usernames.get(0);
    }
}
