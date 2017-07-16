package com.casic.simulation.patrol.web;

import com.casic.simulation.patrol.dto.PatrolerDto;
import com.casic.simulation.patrol.manager.PatrolerManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lenovo on 2017/4/28.
 */
@Controller
@RequestMapping("patrol")
public class PatrolerController {
    @Resource
    private PatrolerManager patrolerManager;

    @RequestMapping("fetch-all")
    @ResponseBody
    public List<PatrolerDto> getAllPatrolerInfo(){
        return patrolerManager.allPatrolers();
    }
}
