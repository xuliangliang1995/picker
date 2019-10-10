package com.grasswort.picker.user.service.mailbuilder.wrapper;

import lombok.Data;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname AbstractMailInfoWrapper
 * @Description 邮件信息封装
 * @Date 2019/10/10 17:40
 * @blame Java Team
 */
@Data
public abstract class AbstractMailInfoWrapper {

    private List<String> receivers;

}
