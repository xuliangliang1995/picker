package com.grasswort.picker.user.exception;

/**
 * @author xuliangliang
 * @Classname TokenVerifyFailException
 * @Description TODO
 * @Date 2019/10/2 13:49
 * @blame Java Team
 */
public class TokenVerifyFailException extends AbstractTokenException {

    private TokenVerifyFailException() {
        super("token 校验失败");
    }

    private static TokenVerifyFailException sington = new TokenVerifyFailException();

    public static TokenVerifyFailException getInstance() {
        return sington;
    }

}
