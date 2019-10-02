package com.grasswort.picker.user.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.grasswort.picker.user.exception.JwtFreeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

/**
 * @author xuliangliang
 * @Classname JwtTokenUtils
 * @Description TODO
 * @Date 2019/10/1 22:20
 * @blame Java Team
 */
@Slf4j
public class JwtTokenUtil {

    private final static String ISSUER = "picker-server";
    private final static String USER = "user";
    private final static String SECRET = "324iu23094u598ndsofhsiufhaf_+0wq-42q421jiosadiusadiasd";

    /**
     * 生成 token
     * @param msg
     * @return
     */
    public static String creatJwtToken (String msg) {
        String token = JWT.create()
                .withIssuer(ISSUER).withExpiresAt(DateTime.now().plusDays(1).toDate())
                .withClaim(USER, msg)
                .sign(Algorithm.HMAC256(SECRET));
        return token;
    }

    /**
     * 解析 token
     * @return
     */
    public static String freeJwt (String token) throws JwtFreeException {
        DecodedJWT decodedJWT = null;
        try {
            // 使用hmac256加密算法
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET))
                    .withIssuer(ISSUER)
                    .build();
            decodedJWT = verifier.verify(token);
            log.info("\n签名人：{}，加密方式：{}，携带信息：{}。", decodedJWT.getIssuer(), decodedJWT.getAlgorithm(), decodedJWT.getClaim(USER).asString());
        } catch (Exception e) {
            log.info("\njwt解密出现错误，jwt或私钥或签证人不正确。");
            throw JwtFreeException.instance();
        }
        // 获得token的头部，载荷和签名，只对比头部和载荷
        String [] headPayload = token.split("\\.");
        // 获得jwt解密后头部
        String header = decodedJWT.getHeader();
        // 获得jwt解密后载荷
        String payload = decodedJWT.getPayload();
        if (! header.equals(headPayload[0]) && ! payload.equals(headPayload[1])) {
            throw JwtFreeException.instance();
        }
        String result = decodedJWT.getClaim(USER).asString();
        if (StringUtils.isNotBlank(result)) {
            throw JwtFreeException.instance();
        }
        return result;
    }
}
