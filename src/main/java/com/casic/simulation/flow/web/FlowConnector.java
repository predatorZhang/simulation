package com.casic.simulation.flow.web;

import com.casic.simulation.core.util.ExecResult;
import com.casic.simulation.event.manager.AlarmEventManager;
import com.casic.simulation.event.manager.AlarmRecordManager;
import com.casic.simulation.event.manager.PadEventManager;
import com.casic.simulation.flow.bean.EventSourceEnum;
import com.casic.simulation.flow.bean.EventTypeEnum;
import com.casic.simulation.flow.bean.OperationEnum;
import com.casic.simulation.flow.domain.Flow;
import com.casic.simulation.flow.domain.Node;
import com.casic.simulation.flow.manager.FlowAllocationManager;
import com.casic.simulation.flow.manager.FlowNodeManager;
import com.casic.simulation.flow.manager.NodeLimitManager;
import com.casic.simulation.flow.manager.NodeManager;
import com.casic.simulation.util.MessageStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/4/27.
 */
public class FlowConnector {

    private static Logger logger = LoggerFactory.getLogger(FlowConnector.class);

    @Resource
    private AlarmEventManager alarmEventManager;

    @Resource
    private AlarmRecordManager alarmRecordManager;

    @Resource
    private PadEventManager padEventManager;

    @Resource
    private NodeManager nodeManager;

    @Resource
    private FlowAllocationManager flowAllocationManager;

    @Resource
    private FlowNodeManager flowNodeManager;

    @Resource
    private NodeLimitManager nodeLimitManager;

    /**
     * 获取事件处理操作列表
     * @param source
     * @param eventID
     * @return
     */
    public List<OperationEnum> getOperations(EventSourceEnum source,
                                             Long eventID,
                                             Long personID) {
        List<OperationEnum> result = new ArrayList<OperationEnum>();

        ExecResult<Node> nodeExecResult = getNodeBySource(source, eventID);
        if (!nodeExecResult.isSucc()) {
            return result;
        }
        EventTypeEnum type = getEventTypeBySource(source, eventID);
        boolean authority = nodeLimitManager.getAuthority(
                nodeExecResult.getValue(),
                type.getIndex(),
                personID
        );
        if (!authority) {
            return result;
        }

        ExecResult<Flow> flowExecResult = flowAllocationManager
                .getFlowByEventType(type.getIndex());
        if (!flowExecResult.isSucc()) {
            return result;
        }
        return flowNodeManager.getOperation(
                nodeExecResult.getValue(),
                flowExecResult.getValue()
        );
    }

    /**
     * 获取事件处理操作列表 for pad
     * @param source
     * @param eventID
     * @return
     */
    public List<OperationEnum> getOperations(EventSourceEnum source,
                                             Long eventID) {
        List<OperationEnum> result = new ArrayList<OperationEnum>();
        ExecResult<Node> nodeExecResult = getNodeBySource(source, eventID);
        if (!nodeExecResult.isSucc()) {
            return result;
        }
        EventTypeEnum type = getEventTypeBySource(source, eventID);
        ExecResult<Flow> flowExecResult = flowAllocationManager.getFlowByEventType(type.getIndex());
        if (!flowExecResult.isSucc()) {
            return result;
        }
        return flowNodeManager.getOperation(
                nodeExecResult.getValue(),
                flowExecResult.getValue()
        );
    }

    /**
     * 为pad端获取头节点message status
     * @return
     */
    public Integer getStartStatusForPad() {
        ExecResult<Flow> result =
                flowAllocationManager.getFlowByEventType(
                        EventTypeEnum.getBySourceAndInfo(
                                EventSourceEnum.FEEDBACK, null).getIndex());
        if (!result.isSucc()) {
            logger.error("The source[{}] can not find the match flow.", EventSourceEnum.FEEDBACK.getSrcTableName());
            return MessageStatusEnum.DISTRIBUTE.getIndex();
        }
        Integer o = flowNodeManager.getHeadStatus(result.getValue());
        if (o == null) {
            o = MessageStatusEnum.DISTRIBUTE.getIndex();
        }
        return o;
    }

    /**
     * 根据事件以及当前操作，获取事件的下一步事件状态
     * @param source
     * @param eventID
     * @param operationCode
     * @return
     */
    public Integer getNextStatus(EventSourceEnum source,
                                 Long eventID,
                                 String operationCode) {
        ExecResult<Node> nodeExecResult = getNodeBySource(source, eventID);
        EventTypeEnum type = getEventTypeBySource(source, eventID);
        ExecResult<Flow> flowExecResult = flowAllocationManager
                .getFlowByEventType(type.getIndex());
        if (!nodeExecResult.isSucc() || !flowExecResult.isSucc()) {
            logger.error("The source[{}] and the eventID[{}] can not find the match node and flow.", source.getSrcTableName(), eventID);
            return null;
        }
        return flowNodeManager.getNextStatus(
                nodeExecResult.getValue(),
                flowExecResult.getValue(),
                operationCode
        );
    }

    private ExecResult<Node> getNodeBySource(EventSourceEnum source,
                                 Long eventID) {
        if (source == EventSourceEnum.ALARM_EVENT) {
            return nodeManager.getNodeByStatus(
                    alarmEventManager.get(eventID).getMessageStatus()
            );
        }
        if (source == EventSourceEnum.ALARM_RECORD) {
            return nodeManager.getNodeByStatus(
                    alarmRecordManager.get(eventID).getMessageStatus()
            );
        }
        if (source == EventSourceEnum.FEEDBACK) {
            return nodeManager.getNodeByStatus(
                    padEventManager.get(eventID).getStatus()
            );
        }
        return ExecResult.fail("source is unknown.");
    }

    /**
     * 根据source获取EventType
     * @param source
     * @return
     */
    public EventTypeEnum getEventTypeBySource(
            EventSourceEnum source, Long eventID
    ) {
        if (source == EventSourceEnum.ALARM_RECORD) {
            return EventTypeEnum.getBySourceAndInfo(
                    source, alarmRecordManager.get(eventID)
                            .getDevice().getDeviceType().getTypeName()
            );
        }
        return EventTypeEnum.getBySourceAndInfo(
                source, null
        );
    }
}
