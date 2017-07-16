package com.casic.simulation.core.ext.export;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Exportor {
    void export(HttpServletResponse response, TableModel tableModel)
            throws IOException;
}
