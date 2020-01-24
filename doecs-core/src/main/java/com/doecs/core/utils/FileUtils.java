package com.doecs.core.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class FileUtils extends org.apache.commons.io.FileUtils {
    public static String getExportFileName(String fileName, String userAgent){
        String finalFileName = null;
        try {
            if(StringUtils.contains(userAgent, "MSIE")){//IE浏览器
                    finalFileName = URLEncoder.encode(fileName,"UTF-8");
            }else if(StringUtils.contains(userAgent, "Edge")){//Edge
                finalFileName = URLEncoder.encode(fileName,"UTF-8");
            }else if(StringUtils.contains(userAgent, "Mozilla")){//google,火狐浏览器
                finalFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }else{
                finalFileName = URLEncoder.encode(fileName,"UTF-8");//其他浏览器
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            finalFileName = fileName;
        }
        return finalFileName;
    }

    /**
     * 取得classpath
     * @return
     */
    public static String getAbsolutePathOfClassRoot(){
        String s = FileUtils.class.getClassLoader().getResource("").getPath();
        if(s.indexOf("/") == 0){
            s = s.substring(1);
        }
        return  s;
    }

    public static String getSuffixFromFileName(String fileName){
        if(StringUtils.isBlank(fileName) || fileName.indexOf(".") < 0){
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static String getRemoveSuffixFileName(String fileName){
        if(StringUtils.isBlank(fileName) || fileName.indexOf(".") < 0){
            return fileName;
        }
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * no use
     * @param relatePath
     * @return
     * @throws MalformedURLException
     */
    public static URL getRelatePathOfClassRoot(String relatePath) throws MalformedURLException {
        if(relatePath.contains("../")){
            return getResourceOfClassRoot(relatePath);
        }
        String classPathAbsolutePath = getAbsolutePathOfClassRoot();
        if(relatePath.substring(0,1).equals("/")){
            relatePath = relatePath.substring(1);   // 去除第一个/
        }

        String wildcardString = relatePath.substring(0, relatePath.lastIndexOf("../")+3);
        relatePath = relatePath.substring(relatePath.lastIndexOf("../") + 3);
        int containCnt = containCnt(wildcardString, "../");
        classPathAbsolutePath =cutLastStr(classPathAbsolutePath, "/", containCnt);
        String resourceAbsolutePath = classPathAbsolutePath + relatePath;
        URL resourceAbsoluteURL = new URL(resourceAbsolutePath);
        return resourceAbsoluteURL;
    }

    /**
     * 计算指定字符串出现的数量
     * @param s
     * @param target
     * @return
     */
    public static int containCnt(String s, String target){
        int rs = 0;
        int targetLen = target.length();
        while (s.contains(target)){
            rs++;
            s = s.substring(targetLen);
        }
        return rs;
    }

    /**
     *no use
     * @param s
     * @param target
     * @param cnt
     * @return
     */
    public static String cutLastStr(String s, String target, int cnt){
        for(int i=0; i<cnt; i++){
            s = s.substring(0, s.lastIndexOf(target, s.length()-2)+1);
        }
        return s;
    }


    /**
     * no use
     * @param resource
     * @return
     */
    public static URL getResourceOfClassRoot(String resource){
        return FileUtils.class.getClassLoader().getResource(resource);
    }


    public static String fileNameAddSuffix(String filePath, String suffix) {
        String sepa = File.separator;
        File file = new File(filePath);
        String parentPath = StringUtils.isBlank(file.getParent()) ? "" : (file.getParent() + sepa);
        return parentPath +
                FileUtils.getRemoveSuffixFileName(file.getName())
                + suffix +"." + FileUtils.getSuffixFromFileName(file.getName());
    }

    public static String fileNameAddPrefix(String filePath, String prefix) {
        String sepa = File.separator;
        File file = new File(filePath);
        String parentPath = StringUtils.isBlank(file.getParent()) ? "" : (file.getParent() + sepa);
        return parentPath +
                FileUtils.getRemoveSuffixFileName(prefix
                        + file.getName()) +"." + FileUtils.getSuffixFromFileName(file.getName());
    }

    public static String getParentDirName(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            return file.getName();
        }else {
            File file2 = new File(file.getParent());
            return file2.getName();
        }
    }

    public static String getRelativePath(String filePath, String rootPath) {
        if(StringUtils.isBlank(filePath)) {
            return null;
        }
        if(!StringUtils.endsWith(rootPath, "/")
                && !StringUtils.endsWith(rootPath, "\\")){
            if(rootPath.indexOf("/") > -1) {
                rootPath = rootPath + "/";
            }else {
                rootPath = rootPath + "\\";
            }
        }

        if(filePath.indexOf(rootPath) < 0) {
            if(rootPath.indexOf("/") > -1) {
                rootPath = rootPath.replace("/", "\\");
            }else {
                rootPath = rootPath.replace("\\", "/");
            }
        }

        return filePath.replace(rootPath,"");
    }

    public static ArrayList<File> getFiles(String path) {
        //目标集合fileList
        ArrayList<File> fileList = new ArrayList<File>();
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            ArrayList<File> fileListSub = new ArrayList<>();
            for (File fileIndex : files) {
                //如果这个文件是目录，则进行递归搜索
                if (fileIndex.isDirectory()) {
                    fileListSub.clear();
                    fileListSub = getFiles(fileIndex.getPath());
                    if (fileListSub != null && fileListSub.size() > 0) {
                        fileList.addAll(fileListSub);
                    }
                } else {
                    //如果文件是普通文件，则将文件句柄放入集合中
                    fileList.add(fileIndex);
                }
            }
        }
        return fileList;
    }

    /**
     * generate full file path by root path and relativePath
     * @param relativeFilePath
     * @param rootPath
     * @return
     */
    public static String genFilePath(String relativeFilePath, String rootPath) {
        String sepa = File.separator;
        if (StringUtils.isBlank(relativeFilePath)) {
            return null;
        }

        rootPath = StringUtils.isBlank(rootPath) ? "" : (rootPath + sepa);
        return rootPath + relativeFilePath;
    }
}
