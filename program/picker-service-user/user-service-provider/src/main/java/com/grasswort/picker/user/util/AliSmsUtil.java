package com.grasswort.picker.user.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname AliSmsUtil
 * @Description 阿里短信
 * @Date 2019/11/3 18:45
 * @blame Java Team
 */
public class AliSmsUtil {
    private static final String REGION_ID = "cn-hangzhou";
    private static final String ACCESS_KEY_ID = "LTAI4Fp697iaTsUC5tLNtsxz";
    private static final String ACCESS_KEY_SECRET = "klxWoY3gVtR0OSZ5MENlBbhlvniEdC";
    private static final String SYS_DOMAIN = "dysmsapi.aliyuncs.com";
    private static final String SYS_VERSION = "2017-05-25";
    private static final String SYS_ACTION = "SendSms";
    private static final IAcsClient CLIENT = new DefaultAcsClient(DefaultProfile.getProfile(REGION_ID, ACCESS_KEY_ID, ACCESS_KEY_SECRET));
    private static final String SUCCESS_CODE = "OK";


    /**
     * 发送个短信
     * @param template
     * @param phone
     * @param json
     * @return
     */
    public static Optional<String> sendSm(String template, String phone, JSONObject json) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(SYS_DOMAIN);
        request.setSysVersion(SYS_VERSION);
        request.setSysAction(SYS_ACTION);
        request.putQueryParameter("RegionId", REGION_ID);
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "Picker");
        request.putQueryParameter("TemplateCode", template);
        request.putQueryParameter("TemplateParam", json.toJSONString());
        try {
            CommonResponse response = CLIENT.getCommonResponse(request);
            return Optional.ofNullable(response).map(CommonResponse::getData).map(str -> JSON.parseObject(str))
                    .filter(j -> SUCCESS_CODE.equalsIgnoreCase(j.getString("Code")))
                    .map(j -> j.getString("RequestId"));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * 发送个短信
     * @param template
     * @param phone
     * @param json
     * @return
     */
    public static Optional<String> sendSm(String template, String phone, String json) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(SYS_DOMAIN);
        request.setSysVersion(SYS_VERSION);
        request.setSysAction(SYS_ACTION);
        request.putQueryParameter("RegionId", REGION_ID);
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "Picker");
        request.putQueryParameter("TemplateCode", template);
        request.putQueryParameter("TemplateParam", json);
        try {
            CommonResponse response = CLIENT.getCommonResponse(request);
            return Optional.ofNullable(response).map(CommonResponse::getData).map(str -> JSON.parseObject(str))
                    .filter(j -> SUCCESS_CODE.equalsIgnoreCase(j.getString("Code")))
                    .map(j -> j.getString("RequestId"));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /*public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "782383");
        System.out.println(AliSmsUtil.sendSm(AliSmsTemplate.CAPTCHA, "18910422873", jsonObject).get());;
    }*/
}
