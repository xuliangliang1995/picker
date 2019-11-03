package com.grasswort.picker.user.util.sms;

import com.alibaba.fastjson.JSONObject;
import com.grasswort.picker.user.util.AliSmsTemplate;
import com.grasswort.picker.user.util.AliSmsUtil;
import com.sun.org.apache.bcel.internal.generic.ALOAD;

import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname MobileCaptchaSender
 * @Description 手机验证码发送器
 * @Date 2019/11/3 19:22
 * @blame Java Team
 */
public class MobileCaptchaSender {

    private String phone;

    private String captcha;

    /**
     * 发送短信
     * @return
     */
    public Optional<String> send() {
        JSONObject jsonObject = new JSONObject();
        return AliSmsUtil.sendSm(AliSmsTemplate.CAPTCHA, phone, String.format("{\"code\":\"%s\"}", captcha));
    }


    public static final class Builder {
        private String phone;
        private String captcha;

        private Builder() {
        }

        public static Builder aMobileCaptchaSender() {
            return new Builder();
        }

        public Builder withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder withCaptcha(String captcha) {
            this.captcha = captcha;
            return this;
        }

        public MobileCaptchaSender build() {
            MobileCaptchaSender mobileCaptchaSender = new MobileCaptchaSender();
            mobileCaptchaSender.phone = this.phone;
            mobileCaptchaSender.captcha = this.captcha;
            return mobileCaptchaSender;
        }
    }
}
