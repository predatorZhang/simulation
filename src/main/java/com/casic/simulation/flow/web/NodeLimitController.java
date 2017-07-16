package com.casic.simulation.flow.web;

import com.casic.simulation.core.page.Page;
import com.casic.simulation.core.util.ExecInfo;
import com.casic.simulation.core.util.ExecResult;
import com.casic.simulation.core.util.StringUtils;
import com.casic.simulation.flow.bean.TreeNodeEnum;
import com.casic.simulation.flow.domain.FlowDepartment;
import com.casic.simulation.flow.domain.FlowPerson;
import com.casic.simulation.flow.domain.Node;
import com.casic.simulation.flow.dto.FlowDepartmentDto;
import com.casic.simulation.flow.dto.FlowTreeNodeDto;
import com.casic.simulation.flow.dto.NodeDto;
import com.casic.simulation.flow.dto.NodeLimitDto;
import com.casic.simulation.flow.manager.FlowDepartmentManager;
import com.casic.simulation.flow.manager.FlowPersonManager;
import com.casic.simulation.flow.manager.NodeLimitManager;
import com.casic.simulation.flow.manager.NodeManager;
import com.casic.simulation.util.MessageStatusEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import java.util.*;

@Controller
@RequestMapping("node-limit")
public class NodeLimitController {

    /** 处置部门查询条件“全部”代表的数值 **/
    public static final Long FLOW_DEP_QUERY_FOR_ALL = -100l;

    /** 处置节点查询条件(处置规则查询页面)“全部”代表的数值 **/
    public static final Long FLOW_NODE_QUERY_FOR_ALL = -200l;

    /** 处置节点查询条件(处置规则查询页面)“全部”代表的集合 **/
    public static final Set<Long> FLOW_NODE_QUERY_CONTAIN =
            new HashSet<Long>();

    @Resource
    private NodeLimitManager nodeLimitManager;

    @Resource
    private FlowDepartmentManager flowDepartmentManager;

    @Resource
    private FlowPersonManager flowPersonManager;

    @Resource
    private NodeManager nodeManager;

    @POST
    @RequestMapping("node-limie-list")
    @ResponseBody
    public Map<String, Object> showNodeLimits(
            @RequestParam(value = "queryNode", required = false) Long queryNode,
            @RequestParam(value = "eventType", required = false) Integer eventType,
            @RequestParam(value = "depID", required = false) Long depID,
            @RequestParam(value = "page") Integer pageNum,
            @RequestParam(value = "rows") Integer rows
    ) {
        Map<String, Object> map = new HashMap<String, Object>();
        ExecResult<Page> result =
                nodeLimitManager.queryForList(queryNode, eventType,
                        depID, pageNum, rows);
        if (result.isSucc()) {
            map.put("total", result.getValue().getTotalCount());
            map.put("rows", result.getValue().getResult());
        } else {
            map.put("total", 0);
            map.put("rows", Arrays.asList());
            map.put("msg", result.getMsg());
        }
        return map;
    }

    @POST
    @RequestMapping("queryAllDep")
    @ResponseBody
    public List<FlowDepartmentDto> queryAllDepartment() {
        List<FlowDepartmentDto> result =
                new ArrayList<FlowDepartmentDto>();
        result.add(getDefaultDep());
        List<FlowDepartmentDto> dtos =
                flowDepartmentManager.queryExceptRootForActive();
        result.addAll(dtos);
        return result;
    }

    private FlowDepartmentDto getDefaultDep() {
        FlowDepartmentDto temp = new FlowDepartmentDto();
        temp.setActive(false);
        temp.setDepName("全部");
        temp.setId(FLOW_DEP_QUERY_FOR_ALL);
        temp.setSelected(true);
        return temp;
    }

    @POST
    @RequestMapping("queryNode")
    @ResponseBody
    public List<NodeDto> queryNode() {
        List<NodeDto> nodes = new ArrayList<NodeDto>();
        nodes.add(getDefaultNode());
        makeNodeDto(nodes, MessageStatusEnum.DISTRIBUTE.getIndex());
        makeNodeDto(nodes, MessageStatusEnum.RECORD.getIndex());
        makeNodeDto(nodes, MessageStatusEnum.FINISH.getIndex());
        return nodes;
    }

    /**
     * 获取默认节点
     * @return
     */
    private NodeDto getDefaultNode() {
        NodeDto temp = new NodeDto();
        temp.setNodeName("全部");
        temp.setId(FLOW_NODE_QUERY_FOR_ALL);
        temp.setSelected(true);
        return temp;
    }

    /**
     * 拼装dto至list中
     * @param list
     * @param status
     */
    private void makeNodeDto(List<NodeDto> list, Integer status) {
        ExecResult<Node> result = nodeManager.getNodeByStatus(status);
        if (result.isSucc()) {
            Node temp = result.getValue();
            if (!FLOW_NODE_QUERY_CONTAIN.contains(temp.getId())) {
                FLOW_NODE_QUERY_CONTAIN.add(temp.getId());
            }
            list.add(NodeDto.convert(temp));
        }
    }

    @POST
    @RequestMapping("queryNodeExceptAll")
    @ResponseBody
    public List<NodeDto> queryNodeExceptAll() {
        List<NodeDto> nodes = new ArrayList<NodeDto>();
        makeNodeDto(nodes, MessageStatusEnum.DISTRIBUTE.getIndex());
        makeNodeDto(nodes, MessageStatusEnum.RECORD.getIndex());
        makeNodeDto(nodes, MessageStatusEnum.FINISH.getIndex());
        return nodes;
    }

    @POST
    @RequestMapping("get-tree-with-info")
    @ResponseBody
    public List<FlowTreeNodeDto> getTreeWithInfo(
            @RequestParam(value = "queryNode") Long queryNode,
            @RequestParam(value = "eventType") Integer eventType
    ) {
        List<FlowTreeNodeDto> result = new ArrayList<FlowTreeNodeDto>();
        Map<Long, Long> limitsInfo = nodeLimitManager.getInfoByNodeAndEventType(queryNode, eventType);
        FlowTreeNodeDto defaultNode = getRootTreeNode();
        makeTree(defaultNode, limitsInfo);
        result.add(defaultNode);
        return result;
    }

    private FlowTreeNodeDto getRootTreeNode() {
        FlowTreeNodeDto dto = new FlowTreeNodeDto();
        FlowDepartment department =
                flowDepartmentManager.getActiveByID(
                        FlowDepartmentManager.ROOT_DEP_ID);
        if (department != null) {
            dto.setId(department.getId());
            dto.setText(department.getDepName());
        }
        dto.setState("open");
        dto.setType(TreeNodeEnum.ROOT.getType());
        dto.setIconCls(TreeNodeEnum.ROOT.getIconCls());
        return dto;
    }

    private void makeTree(FlowTreeNodeDto parent, Map<Long, Long> infos) {
        List<FlowTreeNodeDto> children = new ArrayList<FlowTreeNodeDto>();
        List<FlowDepartment> departments = flowDepartmentManager
                .queryForFlowDepByParentID(parent.getId());
        for (FlowDepartment department : departments) {
            FlowTreeNodeDto child = convert(department);
            makeTree(child, infos);
            children.add(child);
        }
        List<FlowPerson> persons = flowPersonManager
                .queryPersonsByDepID(parent.getId());
        for (FlowPerson person : persons) {
            FlowTreeNodeDto child = convert(person, infos);
            children.add(child);
        }
        parent.setChildren(children);
    }

    private FlowTreeNodeDto convert(FlowDepartment department) {
        FlowTreeNodeDto dto = new FlowTreeNodeDto();
        dto.setText(department.getDepName());
        dto.setId(department.getId());
        dto.setState("open");
        dto.setType(TreeNodeEnum.DEPARTMENT.getType());
        dto.setIconCls(TreeNodeEnum.DEPARTMENT.getIconCls());
        return dto;
    }

    private FlowTreeNodeDto convert(FlowPerson person, Map<Long, Long> info) {
        FlowTreeNodeDto dto = new FlowTreeNodeDto();
        dto.setText(person.getUserName());
        dto.setId(person.getId());
        dto.setType(TreeNodeEnum.PERSON.getType());
        dto.setIconCls(TreeNodeEnum.PERSON.getIconCls());
        if (info.containsKey(person.getId())) {
            dto.setChecked(true);
            dto.setNodeLimitID(info.get(person.getId()));
        }
        return dto;
    }

    @POST
    @RequestMapping("modify-node-limit")
    @ResponseBody
    public Map<String, Object> modifyNodeLimit(
            @RequestParam(value = "persons[]") Long[] persons,
            @RequestParam(value = "queryNode") Long queryNode,
            @RequestParam(value = "eventType") Integer eventType
    ) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", false);
        Map<Long, Long> limitsInfo = nodeLimitManager.getInfoByNodeAndEventType(queryNode, eventType);
        for (Long person : persons) {
            if (limitsInfo.containsKey(person)) {
                limitsInfo.remove(person);
            } else {
                ExecInfo info = nodeLimitManager.
                        addBy(queryNode, eventType, person);
                if (!info.isSucc()) {
                    result.put("msg", "修改失败，增加处置人员[" +
                            getPersonNameById(person) +
                            "]发生错误[" + info.getMsg() + "]");
                    return result;
                }
            }
        }
        Iterator<Long> iter = limitsInfo.keySet().iterator();
        while (iter.hasNext()) {
            Long personID= iter.next();
            ExecInfo info = nodeLimitManager.delBy(
                    queryNode, eventType, personID
            );
            if (!info.isSucc()) {
                result.put("msg", "修改失败，取消处置人员[" +
                        getPersonNameById(personID) +
                        "]发生错误[" + info.getMsg() + "]");
                return result;
            }
        }
        result.put("success", true);
        result.put("msg", "修改成功！");
        return result;
    }

    private String getPersonNameById(Long id) {
        if (id == null) {
            return "null";
        }
        FlowPerson person = flowPersonManager.get(id);
        if (person == null) {
            return "NULL";
        } else if (!person.getActive()) {
            return "已失效";
        } else if (StringUtils.isBlank(person.getUserName())) {
            return "";
        } else {
            return person.getUserName();
        }
    }
}