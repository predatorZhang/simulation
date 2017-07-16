package com.casic.simulation.dangerArea.web;
import com.casic.simulation.core.ext.store.MultipartFileResource;
import com.casic.simulation.core.ext.store.StoreConnector;
import com.casic.simulation.core.ext.store.StoreDTO;
import com.casic.simulation.core.json.JSONTool;
import com.casic.simulation.core.mapper.JsonMapper;
import com.casic.simulation.core.util.StringUtils;
import com.casic.simulation.dangerArea.domain.DangerArea;
import com.casic.simulation.dangerArea.dto.DangerAreaDTO;
import com.casic.simulation.dangerArea.manager.DangerAreaManager;
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
@RequestMapping("dangerArea")
public class DangerAreaController
{
    private DangerAreaManager dangerAreaManager;
    private StoreConnector storeConnector;

    @Resource
    public void setStoreConnector(StoreConnector storeConnector)
    {
        this.storeConnector = storeConnector;
    }

    @Resource
    public void setDangerAreaManager(DangerAreaManager dangerAreaManager) {
        this.dangerAreaManager = dangerAreaManager;
    }

    //查询设备类型信息，按照设备类型名称模糊查询
    @RequestMapping("dangerArea-list")
    public @ResponseBody String listDangerAreaInfo(HttpServletRequest request){
        try
        {
            int rows = Integer.parseInt(request.getParameter("rows"));
            int page = Integer.parseInt(request.getParameter("page"));
            DangerAreaDTO dangerAreaDTO = new DangerAreaDTO();
            dangerAreaDTO.setDescription(request.getParameter("desc"));
            dangerAreaDTO.setAreaName(request.getParameter("name"));
            dangerAreaDTO.setAreaGrade(request.getParameter("level"));
            Map map = dangerAreaManager.queryDangerousArea(page,rows,dangerAreaDTO);
            return new JsonMapper().toJson(map);
        }
        catch (Exception e)
        {
           return null;
        }
    }

    @RequestMapping("dangerArea-save")
    public void saveDangerArea(@ModelAttribute DangerArea dangerArea,
                                             @RequestParam(value = "uploadFile",required = false) MultipartFile file, HttpServletResponse response)throws IOException
    {
        Map<String,Object> map = new HashMap<String, Object>();
        try
        {
            if(null == dangerArea.getDbId())
            {
                map.put("save","add");
                if (dangerAreaManager.isExist(dangerArea.getAreaName()))
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
            if (file!=null)
            {
                if(StringUtils.isNotBlank(file.getOriginalFilename())){
                    StoreDTO storeDto = storeConnector.save("DangerrousArea",
                            new MultipartFileResource(file),
                            file.getOriginalFilename());
                    dangerArea.setFileName(file.getOriginalFilename());
                    dangerArea.setFilePath(storeDto.getKey());
                }
            }
            dangerArea.setActive(true);
            dangerAreaManager.save(dangerArea);
            DangerAreaDTO dangerAreaDTO = new DangerAreaDTO(dangerArea);
            map.put("model",dangerAreaDTO);
            map.put("success", true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            map.put("success",false);
        }
        JSONTool.writeDataResult(response, map);
    }

    @RequestMapping("file-download")
    @ResponseBody
    public String downloadFile(@RequestParam(value = "dbId",required = true)Long id,
                               HttpServletResponse response) {
        try{
            dangerAreaManager.downloadStrategyFile(id, response);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "dangerArea/dangerArea";
    }

    @RequestMapping("dangerArea-delete")
    @ResponseBody
    public Map<String,Object> deleteDangerArea(@RequestParam(value = "dbId",required = true) Long dbId)
    {
        Map<String,Object> map = new HashMap<String, Object>();
        try
        {
            map.put("success",true);
            DangerArea dangerArea = dangerAreaManager.get(dbId);
            if(dangerArea ==null) return map;
            dangerArea.setActive(false);
            dangerAreaManager.save(dangerArea);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            map.put("success",false);
        }
        return  map;
    }

    @RequestMapping("addDangerArea-show")
    public String setPosition(@RequestParam(value = "position",required = false) String position,
                              Model model)
    {
        model.addAttribute("position", position);

        return "dangerArea/addDangerArea";
    }

    @RequestMapping("description-show")
    public String getDescription(@RequestParam(value = "dbId",required = false) Long id,
                                 Model model)
    {
        DangerArea dangerArea = dangerAreaManager.get(id);
        if (dangerArea!=null)
        {
            DangerAreaDTO dangerAreaDTO = new DangerAreaDTO(dangerArea);
            model.addAttribute("model", dangerAreaDTO);
        }
        return "dangerArea/dangerAreaInfo";
    }

    @RequestMapping("edit")
    public String editDangerAreaInfo(@RequestParam(value = "dbId",required = false) Long dbId,
                                     Model model) {
        DangerAreaDTO dangerAreaDTO = new DangerAreaDTO();
        if(dbId!=null)
        {
            dangerAreaDTO = dangerAreaManager.findDangerAreaDTOById(dbId);
        }

        model.addAttribute("model", dangerAreaDTO);
        return "dangerArea/addDangerArea";
    }

}