package com.casic.simulation.dangerSource.web;

import com.casic.simulation.core.ext.store.MultipartFileResource;
import com.casic.simulation.core.ext.store.StoreConnector;
import com.casic.simulation.core.ext.store.StoreDTO;
import com.casic.simulation.core.json.JSONTool;
import com.casic.simulation.core.mapper.JsonMapper;
import com.casic.simulation.core.util.StringUtils;
import com.casic.simulation.dangerSource.domain.DangerSource;
import com.casic.simulation.dangerSource.dto.DangerSourceDTO;
import com.casic.simulation.dangerSource.manager.DangerSourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("dangerSource")
public class DangerSourceController
{
    private static final String MSG ="<li class='in'>"+
            "<img class='avatar' alt='' src='../../s/media/image/avatar1.jpg'/>"+
            "<div class='message'>"+
            "<span class='arrow'></span>"+
            "<span class='id hidden'>dbId</span>"+
            "<a href='#' class='name'>dangerSource</a>"+
            "<span class='time'>reportDay</span>"+
            "<span class='body'>alarmMsg</span>"+
            "<a href='#' class='btn mini green'>不再提示</a>"+
            "</div>"+
            "</li>";

    private DangerSourceManager dangerSourceManager;
    private StoreConnector storeConnector;
    private static Logger logger = LoggerFactory.getLogger(DangerSourceController.class);

    @Resource
    public void setStoreConnector(StoreConnector storeConnector) {
        this.storeConnector = storeConnector;
    }

    @Resource
    public void setDangerSourceManager(DangerSourceManager dangerSourceManager) {
        this.dangerSourceManager = dangerSourceManager;
    }


    @RequestMapping("dangerSource-list")
    public @ResponseBody String listDangerSourceInfo(HttpServletRequest request){
        try
        {
            int rows = Integer.parseInt(request.getParameter("rows"));
            int page = Integer.parseInt(request.getParameter("page"));
            DangerSourceDTO dangerSourceDTO = new DangerSourceDTO();
            dangerSourceDTO.setDescription(request.getParameter("desc"));
            dangerSourceDTO.setSourceName(request.getParameter("name"));
            dangerSourceDTO.setSourceGrade(request.getParameter("level"));
            dangerSourceDTO.setErrorMode(request.getParameter("model"));
            Map map = dangerSourceManager.queryDangerousPoints(page,rows,dangerSourceDTO);
            return new JsonMapper().toJson(map);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("dangerSource-save")
    public void saveDangerSource(@ModelAttribute DangerSource dangerSource,
                                             @RequestParam(value = "uploadFile",required = false) MultipartFile file, HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        try
        {
            if(null == dangerSource.getDbId())
            {
                map.put("save","add");
                if (dangerSourceManager.isExist(dangerSource.getSourceName()))
                {
                    String errorMessage = "名称已存在！";
                    map.put("success",false);
                    map.put("errorMessage",errorMessage);
                    JSONTool.writeDataResult(response, map);
                    return;
                }
            }
            else
            {
                map.put("save","edit");
            }
            dangerSource.setActive(true);
            if (file!=null)
            {
                if(StringUtils.isNotBlank(file.getOriginalFilename())){
                    StoreDTO storeDto = storeConnector.save("DangerrousSource",new MultipartFileResource(file),
                            file.getOriginalFilename());
                    dangerSource.setFileName(file.getOriginalFilename());
                    dangerSource.setFilePath(storeDto.getKey());
                }
            }
            dangerSourceManager.save(dangerSource);
            DangerSourceDTO dangerSourceDTO = new DangerSourceDTO(dangerSource);
            map.put("model",dangerSourceDTO);
            map.put("success",true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            map.put("success",false);
        }
        JSONTool.writeDataResult(response, map);
    }

    @RequestMapping("file-download")
    public String downloadFile(@RequestParam(value = "dbId",required = true)Long id,
                               HttpServletResponse response) {
        Map<String,Object> map = new HashMap<String, Object>();
        try{
            dangerSourceManager.downloadStrategyFile(id, response);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "dangerSource/dangerSource";
    }

    @RequestMapping("dangerSource-delete")
    @ResponseBody
    public Map<String,Object> deleteDangerSource(@RequestParam(value = "dbId",required = true) Long dbId)
    {
        Map<String,Object> map = new HashMap<String, Object>();
        try
        {
            map.put("success",true);
            DangerSource dangerSource = dangerSourceManager.get(dbId);
            if(dangerSource==null) return map;
            dangerSource.setActive(false);
            dangerSourceManager.save(dangerSource);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            map.put("success",false);
        }
        return  map;
    }

    @RequestMapping("addDangerSource-show")
    public String setPosition(@RequestParam(value = "longitude",required = false) String longitude,
                              @RequestParam(value = "latitude",required = false) String latitude,
                              Model model)
    {
        model.addAttribute("longitude", longitude);
        model.addAttribute("latitude", latitude);
        return "dangerSource/addDangerSource";
    }
    @RequestMapping("description-show")
    public String getDescription(@RequestParam(value = "dbId",required = false) Long id,
                                 Model model)
    {
        DangerSource dangerSource = dangerSourceManager.get(id);
        if (dangerSource!=null)
        {
            DangerSourceDTO dangerSourceDTO = new DangerSourceDTO(dangerSource);
            model.addAttribute("model", dangerSourceDTO);
        }
        return "dangerSource/dangerSourceInfo";
    }
    @RequestMapping("edit")
    public String editDangerAreaInfo(@RequestParam(value = "dbId",required = false) Long dbId,
                                     Model model) {
        DangerSourceDTO dangerSourceDTO = new DangerSourceDTO();
        if(dbId!=null)
        {
            dangerSourceDTO = dangerSourceManager.findDangerSourceDTOById(dbId);
        }

        model.addAttribute("model", dangerSourceDTO);
        return "dangerSource/addDangerSource";
    }

}