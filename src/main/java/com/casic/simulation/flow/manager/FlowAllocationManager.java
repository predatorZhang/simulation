package com.casic.simulation.flow.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.page.Page;
import com.casic.simulation.core.util.ExecInfo;
import com.casic.simulation.core.util.ExecResult;
import com.casic.simulation.flow.bean.EventTypeEnum;
import com.casic.simulation.flow.domain.Flow;
import com.casic.simulation.flow.domain.FlowAllocation;
import com.casic.simulation.flow.dto.FlowAllocationDto;
import com.casic.simulation.flow.web.EventTypeController;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2017/4/24.
 */
@Service
public class FlowAllocationManager
        extends HibernateEntityDao<FlowAllocation> {

    @Resource
    private FlowManager flowManager;

    /**
     * 根据事件种类获取flow，该方法可依赖性强
     * @param eventType
     * @return
     */
    public ExecResult<Flow> getFlowByEventType(Integer eventType) {
        if (eventType == null) {
            return ExecResult.fail("event type is null.");
        }
        try {
            Criteria criteria = createCriteria(FlowAllocation.class);
            criteria.add(Restrictions.eq("eventType", eventType));
            FlowAllocation result = (FlowAllocation) criteria.setMaxResults(1).uniqueResult();
            if (result == null) {
                Flow flow = flowManager.getDefaultFlow();
                if (flow == null) {
                    return ExecResult.fail("there is no flow!");
                } else {
                    return ExecResult.succ(flow);
                }
            } else {
                return ExecResult.succ(result.getFlow());
            }
        } catch (Exception e) {
            return ExecResult.fail(e.getMessage());
        }
    }

    /**
     * 事件派发列表查询
     * @param eventType
     * @param page
     * @param rows
     * @return
     */
    public ExecResult<Page> queryForList(
            Integer eventType, int page, int rows
    ) {
        try {
            Criteria criteria = createCriteria(FlowAllocation.class);
            if (eventType != null && !eventType.equals(EventTypeController.EVENT_TYPE_QUERY_FOR_ALL)) {
                criteria.add(Restrictions.eq("eventType", eventType));
            }
            criteria.addOrder(Order.desc("id"));
            Page pg = pagedQuery(criteria, page, rows);
            pg.setResult(FlowAllocationDto.convert(
                    (List<FlowAllocation>) pg.getResult()
            ));
            return ExecResult.succ(pg);
        } catch (Exception e) {
            return ExecResult.fail(e.getMessage());
        }
    }

    /**
     * 根据id删除派发事件
     * @param id
     * @return
     */
    public ExecInfo delete(Long id) {
        try {
            Criteria criteria = createCriteria(FlowAllocation.class);
            if (id == null) {
                return ExecInfo.fail("ID不能为空");
            }
            criteria.add(Restrictions.eq("id", id));
            FlowAllocation f = (FlowAllocation)criteria.uniqueResult();
            remove(f);
            return ExecInfo.succ("删除成功！");
        } catch (Exception e) {
            return ExecInfo.fail(e.getMessage());
        }
    }

    /**
     * 增加派发数据
     * @param typeID
     * @param flowID
     * @return
     */
    public ExecInfo add(Integer typeID, Long flowID) {
        try {
            Flow flow = flowManager.get(flowID);
            if (flow == null || !flow.getActive() || typeID == null) {
                return ExecInfo.fail("流程或种类已失效");
            }
            Criteria criteria = createCriteria(FlowAllocation.class);
            criteria.add(Restrictions.eq("eventType", typeID));
            Integer count = getCount(criteria);
            if (count > 0) {
                return ExecInfo.fail(EventTypeEnum.getByIndex(typeID).getTypeName() + " 类型派发事件已经设置");
            }
            FlowAllocation allocation = new FlowAllocation();
            allocation.setEventType(typeID);
            allocation.setCreateTime(new Date());
            allocation.setFlow(flow);
            save(allocation);
            return ExecInfo.succ("添加成功！");
        } catch (Exception e) {
            return ExecInfo.fail(e.getMessage());
        }
    }

    /**
     * 编辑派发数据
     * @param id
     * @param typeID
     * @param flowID
     * @return
     */
    public ExecInfo edit(Long id, Integer typeID, Long flowID) {
        try {
            FlowAllocation current = get(id);
            if (current == null) {
                return add(typeID, flowID);
            }
            Criteria criteria = createCriteria(FlowAllocation.class);
            criteria.add(Restrictions.eq("eventType", typeID));
            criteria.add(Restrictions.not(Restrictions.eq("id", id)));
            Integer count = getCount(criteria);
            if (count > 0) {
                return ExecInfo.fail(EventTypeEnum.getByIndex(typeID).getTypeName() + " 类型派发事件已经设置");
            }
            current.setUpdateTime(new Date());
            current.setFlow(flowManager.get(flowID));
            current.setEventType(typeID);
            save(current);
            return ExecInfo.succ("修改成功！");
        } catch (Exception e) {
            return ExecInfo.fail(e.getMessage());
        }
    }

    /**
     * 查询是否存在派发事件关联该flow
     * @param flow
     * @return
     */
    public boolean checkExistAllocationBelongTo(Flow flow) {
        Criteria criteria = createCriteria(FlowAllocation.class);
        criteria.add(Restrictions.eq("flow", flow));
        return getCount(criteria) > 0;
    }
}
