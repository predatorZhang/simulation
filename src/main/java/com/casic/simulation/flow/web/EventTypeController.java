package com.casic.simulation.flow.web;

import com.casic.simulation.flow.bean.EventTypeEnum;
import com.casic.simulation.flow.dto.EventTypeDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.POST;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("event")
public class EventTypeController {

    public static final Integer EVENT_TYPE_QUERY_FOR_ALL =
            Integer.MIN_VALUE;

    @POST
    @RequestMapping("queryAllEventType")
    @ResponseBody
    public List<EventTypeDto> queryAllEventType() {
        List<EventTypeDto> dtos = new ArrayList<EventTypeDto>();
        dtos.add(new EventTypeDto(
                EVENT_TYPE_QUERY_FOR_ALL, "全部", true
        ));
        for (EventTypeEnum typeEnum : EventTypeEnum.values()) {
            if (typeEnum != EventTypeEnum.UNKNOWN) {
                dtos.add(new EventTypeDto(
                        typeEnum.getIndex(), typeEnum.getTypeName()
                ));
            }
        }
        return dtos;
    }

    @POST
    @RequestMapping("queryEventType")
    @ResponseBody
    public List<EventTypeDto> queryEventType() {
        List<EventTypeDto> dtos = new ArrayList<EventTypeDto>();
        for (EventTypeEnum typeEnum : EventTypeEnum.values()) {
            if (typeEnum != EventTypeEnum.UNKNOWN) {
                dtos.add(new EventTypeDto(
                        typeEnum.getIndex(), typeEnum.getTypeName()
                ));
            }
        }
        return dtos;
    }

}