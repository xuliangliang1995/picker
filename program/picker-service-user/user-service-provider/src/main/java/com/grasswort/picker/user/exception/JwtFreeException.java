package com.grasswort.picker.user.exception;

/**
 * @author xuliangliang
 * @Classname JwtFreeException
 * @Description TODO
 * @Date 2019/10/1 22:42
 * @blame Java Team
 */
public class JwtFreeException extends Exception {

    private JwtFreeException() {
    }

    public static JwtFreeException instance() {
        return SingtonHolder.sington;
    }

    private static class SingtonHolder {
        static JwtFreeException sington = new JwtFreeException();
    }
}
