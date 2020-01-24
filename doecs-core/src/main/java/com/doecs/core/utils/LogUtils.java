package com.doecs.core.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtils {

    private static Logger logger =  null;

    public static Logger getLogger(){
        if (null == logger){
            //适用于Java7。Java8 废弃了Reflection.getCallerClass()
//            logger = LogManager.getLogger(Reflection.getCallerClass().getName());

            // 适用于java8以后版本
            logger = LogManager.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());

//            logger.debug("调用者类名"+Reflection.getCallerClass().getKey());
        }
        return logger;
    }
}
