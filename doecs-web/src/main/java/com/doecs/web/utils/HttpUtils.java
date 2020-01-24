package com.doecs.web.utils;

import com.doecs.core.bean.BaseException;
import com.doecs.core.utils.JsonUtils;
import com.doecs.core.utils.StringUtils;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    public enum BrowserType {
        WE_CHAT, IOS, ANDROID, PC
    }
    /**
     * 获取Ip地址
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if(index != -1){
                return XFor.substring(0,index);
            }else{
                return XFor;
            }
        }
        XFor = Xip;
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }


    /**
     * 取得浏览器类型
     * @param request
     */
    public static BrowserType getBrowserType(HttpServletRequest request) {
        String info = request.getHeader("user-agent").toLowerCase();
        BrowserType rs = BrowserType.PC;//默认pc
        System.out.println(info);
        if (info.contains("micromessenger")) {
            rs = BrowserType.WE_CHAT;   //来自于微信
        } else if (info.contains("android")) {
            rs = BrowserType.ANDROID;   //来自于安卓
        } else if (info.contains("ios")) {
            rs = BrowserType.IOS;   //来自于ios
        }
        return rs;
    }

    /**
     * 以get方式提交请求到https链接
     *
     * @param url
     * 链接地址
     * @return
     */
    public static String httpsGet(String url) throws BaseException {
        HttpClient client = new HttpClient();
        Protocol myhttps = new Protocol("https", new SslSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);
        GetMethod get = new GetMethod(url);
        String respStr = "";
        try {
            get.getParams().setContentCharset("utf-8");
            client.executeMethod(get);
            respStr = get.getResponseBodyAsString();
        } catch (HttpException e) {
            throw new BaseException("remote request fail.").setRealException(e);
        } catch (IOException e) {
            throw new BaseException("remote request fail.").setRealException(e);
        } finally {
            if(get != null) {
                get.releaseConnection();
            }
        }
        return respStr;
    }

    /**
     * 以post方式提交请求到https链接
     *
     * @param url
     * 链接地址
     * @return
     */
    public static String httpsPost(String url, String JsonData) throws BaseException {
        HttpClient client = new HttpClient();
        Protocol myhttps = new Protocol("https", new SslSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);
        PostMethod postMethod = new PostMethod(url);
        String respStr = "";

        try {
            RequestEntity requestEntity = new StringRequestEntity(JsonData ,"application/json" ,"UTF-8");
            postMethod.setRequestEntity(requestEntity);
            postMethod.setRequestHeader("Content-Type","application/json");
            //默认的重试策略
            postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
            postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);//设置超时时间
            postMethod.getParams().setContentCharset("utf-8");
            client.executeMethod(postMethod);
            respStr = postMethod.getResponseBodyAsString();
        } catch (HttpException e) {
            throw new BaseException("remote request fail.").setRealException(e);
        } catch (IOException e) {
            throw new BaseException("remote request fail.").setRealException(e);
        } finally {
            if(postMethod != null) {
                postMethod.releaseConnection();
            }
        }
        return respStr;
    }

    /**
     * 获取请求Body String
     *
     * @param request
     * @return
     */
    public static String getBodyStr(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";

            while (reader != null && (line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
//            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }


    /**
     * 获取请求Body Map
     *
     * @param request
     * @return
     */
    public static Map<String, Object> getBodyMap(ServletRequest request) {
        return JsonUtils.json2Map(getBodyStr(request));
    }

    /**
     * 是否ajax请求
     * @param request
     * @return
     */
    public static boolean  isAjaxRequest(HttpServletRequest request){
        if (request.getHeader("x-requested-with") != null
                && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        }else{
            return false;
        }
    }

    // get request headers
    public static Map<String, String> getHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    public static boolean isOptionRequest(HttpServletRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod()) ? true : false;
    }
}
