package com.casic.simulation.flow.manager;

import com.casic.simulation.core.hibernate.HibernateEntityDao;
import com.casic.simulation.core.page.Page;
import com.casic.simulation.core.util.ExecInfo;
import com.casic.simulation.core.util.ExecResult;
import com.casic.simulation.flow.bean.EventTypeEnum;
import com.casic.simulation.flow.domain.FlowDepartment;
import com.casic.simulation.flow.domain.FlowPerson;
import com.casic.simulation.flow.domain.Node;
import com.casic.simulation.flow.domain.NodeLimit;
import com.casic.simulation.flow.dto.NodeLimitDto;
import com.casic.simulation.flow.web.EventTypeController;
import com.casic.simulation.flow.web.NodeLimitController;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/4/24.
 */
@Service
public class NodeLimitManager extends HibernateEntityDao<NodeLimit> {

    @Resource
    private FlowDepartmentManager flowDepartmentManager;

    @Resource
    private FlowPersonManager flowPersonManager;

    @Resource
    private NodeManager nodeManager;

    /**
     * 根据node节点、事件种类、登录人员，获取是否有处理权限
     *
     * @param node
     * @param eventType
     * @param sysPersonID
     * @return
     */
    public boolean getAuthority(Node node,
                                Integer eventType,
                                Long sysPersonID) {
        if (node == null || eventType == null || sysPersonID == null) {
            return false;
        }
        Criteria criteria = createCriteria(NodeLimit.class);
        criteria.add(Restrictions.eq("node", node));
        criteria.add(Restrictions.eq("eventType", eventType));
        criteria.add(Restrictions.eq("sysPersonID", sysPersonID));
        return getCount(criteria) == 1;
    }

    /**
     * 删除处置人员需要删除关联的流程规则设置
     * @param flowPersonID
     */
    public void delByFlowPerson(Long flowPersonID) {
        Criteria criteria = createCriteria(NodeLimit.class);
        criteria.add(Restrictions.eq("flowPersonID", flowPersonID));
        List<NodeLimit> nodes = criteria.list();
        for (NodeLimit node : nodes) {
            remove(node);
        }
    }


    /**
     * 页面查询流程规则
     * @param queryNode
     * @param eventType
     * @param depID
     * @param page
     * @param rows
     * @return
     */
    public ExecResult<Page> queryForList(
            Long queryNode, Integer eventType, Long depID,
            int page, int rows
    ) {
        try {
            Criteria criteria = createCriteria(NodeLimit.class);
            if (queryNode != null && !queryNode.equals(NodeLimitController.FLOW_NODE_QUERY_FOR_ALL)) {
                criteria.createAlias("node", "n");
                criteria.add(Restrictions.eq("n.id", queryNode));
            } else {
                criteria.createAlias("node", "n");
                criteria.add(Restrictions.in("n.id", NodeLimitController.FLOW_NODE_QUERY_CONTAIN));
            }
            if (eventType != null && !eventType.equals(EventTypeController.EVENT_TYPE_QUERY_FOR_ALL)) {
                criteria.add(Restrictions.eq("eventType", eventType));
            }
            if (depID != null && !depID.equals(NodeLimitController.FLOW_DEP_QUERY_FOR_ALL)) {
                criteria.add(Restrictions.eq("flowDepID", depID));
            }
            criteria.addOrder(Order.desc("id"));
            Page pg = pagedQuery(criteria, page, rows);
            List<NodeLimitDto> dtos = NodeLimitDto.convert(
                    (List<NodeLimit>) pg.getResult()
            );
            for (NodeLimitDto dto : dtos) {
                dto.setEventTypeName(EventTypeEnum.getByIndex(dto.getEventType()).getTypeName());
                FlowDepartment department = flowDepartmentManager.getActiveByID(dto.getFlowDepID());
                dto.setFlowDepName(department.getDepName());
                FlowPerson person = flowPersonManager.getActiveByID(dto.getFlowPersonID());
                dto.setFlowPersonName(person.getUserName());
            }
            pg.setResult(dtos);
            return ExecResult.succ(pg);
        } catch (Exception e) {
            e.printStackTrace();
            return ExecResult.fail(e.getMessage());
        }
    }

    /**
     * 根据节点ID以及事件类型，获取对应的执行人列表
     * @param node
     * @param type
     * @return Map key 表示flow person id, value 表示node limit id
     */
    public Map<Long, Long> getInfoByNodeAndEventType(
            Long node, Integer type) {
        Map<Long, Long> result = new HashMap<Long, Long>();
        try {
            Criteria criteria = createCriteria(NodeLimit.class);
            criteria.createAlias("node", "n");
            criteria.add(Restrictions.eq("n.id", node));
            criteria.add(Restrictions.eq("eventType", type));
            criteria.addOrder(Order.desc("id"));
            List<NodeLimit> list = criteria.list();
            for (NodeLimit temp : list) {
                result.put(temp.getFlowPersonID(), temp.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 增加流程规则
     * @param node
     * @param type
     * @param flowperson
     * @return
     */
    public ExecInfo addBy(Long node, Integer type, Long flowperson) {
        try {
            NodeLimit nodeLimit = new NodeLimit();
            FlowPerson person = flowPersonManager.getActiveByID(flowperson);
            if (person == null) {
                return ExecInfo.fail("关联处置人员无法获取[" + flowperson +"]");
            }
            nodeLimit.setSysPersonID(person.getSysPersonID());
            nodeLimit.setFlowPersonID(flowperson);
            nodeLimit.setEventType(type);
            FlowDepartment department = person.getFlowDepartment();
            if (department == null || !department.getActive() ) {
                return ExecInfo.fail("关联处置部门失效");
            }
            nodeLimit.setSysDepID(department.getSysDepID());
            nodeLimit.setFlowDepID(department.getId());
            Node n = nodeManager.get(node);
            if (n == null) {
                return ExecInfo.fail("node节点无法获取[" + node +"]");
            }
            nodeLimit.setNode(n);
            save(nodeLimit);
            return ExecInfo.succ();
        } catch (Exception e) {
            e.printStackTrace();
            return ExecInfo.fail(e.getMessage());
        }
    }

    /**
     *
     * @param node
     * @param type
     * @param flowperson
     * @return
     */
    public ExecInfo delBy(Long node, Integer type, Long flowperson) {
        try {
            if (node == null || type == null || flowperson == null) {
                return ExecInfo.fail("node节点/type类型/处置人员ID不能为空");
            }
            Criteria criteria = createCriteria(NodeLimit.class);
            criteria.createAlias("node", "n");
            criteria.add(Restrictions.eq("n.id", node));
            criteria.add(Restrictions.eq("eventType", type));
            criteria.add(Restrictions.eq("flowPersonID", flowperson));
            List<NodeLimit> list = criteria.list();
            for (NodeLimit temp : list) {
                remove(temp);
            }
            return ExecInfo.succ();
        } catch (Exception e) {
            e.printStackTrace();
            return ExecInfo.fail(e.getMessage());
        }
    }
}
