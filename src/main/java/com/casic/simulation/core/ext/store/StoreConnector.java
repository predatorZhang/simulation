package com.casic.simulation.core.ext.store;

import com.casic.simulation.core.util.ExecInfo;
import org.springframework.core.io.Resource;

public interface StoreConnector {
    StoreDTO save(String model, Resource resource, String originName)
            throws Exception;

    StoreDTO get(String model, String key) throws Exception;

    ExecInfo checkFile(String model, String key);

    void remove(String model, String key) throws Exception;
}
