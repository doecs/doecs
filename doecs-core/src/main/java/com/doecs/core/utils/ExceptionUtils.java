package com.doecs.core.utils;

import com.doecs.core.bean.BaseException;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
    public static void handle(Exception e) {
        if (e instanceof BaseException) {
            LogUtils.getLogger().debug(e.getMessage(), e);
        } else {
            LogUtils.getLogger().error(e.getMessage(), e);
        }
    }

    public static String getStackTrace(Throwable throwable)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try
        {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally
        {
            pw.close();
        }
    }
}
