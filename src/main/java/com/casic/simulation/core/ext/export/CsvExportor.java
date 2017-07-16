package com.casic.simulation.core.ext.export;

import com.casic.simulation.core.util.ServletUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CsvExportor implements Exportor {
    public void export(HttpServletResponse response, TableModel tableModel)
            throws IOException {
        StringBuilder buff = new StringBuilder();

        for (int i = 0; i < tableModel.getHeaderCount(); i++) {
            buff.append(tableModel.getHeader(i));

            if (i != (tableModel.getHeaderCount() - 1)) {
                buff.append(",");
            }
        }

        buff.append("\n");

        for (int i = 0; i < tableModel.getDataCount(); i++) {
            for (int j = 0; j < tableModel.getHeaderCount(); j++) {
                buff.append(tableModel.getValue(i, j));

                if (j != (tableModel.getHeaderCount() - 1)) {
                    buff.append(",");
                }
            }

            buff.append("\n");
        }

        response.setContentType(ServletUtils.STREAM_TYPE);
        ServletUtils.setFileDownloadHeader(response, tableModel.getName()
                + ".csv");
        response.getOutputStream().write(buff.toString().getBytes("UTF-8"));
        response.getOutputStream().flush();
    }
}
