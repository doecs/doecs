package com.doecs.core.utils;

import org.apache.commons.lang3.RandomStringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ImageUtils {
    /**
     * 本地图片转换成base64字符串
     * @param relativeFilePath
     * @param rootPath
     * @return
     */
    public static String imageToBase64(String relativeFilePath, String rootPath) {
        String sepa = File.separator;
        if (StringUtils.isBlank(relativeFilePath)) {
            return null;
        }

        rootPath = StringUtils.isBlank(rootPath) ? "" : (rootPath + sepa);
        return imageToBase64(rootPath + relativeFilePath);
    }


    /**
     * 本地图片转换成base64字符串
     * @param filePath
     * @return
     */
    public static String imageToBase64(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        InputStream in = null;
        byte[] data = null;

        // 读取图片字节数组
        try {
            in = new FileInputStream(filePath);

            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return genMediaUriScheme(filePath) + encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    /**
     * base64字符串转换成图片
     * @param imgBase64
     * @param rootPath 文件管理根目录
     * @param extName 指定文件扩展名
     * @param moduleName 模块名
     * @param date 指定日期（关系导目录名和文件名
     * @return 相对路径+文件名
     */
    public static String base64ToImageFile(String imgBase64, String rootPath, String extName, String moduleName, Date date) {
        // 对字节数组字符串进行Base64解码并生成图片
        if (StringUtils.isBlank(imgBase64, rootPath, extName)) // 图像数据为空
            return null;

        // 去除前缀如：“data:image/jpeg;base64,”
        int idx = imgBase64.indexOf(";base64,");
        if (idx > 0) {
            imgBase64 = imgBase64.substring(idx + 8);
        }

        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgBase64);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }

            if (date == null) {
                date = new Date();  // 未指定日期则使用当前日期
            }
            String fileName = genRandomFileName(moduleName, date, extName); // 未指定名称则使用随机名称
            String path = createYyyyMmDir(rootPath, date);
            String relativePath = path.replace(rootPath, "");

            out = new FileOutputStream(path + "/" + fileName);
            out.write(b);
            out.flush();
            out.close();

            return relativePath + "/" + fileName;
        } catch (Exception e) {
            return null;
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 本地图片转换成base64字符串
     * @param relativeFilePath
     * @param rootPath
     * @return
     */
    @Deprecated
    public static String ImageToBase64(String relativeFilePath, String rootPath) {
        if (StringUtils.isBlank(relativeFilePath)) {
            return null;
        }
        InputStream in = null;
        byte[] data = null;

        // 读取图片字节数组
        try {
            rootPath = StringUtils.isBlank(rootPath) ? "" : (rootPath + "/");
            in = new FileInputStream(rootPath + relativeFilePath);

            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return genMediaUriScheme(relativeFilePath) + encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    /**
     * base64字符串转换成图片
     * @param imgBase64
     * @param rootPath 文件管理根目录
     * @param extName 指定文件扩展名
     * @param moduleName 模块名
     * @param date 指定日期（关系导目录名和文件名
     * @return 相对路径+文件名
     */
    @Deprecated
    public static String Base64ToImageFile(String imgBase64, String rootPath, String extName, String moduleName, Date date) {
        // 对字节数组字符串进行Base64解码并生成图片
        if (StringUtils.isBlank(imgBase64, rootPath, extName)) // 图像数据为空
            return null;

        // 去除前缀如：“data:image/jpeg;base64,”
        int idx = imgBase64.indexOf(";base64,");
        if (idx > 0) {
            imgBase64 = imgBase64.substring(idx + 8);
        }

        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgBase64);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }

            if (date == null) {
                date = new Date();  // 未指定日期则使用当前日期
            }
            String fileName = genRandomFileName(moduleName, date, extName); // 未指定名称则使用随机名称
            String path = createYyyyMmDir(rootPath, date);
            String relativePath = path.replace(rootPath, "");

            out = new FileOutputStream(path + "/" + fileName);
            out.write(b);
            out.flush();
            out.close();

            return relativePath + "/" + fileName;
        } catch (Exception e) {
            return null;
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 根据模块名+yyyyMMddHHmmssSSS+五位随机数创建文件名
    public static String genRandomFileName(String moduleName, Date date, String extName) {
        UUID uuid = UUID.randomUUID();
        String randStr = RandomStringUtils.random(5, true, true);
        moduleName = StringUtils.isBlank(moduleName) ? "" : (moduleName + "-");

        return moduleName + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date)
                + "-" + randStr+ "." + extName;
    }

    // 按yyyyMM创建目录
    public static String createYyyyMmDir(String rootPath, Date date) {
        String path = rootPath + "/" + new SimpleDateFormat("yyyyMM").format(date);
        //如果不存在,创建文件夹
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        return path;
    }
    /**
     * 获取文件扩展名
     * @return
     */
    public static String getExt(String filename) {
        int index = filename.lastIndexOf(".");

        if (index == -1) {
            return null;
        }
        String result = filename.substring(index + 1);
        return result;
    }

    /**
     * 获取文件扩展名
     * @return
     */
    public static String genMediaUriScheme(String filename) {
        String ext = getExt(filename);
        if (StringUtils.isBlank(ext)){
            return "";
        }
        if (ext.equals("gif")){
            return String.format("data:image/gif;base64,");
        }else if (ext.equals("jpg") || ext.equals("jpeg")){
            return String.format("data:image/jpeg;base64,");
        }else if (ext.equals("png")){
            return String.format("data:image/png;base64,");
        }else if (ext.equals("ico")){
            return String.format("data:image/x-icon;base64,");
        }else{
            return "data:image/jpeg;base64,";
        }
    }
}