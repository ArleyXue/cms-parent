package com.arley.cms.console.util;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author XueXianlei
 * @Description:
 * @date 2018/9/25 9:59
 */
public class JJWTUtils {

    private static final String key_prefix = "arley";

    public static String createJWT(String id, String subject, long ttlMillis, String keyStr) {
        // 指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 生成jwt的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        // 创建payload的私有声明(根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的)
        /*Map<String,Object> claims = new HashMap<>();
        claims.put("uid", "DSSFAWDWADAS...");
        claims.put("user_name", "admin");
        claims.put("nick_name","DASDA121");*/
        SecretKey key = generalKey(keyStr);

        //下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder，设置jwt的body
               // .setClaims(claims)          //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(id)                  //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)           //iat: jwt的签发时间
                .setSubject(subject)        //sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .signWith(signatureAlgorithm, key);//设置签名使用的签名算法和签名使用的秘钥
        if (ttlMillis >= 0) {
            //设置过期时间
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 解密jwt
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt, String keyStr) {
        SecretKey key = generalKey(keyStr);  //签名秘钥，和生成的签名的秘钥一模一样
        return Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(key)         //设置签名的秘钥
                .parseClaimsJws(jwt).getBody();
    }

    /**
     * 由字符串生成加密key
     * @return
     */
    public static SecretKey generalKey(String stringKey){
        //本地的密码解码[B@152f6e2
        byte[] encodedKey = Base64.decodeBase64(key_prefix + stringKey);
        // 根据给定的字节数组使用AES加密算法构造一个密钥，使用 encodedKey中的始于且包含 0 到前 leng 个字节这是当然是所有。（后面的文章中马上回推出讲解Java加密和解密的一些算法）
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }


    public static void main(String[] args) {
        String id = UUID.randomUUID().toString();
        Map<String, String> data = new HashMap<>();
        data.put("userId", "1234444444");
        data.put("name", "23232");
        String abc = createJWT(id, JSON.toJSONString(data), -1, "abc");
        System.out.println(abc);

        Claims abc1 = parseJWT(abc, "abc");
        String subject = abc1.getSubject();
        Map map = JSON.parseObject(subject, Map.class);

        System.out.println(abc1);
    }
}
