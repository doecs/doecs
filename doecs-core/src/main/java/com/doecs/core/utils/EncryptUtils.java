package com.doecs.core.utils;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static sun.security.x509.CertificateAlgorithmId.ALGORITHM;

public class EncryptUtils {
    private static final String ENCODING = "UTF-8";

    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String md5=new BigInteger(1, md.digest()).toString(16);
            //BigInteger会把0省略掉，需补全至32位
            return fillMD5(md5);
        } catch (Exception e) {
            throw new RuntimeException("MD5加密错误:"+e.getMessage(),e);
        }
    }

    public static String fillMD5(String md5){
        return md5.length()==32?md5:fillMD5("0"+md5);
    }


    public static String getSha256(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            BASE64Encoder base64Encoder = new BASE64Encoder();
            encodeStr = base64Encoder.encode(messageDigest.digest());

//            BASE64Encoderode(messageDigest.digest());
//            encodeStr =  messageDigest.digest();
//            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }
    /**
     * 将byte转为16进制
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    //***************************签名和验证*******************************
    public static byte[] sign(byte[] data, String str_priK, String SIGNATURE_ALGORITHM) throws Exception{
        PrivateKey priK = getRsaPrivateKey(str_priK);
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        sig.initSign(priK);
        sig.update(data);
        return sig.sign();
    }

    /**
     *
     * @param data
     * @param sign
     * @param str_pubK
     * @param SIGNATURE_ALGORITHM: SHA256WithRSA, SHA1WithRSA, MD5withRSA
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, byte[] sign, String str_pubK, String SIGNATURE_ALGORITHM) throws Exception{
        PublicKey pubK = getRsaPublicKey(str_pubK);
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        sig.initVerify(pubK);
        sig.update(data);
        return sig.verify(sign);
    }


    /**
     * 使用getPublicKey得到公钥,返回类型为PublicKey
     * @param key String to PublicKey
     * @throws Exception
     */
    public static PublicKey getRsaPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
    /**
     * 转换私钥
     * @param key String to PrivateKey
     * @throws Exception
     */
    public static PrivateKey getRsaPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 加密方法
     * @param source 源数据
     * @return
     * @throws Exception
     */
    public static String rsaEncrypt(String source, String publicKeyBase64Str) throws Exception {
        PublicKey publicKey = getRsaPublicKey(publicKeyBase64Str);

        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] b = source.getBytes();
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(b);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(b1);
    }

    /**
     * 加密方法
     * @param source 源数据
     * @return
     * @throws Exception
     */
    public static String rsaEncryptByKeyFile(String source, String PUBLIC_KEY_FILE) throws Exception {
        Key publicKey = getKeyFromFile(PUBLIC_KEY_FILE);

        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] b = source.getBytes();
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(b);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(b1);
    }

    /**
     * 解密算法
     * @param cryptograph    密文
     * @return
     * @throws Exception
     */
    public static String rsaDecrypt(String cryptograph, String privateKeyBase64Str) throws Exception {

        Key privateKey = getRsaPrivateKey(privateKeyBase64Str);

        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b1 = decoder.decodeBuffer(cryptograph);

        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }

    /**
     * 解密算法
     * @param cryptograph    密文
     * @return
     * @throws Exception
     */
    public static String rsaDecryptByKeyFile(String cryptograph, String PRIVATE_KEY_FILE) throws Exception {

        Key privateKey = getKeyFromFile(PRIVATE_KEY_FILE);

        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b1 = decoder.decodeBuffer(cryptograph);

        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }

    private static Key getKeyFromFile(String fileName) throws Exception, IOException {
        Key key;
        ObjectInputStream ois = null;
        try {
            /** 将文件中的私钥对象读出 */
            ois = new ObjectInputStream(new FileInputStream(fileName));
            key = (Key) ois.readObject();
        } catch (Exception e) {
            throw e;
        } finally {
            ois.close();
        }
        return key;
    }

    //safeUrlBase64Encode加密
    public static String safeUrlBase64Encode(String data) {
        return safeUrlBase64Encode(data.getBytes());
    }

    public static String safeUrlBase64Encode(byte[] data){
        String encodeBase64 = new BASE64Encoder().encode(data);
        String safeBase64Str = encodeBase64.replace('+', '-');
        safeBase64Str = safeBase64Str.replace('/', '_');
        safeBase64Str = safeBase64Str.replaceAll("=", "");
        return safeBase64Str;
    }

    //safeUrlBase64Encode解密
    public static byte[] safeUrlBase64Decode(final String safeBase64Str) throws IOException {
        String base64Str = safeBase64Str.replace('-', '+');
        base64Str = base64Str.replace('_', '/');
        int mod4 = base64Str.length()%4;
        if(mod4 > 0){
            base64Str = base64Str + "====".substring(mod4);
        }
        return new BASE64Decoder().decodeBuffer(base64Str);
    }

    //urlsafe_base64 加密
    public static String encoded(String data) throws UnsupportedEncodingException {
        byte[] b = Base64.encodeBase64URLSafe(data.getBytes(ENCODING));
        return new String(b, ENCODING);
    }

    public static void main(String[] args) throws Exception {
//    // 应用私钥
        final String str_priK = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCS367ma7iUfnlAS+tc85d8i9/wsdMOczXqRg8kfiYUv3VfdXHqCyQtoWecUxyPUtLrxubB8Y39yg1Uf5TRkURDkFUm93PhDQjN/z06oYbT2nzAaMYuawfpavJ83+afaJ9rUItcVfqCmdHot/1I4gPtRKxK9uWfEUuEDcz/UATs1u0vaXOh7mCamLwTSMvaEfKtw6CXuRkFtZYqt7VcG34AsG3Tjw+RJQ4GLdMMjZYDcQXqac8hPCLQSiif0+alCAvlL2XHoEZgeQdFapFySl1yq7W/rODafOnf9EQAvtuFMiGSqTfUVuzi1qmZ785ZlBrG6Ywk7l/W/ug3saWAHI6NAgMBAAECggEAUPvVTGgJFpfF85Xo6Q/Hb30QtX7XiRgtmkeXAeAszzPiXAg+D3alNCPTJXcOQFjCxA6gHkA6sr0+LGFMhWE76qUizqGS23x8ZlleQX3fAeFu+AjNfbq2WhYGLqSmXS8q99kpfnhJ8Mkv3MnWduFomONMvOj2I4c9xEp3xi8xCAWBMNO+pwQ0TCfQ8EuJL2subzhhhFWTTRqsnpX5HnKHIOMdoU0zparQ4gf0QTizaslDTnRIJAn/YRzjuF1WWEhNr3DfhRq9e1x2eM/SeXkhMgurE6ZQHNs189TfqTu8zrfRSXy2Xo/VozB2h6cMQbdeyonKIiTYqNewGQp5XO8OYQKBgQDmS8MdgLSJQIxRSJAAeVjkfxCAOLYlgvfSVJBEXOMRXFkVXcps1gILLj9hkHY0279lO+4JJeYPIJ6nxoJITOKZviMUagkCuifyj2DyyF9jZnlJ63/0ojsHmXBKVSY9rsMsxp6evVZ/xEyataE0PWu59wyqIxxFId0ETu+cCoEQjwKBgQCjRE1N1fEMHQmMHal7VQRVsGFIaCSBWUUJFY4GXEErYpUhrk86JUytZnZrEniZv5sVqQuuO5cdhozr3s8MiXaBV9r2Ii4AOFqW2w7shpV+BU4KznEE6FKGwKEx2TH+DdQe5awtyAJ+U7a0qi0F1+VFibIA7HYocHCpW+ckU9OFIwKBgFnB95cdHcBM5yObG30DzrCoEX9YGy39oKdajCwI4/tVOedI+ed3da5z5QIasuehKjTDqd/o5ITK2utbRFX3mW4AnF2NgluMwJsaZNfbXso4G1fDMMjhHg61L/3YuCHcZneiJhqKgxcjJBQ8bEsXEVCWyFnbe146d5EBTwRLV8hjAoGAbG17+WlQAH94S7mXSPUZP983fGLNe6nRk0rhW5ABJ9L8v7WHoFINLkuR8WH+/fyl/iiaPUgN/+nPxQXf86+HKDc0j6n7auICJ5Wrv6FRvrO/NKYNoPi5dgyQAQ8BiHuemDZntq/SGBt4h1tNM0hCV4Au2zYu8oRaDd+fDh0tqIkCgYBbEHe8/ZaRuGB0BklLmS4t68K5JGor6NU+Z65iUJZMQTgwaC0vf8IrJ0qnFA8YxISzChQLMuGzRUOYgizhUHTHUBrVhZYwk/bARXtG8x8eCYB0IaRI1UY1W/m01VFulQY/Tf1WarXZkHYRxn5PxJoLago9TMr9f7NdfxZ6NccC4w==";
//
//    // 应用公钥
        final String str_pubK = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkt+u5mu4lH55QEvrXPOXfIvf8LHTDnM16kYPJH4mFL91X3Vx6gskLaFnnFMcj1LS68bmwfGN/coNVH+U0ZFEQ5BVJvdz4Q0Izf89OqGG09p8wGjGLmsH6WryfN/mn2ifa1CLXFX6gpnR6Lf9SOID7USsSvblnxFLhA3M/1AE7NbtL2lzoe5gmpi8E0jL2hHyrcOgl7kZBbWWKre1XBt+ALBt048PkSUOBi3TDI2WA3EF6mnPITwi0Eoon9PmpQgL5S9lx6BGYHkHRWqRckpdcqu1v6zg2nzp3/REAL7bhTIhkqk31Fbs4tapme/OWZQaxumMJO5f1v7oN7GlgByOjQIDAQAB";
//
//        String str_pubK = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqPvovSfXcwBbW8cKMCgwqNpsYuzF8RPAPFb7LGsnVo44JhM/xxzDyzoYtdfNmtbIuKVi9PzIsyp6rg+09gbuI6UGwBZ5DWBDBMqv5MPdOF5dCQkB2Bbr5yPfURPENypUz+pBFBg41d+BC+rwRiXELwKy7Y9caD/MtJyHydj8OUwIDAQAB";
//        String str_priK = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKo++i9J9dzAFtbxwowKDCo2mxi7MXxE8A8VvssaydWjjgmEz/HHMPLOhi1182a1si4pWL0/MizKnquD7T2Bu4jpQbAFnkNYEMEyq/kw904Xl0JCQHYFuvnI99RE8Q3KlTP6kEUGDjV34EL6vBGJcQvArLtj1xoP8y0nIfJ2Pw5TAgMBAAECgYAGGB8IllMwxceLhjf6n1l0IWRH7FuHIUieoZ6k0p6rASHSgWiYNRMxfecbtX8zDAoG0QAWNi7rn40ygpR5gS1fWDAKhmnhKgQIT6wW0VmD4hraaeyP78iy8BLhlvblri2nCPIhDH5+l96v7D47ZZi3ZSOzcj89s1eS/k7/N4peEQJBAPEtGGJY+lBoCxQMhGyzuzDmgcS1Un1ZE2pt+XNCVl2b+T8fxWJH3tRRR8wOY5uvtPiK1HM/IjT0T5qwQeH8Yk0CQQC0tcv3d/bDb7bOe9QzUFDQkUSpTdPWAgMX2OVPxjdq3Sls9oA5+fGNYEy0OgyqTjde0b4iRzlD1O0OhLqPSUMfAkEAh5FIvqezdRU2/PsYSR4yoAdCdLdT+h/jGRVefhqQ/6eYUJJkWp15tTFHQX3pIe9/s6IeT/XyHYAjaxmevxAmlQJBAKSdhvQjf9KAjZKDEsa7vyJ/coCXuQUWSCMNHbcR5aGfXgE4e45UtUoIE1eKGcd6AM6LWhx3rR6xdFDpb9je8BkCQB0SpevGfOQkMk5i8xkEt9eeYP0fi8nv6eOUcK96EXbzs4jV2SAoQJ9oJegPtPROHbhIvVUmNQTbuP10Yjg59+8=";
        String str_plaintext = "这是一段用来测试密钥转换的明文";
        System.err.println("明文："+str_plaintext);
        String bt_cipherBase64 = rsaEncrypt(str_plaintext, str_pubK);
        System.out.println("加密后："+ bt_cipherBase64);

        String bt_original = rsaDecrypt(bt_cipherBase64, str_priK);
        String str_original = new String(bt_original);
        System.out.println("解密结果:"+str_original);

        String str="被签名的内容";
        System.err.println("\n原文:"+str);
        byte[] signature=sign(str.getBytes(), str_priK, "SHA256WithRSA");
        System.out.println("产生签名："+ Base64.encodeBase64String(signature));
        boolean status=verify(str.getBytes(), signature, str_pubK, "SHA256WithRSA");
        System.out.println("验证情况："+status);
    }
}
