package com.grasswort.picker.commons.mask;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author xuliangliang
 * @Classname MaskUtilTest
 * @Description TODO
 * @Date 2019/10/15 12:50
 * @blame Java Team
 */
public class MaskUtilTest {

    @Test
    public void maskMobile() {
        String phone = "18910422873";
        String maskPhone = MaskUtil.maskMobile(phone);
        assertTrue(maskPhone.length() == phone.length());
        assertTrue(maskPhone.contains("*"));
    }

    @Test
    public void maskEmail() {
        String email = "835547206@qq.com";
        String maskEmail = MaskUtil.maskEmail(email);
        assertTrue(maskEmail.length() == email.length());
        assertTrue(maskEmail.contains("*"));
    }

    @Test
    public void maskIdCardNo() {
        String idCard = "678323199898337281";
        String maskIdCard = MaskUtil.maskIdCardNo(idCard);
        assertTrue(maskIdCard.length() == idCard.length());
        assertTrue(maskIdCard.contains("*"));
    }
}