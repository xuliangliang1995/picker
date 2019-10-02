package com.grasswort.picker.user.exception;

/**
 * @author xuliangliang
 * @Classname TokenExpiredException
 * @Description TODO
 * @Date 2019/10/2 13:49
 * @blame Java Team
 */
public class TokenExpiredException extends AbstractTokenException {

    private TokenExpiredException() {
        super("token 已失效");
    }
    private static TokenExpiredException sington = new TokenExpiredException();

    public static TokenExpiredException getInstance() {
        return sington;
    }

}
