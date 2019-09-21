package com.grasswort.picker.commons.result;

import java.io.Serializable;

/**
 * @author xuliangliang
 * @Classname AbstractRequest
 * @Description 抽象请求
 * @Date 2019/9/21 16:31
 * @blame Java Team
 */
public abstract class AbstractRequest implements Serializable {
    private static final long serialVersionUID = -6407706041102956997L;

    public abstract void requestCheck();

    @Override
    public String toString() {
        return "AbstractRequest{}";
    }
}
