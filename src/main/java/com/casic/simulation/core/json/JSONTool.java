package com.casic.simulation.core.json;

import com.casic.simulation.core.mapper.JsonMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class JSONTool {
	public static void writeDataResult(HttpServletResponse response,
                                       Object object) throws IOException {
		JsonMapper jsonMapper = new JsonMapper();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();
		System.out.println(jsonMapper.toJson(object));
		writer.write(jsonMapper.toJson(object));
		writer.flush();
		writer.close();
	}

    public static void writeJsonResult(HttpServletResponse response,
                                       String data) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(data);
        writer.flush();
        writer.close();
    }

	public static void writeMsgResult(HttpServletResponse response,
                                      boolean result, String msg)
			throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", result);
		map.put("msg", msg);
		writeDataResult(response, map);
	}
	
}
