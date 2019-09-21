package com.grasswort.picker.commons.result;

import java.io.Serializable;

/**
 * @author xuliangliang
 * @Classname AbstractResponse
 * @Description 响应
 * @Date 2019/9/21 16:31
 * @blame Java Team
 */
public abstract class AbstractResponse implements Serializable {

    private static final long serialVersionUID = -7587645525666387537L;

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
