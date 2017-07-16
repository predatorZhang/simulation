package com.casic.simulation.flow.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.util.StringUtils;
import com.casic.simulation.flow.bean.OperationEnum;
import com.casic.simulation.flow.domain.Flow;
import com.casic.simulation.flow.domain.FlowNode;
import com.casic.simulation.flow.domain.Node;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/4/24.
 */
@Service
public class FlowNodeManager extends HibernateEntityDao<FlowNode> {

    /**
     * 根据node与flow获取当前能够提供的操作
     *
     * @param node
     * @param flow
     * @return
     */
    public List<OperationEnum> getOperation(Node node, Flow flow) {
        List<OperationEnum> result = new ArrayList<OperationEnum>();
        if (node == null || flow == null) {
            return result;
        }
        Criteria criteria = createCriteria(FlowNode.class);
        criteria.add(Restrictions.eq("node", node));
        criteria.add(Restrictions.eq("flow", flow));
        List<FlowNode> list = criteria.list();
        if (list.size() == 0) {
            list = getNextFlowNode(node, flow);
        }
        for (FlowNode temp : list) {
            result.add(OperationEnum.getByCode(temp.getOperationCode()));
        }
        return result;
    }

    /**
     * 应对在message status设置不在流程范围内时的应对方法
     * @param node
     * @param flow
     * @return
     */
    private List<FlowNode> getNextFlowNode(Node node, Flow flow) {
        Criteria criteria = createCriteria(FlowNode.class);
        criteria.add(Restrictions.eq("flow", flow));
        criteria.createAlias("node", "n");
        criteria.add(Restrictions.gt("n.messageStatus",
                node.getMessageStatus()));
        criteria.addOrder(Order.asc("n.messageStatus"));
        List<FlowNode> list = criteria.list();
        if (list.size() > 1) {
            List<FlowNode> result = new ArrayList<FlowNode>();
            int temp = Integer.MAX_VALUE;
            for (FlowNode flowNode : list) {
                if (flowNode.getNode().getMessageStatus() <= temp) {
                    result.add(flowNode);
                    temp = flowNode.getNode().getMessageStatus();
                }
            }
            return result;
        }
        return list;
    }

    /**
     * 根据当前节点及采取的操作，返回下一步的事件状态status
     * @param node
     * @param flow
     * @param operationCode
     * @return
     */
    public Integer getNextStatus(Node node, Flow flow, String operationCode) {
        if (node == null || flow == null ||
                StringUtils.isBlank(operationCode)) {
            return null;
        }
        Criteria criteria = createCriteria(FlowNode.class);
        criteria.add(Restrictions.eq("node", node));
        criteria.add(Restrictions.eq("flow", flow));
        criteria.add(Restrictions.eq("operationCode", operationCode));
        FlowNode flowNode = (FlowNode)criteria.setMaxResults(1).uniqueResult();
        if (flowNode == null) {
            flowNode = getNextFlowNode(flow, operationCode);
        }
        return flowNode == null ? null :
                flowNode.getNext().getNode().getMessageStatus();
    }

    /**
     * 当flow确定下，node与operation产生冲突，则以操作为准
     * 产生的原因很有可能，事件在未完成前对该事件类型的派发进行了修改
     * @param flow
     * @param operationCode
     * @return
     */
    private FlowNode getNextFlowNode(Flow flow, String operationCode) {
        Criteria criteria = createCriteria(FlowNode.class);
        criteria.add(Restrictions.eq("flow", flow));
        criteria.add(Restrictions.eq("operationCode", operationCode));
        return (FlowNode)criteria.setMaxResults(1).uniqueResult();
    }

    /**
     * 根据flow按顺序获取操作列表
     * @param flow
     * @return
     */
    public List<OperationEnum> getOperation(Flow flow) {
        List<OperationEnum> result = new ArrayList<OperationEnum>();
        if (flow == null) {
            return result;
        }
        Criteria criteria = createCriteria(FlowNode.class);
        criteria.add(Restrictions.eq("flow", flow));
        criteria.addOrder(Order.asc("id"));
        List<FlowNode> list = criteria.list();
        for (FlowNode temp : list) {
            result.add(OperationEnum.getByCode(temp.getOperationCode()));
        }
        return result;
    }

    /**
     * 根据flow获取头节点
     * @param flow
     * @return
     */
    public Integer getHeadStatus(Flow flow) {
        if (flow == null) {
            return null;
        }
        Criteria criteria = createCriteria(FlowNode.class);
        criteria.add(Restrictions.eq("flow", flow));
        criteria.add(Restrictions.eq("head", true));
        FlowNode flowNode = (FlowNode)criteria.setMaxResults(1).uniqueResult();
        return flowNode == null ? null :
                flowNode.getNode().getMessageStatus();
    }
}
