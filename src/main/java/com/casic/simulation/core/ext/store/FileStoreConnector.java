package com.casic.simulation.core.ext.store;

import com.casic.simulation.core.util.ExecInfo;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileStoreConnector implements StoreConnector {
    private String baseDir;
    
    private String separator = File.separator;
    private String upFolder = ".." + File.separator;

    public StoreDTO save(String model, Resource resource, String originName)
            throws Exception {
        String prefix = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String suffix = this.getSuffix(originName);
        String path = prefix + separator + UUID.randomUUID() + suffix;
        File dir = new File(baseDir + separator + model + separator + prefix);
        dir.mkdirs();

        File targetFile = new File(baseDir + separator + model + separator + path);
        FileOutputStream fos = new FileOutputStream(targetFile);

        try {
            FileCopyUtils.copy(resource.getInputStream(), fos);
            fos.flush();
        } finally {
            fos.close();
        }

        StoreDTO storeDto = new StoreDTO();
        storeDto.setModel(model);
        storeDto.setKey(path);
        storeDto.setResource(new FileSystemResource(targetFile));

        return storeDto;
    }

    public StoreDTO get(String model, String key) throws Exception {
        if (key.indexOf(upFolder) != -1) {
            StoreDTO storeDto = new StoreDTO();
            storeDto.setModel(model);
            storeDto.setKey(key);
        }

        File file = new File(baseDir + separator + model + separator + key);
        StoreDTO storeDto = new StoreDTO();
        storeDto.setModel(model);
        storeDto.setKey(key);
        storeDto.setResource(new FileSystemResource(file));

        return storeDto;
    }

    public ExecInfo checkFile(String model, String key) {
        if (key.indexOf(upFolder) != -1) {
            return ExecInfo.fail("无效的文件路径[包含'" + upFolder +
                    "'敏感字段]");
        }
        File file = new File(baseDir + separator + model + separator + key);
        if (file.exists()) {
            return ExecInfo.succ();
        } else {
            return ExecInfo.fail("文件已不存在");
        }
    }

    public void remove(String model, String key) throws Exception {
        if (key.indexOf(upFolder) != -1) {
            return;
        }

        File file = new File(baseDir + separator + model + separator + key);
        file.delete();
    }

    public String getSuffix(String name) {
        int lastIndex = name.lastIndexOf(".");

        if (lastIndex != -1) {
            return name.substring(lastIndex);
        } else {
            return "";
        }
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }
}
