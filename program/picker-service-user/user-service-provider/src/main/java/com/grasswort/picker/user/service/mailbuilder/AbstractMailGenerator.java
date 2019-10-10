package com.grasswort.picker.user.service.mailbuilder;

import com.grasswort.picker.email.model.Mail;

/**
 * @author xuliangliang
 * @Classname AbstractMailGenerator
 * @Description 邮箱生成类
 * @Date 2019/10/10 17:07
 * @blame Java Team
 */
public abstract class AbstractMailGenerator<T> {

    public abstract Mail generate(T t);
}
