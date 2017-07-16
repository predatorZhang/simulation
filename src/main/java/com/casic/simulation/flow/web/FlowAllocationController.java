package com.casic.simulation.flow.web;

import com.casic.simulation.core.page.Page;
import com.casic.simulation.core.util.ExecInfo;
import com.casic.simulation.core.util.ExecResult;
import com.casic.simulation.flow.bean.EventTypeEnum;
import com.casic.simulation.flow.domain.Flow;
import com.casic.simulation.flow.dto.FlowAllocationDto;
import com.casic.simulation.flow.manager.FlowAllocationManager;
import com.casic.simulation.flow.manager.FlowManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("allocation")
public class FlowAllocationController {

    @Resource
    private FlowAllocationManager flowAllocationManager;

    @Resource
    private FlowManager flowManager;

    @POST
    @RequestMapping("flow-allocation-show")
    @ResponseBody
    public Map<String, Object> showFlowAllocations(
            @RequestParam(value = "eventType", required = false) Integer eventType,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "5") Integer rows
    ) {
        Map<String, Object> map = new HashMap<String, Object>();
        ExecResult<Page> result =
                flowAllocationManager.queryForList(eventType, pageNum, rows);
        if (result.isSucc()) {
            map.put("total", result.getValue().getTotalCount());
            map.put("rows", result.getValue().getResult());
        } else {
            map.put("total", 0);
            map.put("msg", result.getMsg());
        }
        return map;
    }

    @POST
    @RequestMapping("del-allocation")
    @ResponseBody
    public Map<String, Object> deleteAllocation(
            @RequestParam(value = "id") Long id
    ) {
        Map<String, Object> map = new HashMap<String, Object>();
        ExecInfo result = flowAllocationManager.delete(id);
        map.put("success", result.isSucc());
        map.put("msg", result.getMsg());
        return map;
    }

    @POST
    @RequestMapping("save-allocation")
    @ResponseBody
    public Map<String, Object> saveAllocation(
            @RequestParam(value = "distributeID", required = false) Long distributeID,
            @RequestParam(value = "typeID") Integer typeID,
            @RequestParam(value = "flowID") Long flowID
    ) {
        Map<String, Object> map = new HashMap<String, Object>();
        ExecInfo result = checkValidity(typeID, flowID);
        if (result.isSucc()) {
            if (distributeID == null) {
                result = flowAllocationManager.
                        add(typeID, flowID);
            } else {
                result = flowAllocationManager.
                        edit(distributeID, typeID, flowID);
            }
        }
        map.put("success", result.isSucc());
        map.put("msg", result.getMsg());
        return map;
    }

    public static final String DEFAULT_FLOW = "DEFAULT_FLOW";
    public static final String PATROL_FLOW = "PATROL_FLOW";
    public static final String PUBLIC_FLOW = "PUBLIC_FLOW";

    /**
     * 后台对于事件分配的一些特殊处理
     * @param typeID
     * @param flowID
     * @return
     */
    private ExecInfo checkValidity(Integer typeID, Long flowID) {
        Flow flow = flowManager.get(flowID);
        EventTypeEnum type = EventTypeEnum.getByIndex(typeID);
        if (flow == null || type == EventTypeEnum.UNKNOWN) {
            return ExecInfo.fail("事件类型ID或者选择的流程ID提供错误");
        }
        if ((type == EventTypeEnum.JISHUI_EVENT
                || type == EventTypeEnum.RANQIT_EVENT
                || type == EventTypeEnum.WELL_EVENT
                || type == EventTypeEnum.YUWUSHUI_EVENT)
                && !DEFAULT_FLOW.equals(flow.getFlowCode())) {
            return ExecInfo.fail("设备上报事件只能采用默认流程");
        }
        if (type == EventTypeEnum.ALARM_EVENT
                && !DEFAULT_FLOW.equals(flow.getFlowCode())) {
            return ExecInfo.fail("暂时公众上报类事件仅支持采用默认流程");
        }
        return ExecInfo.succ();
    }

}