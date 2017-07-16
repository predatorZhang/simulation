package com.casic.simulation.core.util;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.casic.simulation.util.PropertiesUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wp on 2016/4/18.
 */
public class OfficeConverter {

    private static PropertiesUtil putil = new PropertiesUtil("application.properties");
    public static final String CONVERTER_TARGET_TYPE = "pdf";

    public static final String OPENOFFICE_HOME_ALIAS = "OpenOffice_HOME_Window";
    public static final String OPENOFFICE_URL_ALIAS = "OpenOffice_URL";
    public static final String OPENOFFICE_PORT_ALIAS = "OpenOffice_Port";

    public static final Set<String> CONVERTER_TYPES = new HashSet<String>(
            Arrays.asList(new String[] {"doc", "ppt", "xls"})
    );

    /**
     * 由于本方法仅支持转化为pdf格式文件，因此参数具有如下特点：
     * destPath + destFileName + ".pdf" 即为目标文件的绝对路径
     *
     * @param sourceFile 源文件路径
     * @param destPath 目标父文件夹路径
     * @param destFileName 目标文件文件名（不包含后缀，因为本方法只转化为pdf格式文件）
     * @return
     */
    public static ExecResult<String> office2PDF(String sourceFile, String destPath,
                                      String destFileName) {
        String fileSufix = getFileSuffix(sourceFile);
        String destFile = destPath + File.separator + destFileName
                + "." + CONVERTER_TARGET_TYPE;
        if (CONVERTER_TARGET_TYPE.equals(fileSufix)) {
            return fileCopy(new File(sourceFile), new File(destFile));
        }

        if (CONVERTER_TYPES.contains(fileSufix)) {
            return convert(new File(sourceFile), new File(destFile));
        }

        return ExecResult.fail("暂仅支持浏览pdf、doc、ppt、xls文件");
    }

    /**
     * 若目标文件的文件名保持不变则可以忽略{@link #office2PDF(String, String, String)}
     * 方法的destFileName参数
     *
     * @param sourceFile
     * @param destPath
     * @return 成功返回目标文件路径
     */
    public static ExecResult<String> office2PDF(String sourceFile, String destPath) {
        String destFileName = getFilePrefix(sourceFile);
        return office2PDF(sourceFile, destPath, destFileName);
    }

    public static ExecResult<String> fileCopy(File sourceFile, File destFile) {
        if (destFile.exists()) {
            return ExecResult.succ(destFile.getAbsolutePath());
        }
        try {
            FileUtils.copyFile(sourceFile, destFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ExecResult.fail("文件提取发生错误，请尝试直接下载文件");
        }
        return ExecResult.succ(destFile.getAbsolutePath());
    }

    private static ExecResult<String> convert(File sourceFile, File destFile) {
        OpenOfficeConnection connection = null;
        Process pro = null;
        try {
            if (!sourceFile.exists()) {
                return ExecResult.fail("文件已不存在");
            }

            if (destFile.exists()) {
                return ExecResult.succ(destFile.getAbsolutePath());
            }
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }

            String OpenOffice_HOME = putil.getProperty(OPENOFFICE_HOME_ALIAS);
            if (OpenOffice_HOME == null) {
                return ExecResult.fail("文件格式转换服务未设置，无法在线预览，请尝试直接下载文件");
            }
            if (OpenOffice_HOME.charAt(OpenOffice_HOME.length() - 1) != '\\') {
                OpenOffice_HOME += "\\";
            }

            String url = putil.getProperty(OPENOFFICE_URL_ALIAS);
            int port = Integer.valueOf(putil.getProperty(OPENOFFICE_PORT_ALIAS));
            String command =
                    OpenOffice_HOME
                            + "program\\soffice.exe -headless -accept=\"socket,host=" + url
                            + ",port=" + port + ";urp;\" -nofirststartwizard";
            String checkCommand =
                    OpenOffice_HOME + "program";
            if (!new File(checkCommand).exists()) {
                return ExecResult.fail("文件格式转换服务未设置正确地址，无法在线预览，请尝试直接下载文件");
            }
            pro = Runtime.getRuntime().exec(command);
            connection = new SocketOpenOfficeConnection(url, port);
            connection.connect();
            DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
            converter.convert(sourceFile, destFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ExecResult.fail("文件已不存在");
        } catch (OpenOfficeException e) {
            e.printStackTrace();
            return ExecResult.fail("文件格式转换报错，请尝试直接下载文件");
        } catch (ConnectException e) {
            e.printStackTrace();
            return ExecResult.fail("文件格式转换服务连接失败，请尝试直接下载文件");
        } catch (IOException e) {
            e.printStackTrace();
            return ExecResult.fail("文件读取失败，请尝试直接下载文件");
        } finally {
            if (null != connection){
                try {
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != pro) {
                try {
                    pro.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ExecResult.succ(destFile.getAbsolutePath());
    }

    public static String getFileSuffix(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(splitIndex + 1).toLowerCase();
    }

    public static String getFilePrefix(String fileName){
        int beginIndex = fileName.lastIndexOf(File.separator) + 1;
        int endIndex = fileName.lastIndexOf(".");
        return fileName.substring(beginIndex, endIndex);
    }
}
