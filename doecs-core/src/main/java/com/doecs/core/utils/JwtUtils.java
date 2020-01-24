package com.doecs.core.utils;

import com.doecs.core.bean.BaseException;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    /**
     * 创建jwt access token
     * @param id 不重复的值，如uuid
     * @param subject jwt的所有者、为json格式，如：{"id":"xxx"}
     * @param ttlMillis 过期的时间长度
     * @return access token
     * @throws Exception
     */
    public static String createAccessJWT(String id, String subject, Map<String,Object> claims, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        long nowMillis = System.currentTimeMillis();//生成JWT的时间
        Date now = new Date(nowMillis);
        SecretKey key = getAccessKey();//生成签名的时候使用的秘钥secret,这个方法本地封装了的，一般可以从本地配置文件中读取，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claims)          //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(id)                  //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)           //iat: jwt的签发时间
                .setSubject(subject)        //sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
//                .signWith(signatureAlgorithm, key);//设置签名使用的签名算法和签名使用的秘钥
                .signWith(key);//设置签名使用的签名算法和签名使用的秘钥
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);     //设置过期时间
        }
        return builder.compact();           //就开始压缩为xxxxxxxxxxxxxx.xxxxxxxxxxxxxxx.xxxxxxxxxxxxx这样的jwt
    }

    /**
     * 创建jwt refresh token
     * @param id 不重复的值，如uuid
     * @param subject jwt的所有者、为json格式，如：{"id":"xxx"}
     * @param ttlMillis 过期的时间长度
     * @return refresh token
     */
    public static String createRefreshJWT(String id, String subject, Map<String,Object> claims, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        long nowMillis = System.currentTimeMillis();//生成JWT的时间
        Date now = new Date(nowMillis);
        SecretKey key = getRerfreshKey();//生成签名的时候使用的秘钥secret,这个方法本地封装了的，一般可以从本地配置文件中读取，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claims)          //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(id)                  //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)           //iat: jwt的签发时间
                .setSubject(subject)        //sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
//                .signWith(signatureAlgorithm, key);//设置签名使用的签名算法和签名使用的秘钥
                .signWith(key);//设置签名使用的签名算法和签名使用的秘钥
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);     //设置过期时间

//            LogUtils.getLogger().debug("jay refresh exp:"+ exp);
        }
        return builder.compact();           //就开始压缩为xxxxxxxxxxxxxx.xxxxxxxxxxxxxxx.xxxxxxxxxxxxx这样的jwt
    }

    /**
     * 解析jwt access token
     * @param jwt
     * @return
     */
    public static Claims parseAccessJWT(String jwt) {
//        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        Claims claims = null;
        SecretKey key = getAccessKey();  //签名秘钥，和生成的签名的秘钥一模一样
        try{
            claims = Jwts.parser()  //得到DefaultJwtParser
                    .setSigningKey(key)         //设置签名的秘钥
                    .parseClaimsJws(jwt).getBody();//设置需要解析的jwt

        }catch (ExpiredJwtException e){
            throw new BaseException("sign expired").setRealException(e);
        }
        return claims;
    }

    /**
     * 解析jwt refresh token
     * @param jwt
     * @return
     */
    public static Claims parseRefreshJWT(String jwt) {
//        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        Claims claims = null;
        SecretKey key = getRerfreshKey();  //签名秘钥，和生成的签名的秘钥一模一样
        try{
            claims = Jwts.parser()  //得到DefaultJwtParser
                    .setSigningKey(key)         //设置签名的秘钥
                    .parseClaimsJws(jwt)
                    .getBody();//设置需要解析的jwt

        }catch (ExpiredJwtException e){
            throw new BaseException("refresh token expired").setRealException(e);
        }
        return claims;
    }

    /**
     * 由字符串生成加密key
     * @return
     */
    public static SecretKey getAccessKey(){
        String stringKey = "*cVd)-6FYvS-97zEU-HT^SJ-9qd6&-Cz*md-;fpGt314wer2341231vxfcvgrt22"; //Constant.JWT_SECRET;//本地配置文件中加密的密文
        byte[] encodedKey = Base64.decodeBase64(stringKey); //本地的密码解码[B@152f6e2
        SecretKeySpec key = new SecretKeySpec(encodedKey, "HmacSHA256");
        return key;
    }

    /**
     * 由字符串生成加密key
     * @return
     */
    public static SecretKey getRerfreshKey(){
        String stringKey = "d9d97d8f70SJ-9qd6&-Cz*md-@f0b4ce1734018e1dSJ-9qd6&-Cz*md-&ff7062"; //Constant.JWT_SECRET;//本地配置文件中加密的密文
        byte[] encodedKey = Base64.decodeBase64(stringKey); //本地的密码解码[B@152f6e2
        SecretKeySpec key = new SecretKeySpec(encodedKey, "HmacSHA256");
        return key;
    }

    public static void main(String[] args) throws Exception {
//        System.out.println("1:" + (Keys.secretKeyFor(SignatureAlgorithm.HS256)).getEncoded());
//        System.out.println("1:" + Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());

        //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String,Object> claims = new HashMap<String,Object>();
        claims.put("uid", "DSSFAWDWADAS...");
        claims.put("user_name", "admin");
        claims.put("nick_name","DASDA121");
        String ab= createAccessJWT("jwt","{id:100,name:xiaohong}", claims,60000);
        System.out.println(ab);
        //eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiJEU1NGQVdEV0FEQVMuLi4iLCJzdWIiOiJ7aWQ6MTAwLG5hbWU6eGlhb2hvbmd9IiwidXNlcl9uYW1lIjoiYWRtaW4iLCJuaWNrX25hbWUiOiJEQVNEQTEyMSIsImV4cCI6MTUxNzgzNTE0NiwiaWF0IjoxNTE3ODM1MDg2LCJqdGkiOiJqd3QifQ.ncVrqdXeiCfrB9v6BulDRWUDDdROB7f-_Hg5N0po980
        String jwt="eyJhbGciOiJIUzI1NiJ9.eyJuaWNrX25hbWUiOiJEQVNEQTEyMSIsInVpZCI6IkRTU0ZBV0RXQURBUy4uLiIsInVzZXJfbmFtZSI6ImFkbWluIiwianRpIjoiand0IiwiaWF0IjoxNTMzMjAxMjQ4LCJzdWIiOiJ7aWQ6MTAwLG5hbWU6eGlhb2hvbmd9IiwiZXhwIjoxNTMzMjAxODQ4fQ.LdV88BMulpEZhBqLQQE6tVDyI1pqsBgUGmqToYcOVNw";
        Claims c= parseAccessJWT(ab);//注意：如果jwt已经过期了，这里会抛出jwt过期异常。
        System.out.println(c.getId());//jwt
        System.out.println("Issue："+c.getIssuedAt());//Mon Feb 05 20:50:49 CST 2018
        System.out.println(c.getSubject());//{id:100,name:xiaohong}
        System.out.println(c.getIssuer());//null
        System.out.println(c.get("uid", String.class));//DSSFAWDWADAS...
        System.out.println(c.toString());
        System.out.println("Exp："+c.getExpiration());
    }
}
