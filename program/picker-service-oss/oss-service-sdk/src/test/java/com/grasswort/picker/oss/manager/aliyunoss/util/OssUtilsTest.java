package com.grasswort.picker.oss.manager.aliyunoss.util;

import com.grasswort.picker.oss.manager.aliyunoss.constant.OssStipulation;
import com.grasswort.picker.oss.manager.aliyunoss.dto.OssRefDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author xuliangliang
 * @Classname OssUtilsTest
 * @Description TODO
 * @Date 2019/10/17 21:51
 * @blame Java Team
 */
public class OssUtilsTest {

    @Test
    public void generateOssKeyName() {
        String key =  OssUtils.generateOssKeyName();
        assertEquals("OSS key 生成长度有误", OssStipulation.OSS_KEY_NAME_LENGTH, key.length());
    }

    @Test
    public void findOssUrlFromText() {
        String image1 = "https://grasswort-petals-img.oss-cn-beijing.aliyuncs.com/20190908/053e86e7bbc273a9861039278588e9b2.png";
        String image2 = "https://picker-oss.oss-cn-beijing.aliyuncs.com/20190908/053e86e7bbc273a9861039278588e9b2.jpeg";
        String text = RandomStringUtils.random(10).concat(image1).concat("_").concat(image2)
                .concat(RandomStringUtils.randomAlphabetic(5)).concat(image1);

        List<OssRefDTO> refs = OssUtils.findOssUrlFromText(text);
        assertEquals("匹配 OSS url 数量不对", 3, refs.size());
    }
}