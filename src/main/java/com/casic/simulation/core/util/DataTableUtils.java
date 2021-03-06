package com.casic.simulation.core.util;

import com.casic.simulation.core.mapper.JsonMapper;

import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2015/4/20.
 */
public class DataTableUtils {

    public static Map<String,Object> covertJsonStringToHashMap(String jsonParam) {

        List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
        Map<String, Object> resultMap = new HashMap<String, Object>();

        try {
            JsonMapper mapper = new JsonMapper();
            lists = mapper.fromJson(jsonParam, List.class);
            for (Map<String, String> map : lists) {
                Set<String> set = map.keySet();
                for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
                    String key = it.next();
                    String value = map.get(key).toString();
                    if(key.equals("name")){
                        if(value.equals("iDisplayStart")){
                            Object start = map.get("value");
                            resultMap.put("iDisplayStart", start);
                            break;
                        }
                        else if (value.equals("iDisplayLength")) {
                            Object rows = map.get("value");
                            resultMap.put("iDisplayLength", rows);
                            break;
                        }
                        else if (value.equals("sSearch")) {
                            Object search = map.get("value");
                            resultMap.put("sSearch", search);
                            break;
                        }
                        else if (value.equals("sEcho")){
                            Object search = map.get("value");
                            resultMap.put("sEcho", search);
                            break;
                        }
                    }
                    it.next();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public static DataTableParameter getDataTableParameterByJsonParam(String jsonParam){
        Map<String,Object> map = covertJsonStringToHashMap(jsonParam);
        int iDisplayStart = Integer.parseInt(map.get("iDisplayStart").toString());
        int iDisplayLength = Integer.parseInt(map.get("iDisplayLength").toString());
        int sEcho = Integer.parseInt(map.get("sEcho").toString());
        String sSearch = null;
        if(map.get("sSearch")!=null)
        {
            sSearch = map.get("sSearch").toString();
        }
        return new DataTableParameter(iDisplayStart,iDisplayLength,sEcho,sSearch);
    }
}
