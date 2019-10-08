package com.grasswort.picker.user.constants;

import com.grasswort.picker.email.constants.EmailCenterConstant;

/**
 * @author xuliangliang
 * @Classname PickerActivateMailConfig
 * @Description 激活邮件配置
 * @Date 2019/10/6 22:57
 * @blame Java Team
 */
public class PickerActivateEmailMetaData {

    public static final String PICKER_ACTIVATE_TOPIC = EmailCenterConstant.TOPIC_EMAIL_PREFIX.concat("user-activate-email");

    public static final int PARTITIONS = 5;

    public static final short REPLICATION_FACTOR = 2;

    public static final String SUBJECT = "Picker 账户激活";

    public static final String TEMPLATE_PATH = "emailTemplate";

    public static final String TEMPLATE_NAME = "picker_activate.html";

    public static class Key {
        public static final String TITLE = "title";

        public static final String URL = "url";
    }
}
