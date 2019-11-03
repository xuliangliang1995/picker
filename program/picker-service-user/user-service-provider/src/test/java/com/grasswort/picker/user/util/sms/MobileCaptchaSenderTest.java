package com.grasswort.picker.user.util.sms;

import com.grasswort.picker.user.validator.Mobile;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author xuliangliang
 * @Classname MobileCaptchaSenderTest
 * @Description
 * @Date 2019/11/3 19:27
 * @blame Java Team
 */
public class MobileCaptchaSenderTest {

    @Test
    @Ignore
    public void send() {
        MobileCaptchaSender.Builder.aMobileCaptchaSender()
                .withPhone("18910422873")
                .withCaptcha("896323")
                .build().send();
    }
}