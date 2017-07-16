package com.casic.simulation.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by lenovo on 2017/4/12.
 */
public class RsHttpRequestUtil {
    public static String appendParam(String url, String key, Object value) {
        if (value == null) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (sb.indexOf("?") != -1) {
            sb.append("&");
        } else {
            sb.append("?");
        }
        try {
            sb.append(URLEncoder.encode(key, "UTF-8")).append("=")
                    .append(URLEncoder.encode(value.toString(), "UTF-8"));
        } catch (Exception e) {
            return url;
        }
        return sb.toString();
    }

    public static String sendRequest(String url) {
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            HttpURLConnection conn =
                    (HttpURLConnection)new URL(url).openConnection();
            is = conn.getInputStream();
            baos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = is.read(b, 0, 1024)) != -1) {
                baos.write(b, 0, len);
            }
            baos.flush();
            return new String(baos.toByteArray(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {}
            }
        }
        return new String();
    }
}
