package com.casic.simulation.flow.web;

import com.casic.simulation.core.util.ExecInfo;
import com.casic.simulation.flow.bean.TreeNodeEnum;
import com.casic.simulation.flow.domain.FlowDepartment;
import com.casic.simulation.flow.domain.FlowPerson;
import com.casic.simulation.flow.dto.DepartmentDTO;
import com.casic.simulation.flow.dto.FlowTreeNodeDto;
import com.casic.simulation.flow.dto.UserInfoDTO;
import com.casic.simulation.flow.manager.FlowDepartmentManager;
import com.casic.simulation.flow.manager.FlowPersonManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("flow-dep-person")
public class FlowDepPersonController {

    public static final Long SYS_DEP_FOR_NULL = -1l;

    @Resource
    private FlowDepartmentManager flowDepartmentManager;

    @Resource
    private FlowPersonManager flowPersonManager;

    @POST
    @RequestMapping("get-tree")
    @ResponseBody
    public List<FlowTreeNodeDto> getTree() {
        List<FlowTreeNodeDto> result = new ArrayList<FlowTreeNodeDto>();
        FlowTreeNodeDto defaultNode = getRootTreeNode();
        makeTree(defaultNode);
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

    private void makeTree(FlowTreeNodeDto parent) {
        List<FlowTreeNodeDto> children = new ArrayList<FlowTreeNodeDto>();
        List<FlowDepartment> departments = flowDepartmentManager
                .queryForFlowDepByParentID(parent.getId());
        for (FlowDepartment department : departments) {
            FlowTreeNodeDto child = convert(department);
            makeTree(child);
            children.add(child);
        }
        List<FlowPerson> persons = flowPersonManager
                .queryPersonsByDepID(parent.getId());
        for (FlowPerson person : persons) {
            FlowTreeNodeDto child = convert(person);
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

    private FlowTreeNodeDto convert(FlowPerson person) {
        FlowTreeNodeDto dto = new FlowTreeNodeDto();
        dto.setText(person.getUserName());
        dto.setId(person.getId());
        dto.setType(TreeNodeEnum.PERSON.getType());
        dto.setIconCls(TreeNodeEnum.PERSON.getIconCls());
        return dto;
    }

    @POST
    @RequestMapping("get-sys-persons")
    @ResponseBody
    public List<UserInfoDTO> getSysPersons() {
        return flowPersonManager.queryForSysPerson();
    }

    @POST
    @RequestMapping("get-sys-deps")
    @ResponseBody
    public List<DepartmentDTO> getSysDeps() {
        List<DepartmentDTO> departmentDTOs =
                flowDepartmentManager.queryForSysDep();
        DepartmentDTO head = new DepartmentDTO();
        head.setId(SYS_DEP_FOR_NULL);
        head.setName("无引用");
        head.setSelected(true);
        departmentDTOs.add(0, head);
        return departmentDTOs;
    }

    @POST
    @RequestMapping("add-person")
    @ResponseBody
    public Map<String, Object> addPerson(
            @RequestParam(value = "belongToDepNameID") Long depID,
            @RequestParam(value = "sysPersonID") Long personID
    ) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (flowPersonManager.exist(depID, personID)) {
            map.put("success", false);
            map.put("msg", "该部门下已经存在该人员");
        } else {
            FlowDepartment department =
                    flowDepartmentManager.getActiveByID(depID);
            ExecInfo result =
                    flowPersonManager.add(personID, department);
            map.put("success", result.isSucc());
            map.put("msg", result.getMsg());
        }
        return map;
    }

    @POST
    @RequestMapping("add-dep")
    @ResponseBody
    public Map<String, Object> addDep(
            @RequestParam(value = "depName") String depName,
            @RequestParam(value = "parentDepID") Long parentDepID,
            @RequestParam(value = "sysDepID") Long sysDepID
    ) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (sysDepID != SYS_DEP_FOR_NULL &&
                flowDepartmentManager.exist(sysDepID)) {
            map.put("success", false);
            map.put("msg", "该部门已经被关联");
        } else {
            FlowDepartment parent =
                    flowDepartmentManager.getActiveByID(parentDepID);
            ExecInfo result =
                    flowDepartmentManager.add(sysDepID, parent, depName);
            map.put("success", result.isSucc());
            map.put("msg", result.getMsg());
        }
        return map;
    }

    @POST
    @RequestMapping("del-person")
    @ResponseBody
    public Map<String, Object> delPerson(
            @RequestParam(value = "id") Long id
    ) {
        Map<String, Object> map = new HashMap<String, Object>();
        ExecInfo result = flowPersonManager.del(id);
        map.put("success", result.isSucc());
        map.put("msg", result.getMsg());
        return map;
    }

    @POST
    @RequestMapping("del-dep")
    @ResponseBody
    public Map<String, Object> delDep(
            @RequestParam(value = "id") Long id
    ) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            flowPersonManager.delDep(id);
            map.put("success", true);
            map.put("msg", "删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("msg", e.getMessage());
        }
        return map;
    }
}