package com.grasswort.picker.commons.mask.serializer;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.grasswort.picker.commons.mask.MaskUtil;

import java.io.IOException;

/**
 * @author xuliangliang
 * @Classname EmailJsonSerilizer
 * @Description 邮箱脱敏
 * @Date 2019/10/15 13:12
 * @blame Java Team
 */
public class EmailJsonSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(MaskUtil.maskEmail(s));
    }
}
