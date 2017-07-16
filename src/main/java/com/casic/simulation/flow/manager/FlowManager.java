package com.casic.simulation.flow.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.util.ExecInfo;
import com.casic.simulation.flow.bean.OperationEnum;
import com.casic.simulation.flow.domain.Flow;
import com.casic.simulation.flow.dto.FlowDto;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lenovo on 2017/4/24.
 */
@Service
public class FlowManager extends HibernateEntityDao<Flow> {

    @Resource
    private FlowNodeManager flowNodeManager;

    @Resource
    private FlowAllocationManager flowAllocationManager;

    /**
     * 获取默认flow，获取方法：取ID最小的flow
     * @return
     */
    public Flow getDefaultFlow() {
        Criteria criteria = createCriteria(Flow.class);
        criteria.addOrder(Order.asc("id"));
        return (Flow)criteria.setMaxResults(1).uniqueResult();
    }

    /**
     * 激活流程
     * @param ids
     * @return
     */
    public ExecInfo fireFlows(Long[] ids) {
        try {
            Set<Long> idset = convert(ids);
            Criteria criteria = createCriteria(Flow.class);
            List<Flow> flows = criteria.list();
            for (Flow flow : flows) {
                if (idset.contains(flow.getId())) {
                    flow.setActive(true);
                } else {
                    if (flowAllocationManager.checkExistAllocationBelongTo(flow)) {
                        return ExecInfo.fail(flow.getFlowName() + "在派发事件中被引用");
                    }
                    flow.setActive(false);
                }
                save(flow);
            }
            return ExecInfo.succ("激活成功！");
        } catch (Exception e) {
            return ExecInfo.fail(e.getMessage());
        }
    }

    /**
     * 获取所有Flow列表，每个流程获取操作列表
     * 返回信息中包含操作列表信息
     * @return
     */
    public List<FlowDto> getFlows() {
        Criteria criteria = createCriteria(Flow.class);
        List<Flow> flows = criteria.list();
        List<FlowDto> dtos = new ArrayList<FlowDto>();
        for (Flow flow : flows) {
            if (flow == null) continue;
            FlowDto dto = FlowDto.convert(flow);
            dto.setOperations(getOperations(flowNodeManager.getOperation(flow)));
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * 将list转化为set
     * @param list
     * @return
     */
    private Set<Long> convert(Long[] list) {
        Set<Long> set = new HashSet<Long>();
        for (Long temp : list) {
            set.add(temp);
        }
        return set;
    }

    /**
     * 根据获取的操作列表转化为操作列表描述
     * @param operations
     * @return
     */
    private String getOperations(List<OperationEnum> operations) {
        StringBuilder sb = new StringBuilder();
        for (OperationEnum operation : operations) {
            if (operation == OperationEnum.UNKNOWN) continue;
            sb.append(operation.getOperationName()).append(",");
        }
        if (operations.size() > 0) {
            return sb.substring(0, sb.length() - 1);
        } else {
            return sb.toString();
        }
    }

    /**
     * 获取所有Flow列表，每个流程获取操作列表
     * 返回信息中只包含简单的flow信息
     * @return
     */
    public List<FlowDto> getAllActiveFlow() {
        Criteria criteria = createCriteria(Flow.class);
        criteria.add(Restrictions.eq("active", true));
        return FlowDto.convert(criteria.list());
    }
}
