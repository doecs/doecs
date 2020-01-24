package com.doecs.core.utils;

import java.io.*;
import java.lang.reflect.Field;

public class ObjUtils {

    /**
     * Returns a copy of the object, or null if the object cannot
     * be serialized.
     * @param orig an <code>Object</code> value
     * @return a deep copy of that Object
     * @exception NotSerializableException if an error occurs
     */
    public static Object deepCopy(Object orig) throws NotSerializableException {
        Object obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        } catch (NotSerializableException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }

    public static void setProperty(Object obj, String PropertyName, Object value)
            throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {
        // 获取obj类的字节文件对象
        Class c = obj.getClass();

        // 获取该类的成员变量
        Field f = c.getDeclaredField(PropertyName);

        // 取消语言访问检查
        f.setAccessible(true);

        // 给变量赋值
        f.set(obj, value);
    }

    public static Object getProperty(Object obj, String PropertyName)
            throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {
        // 获取obj类的字节文件对象
        Class c ;
        if (obj instanceof Class) {
            c = (Class)obj;     // 静态属性
        }else{
            c = obj.getClass();
        }

        // 获取该类的成员变量
        Field f = c.getDeclaredField(PropertyName);

        // 取消语言访问检查
        f.setAccessible(true);

        // 给变量赋值
        if (obj instanceof Class) {
            return f.get(null); // 静态属性
        }else{
            return f.get(obj);
        }
    }
}
