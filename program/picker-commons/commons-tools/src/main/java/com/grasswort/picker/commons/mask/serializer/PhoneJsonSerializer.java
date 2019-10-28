package com.grasswort.picker.commons.mask.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.grasswort.picker.commons.mask.MaskUtil;

import java.io.IOException;

/**
 * @author xuliangliang
 * @Classname PhoneJsonSerializer
 * @Description 手机号脱敏
 * @Date 2019/10/15 13:16
 * @blame Java Team
 */
public class PhoneJsonSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(MaskUtil.maskMobile(s));
    }
}
