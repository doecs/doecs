package com.doecs.core.utils;

import java.io.*;

/**
 *
 * Java原生版的 Serialize
 * 
 */
@SuppressWarnings("unchecked")
public class SerializeUtils {
	static final Class<?> CLAZZ = SerializeUtils.class;
	
    public static byte[] serialize(Object value) {
        if (value == null) { 
            throw new NullPointerException("Can't serialize null");
        }
        byte[] rv = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;
        try {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            os.writeObject(value);
            os.close();
            bos.close();
            rv = bos.toByteArray();
        } catch (Exception e) {
        	LogUtils.getLogger().error(String.format("serialize error %s", JsonUtils.bean2Json(value)), e);
        } finally {
            try {
                close(os);
                close(bos);
            } catch (Exception e) {
                LogUtils.getLogger().error(String.format("close out stream error %s", JsonUtils.bean2Json(value)), e);
            }
        }
        return rv;
    }

    
	public static Object deserialize(byte[] in) {
        return deserialize(in, Object.class);
    }

    public static <T> T deserialize(byte[] in, Class<T>...requiredType) {
        Object rv = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream is = null;
        try {
            if (in != null) {
                bis = new ByteArrayInputStream(in);
                is = new ObjectInputStream(bis);
                rv = is.readObject();
            }
        } catch (Exception e) {
            LogUtils.getLogger().error(String.format("serialize error %s", new String(in)), e);
        } finally {
            try{
                close(is);
                close(bis);
            } catch (Exception e) {
                LogUtils.getLogger().error(String.format("close out stream error %s", new String(in)), e);
            }
        }
        return (T) rv;
    }

    private static void close(Closeable closeable) throws IOException {
        if (closeable != null)
            try {
                closeable.close();
            } catch (IOException e) {
            	 LogUtils.getLogger().error("close stream error", e);
            	 throw e;
            }
    }

}
