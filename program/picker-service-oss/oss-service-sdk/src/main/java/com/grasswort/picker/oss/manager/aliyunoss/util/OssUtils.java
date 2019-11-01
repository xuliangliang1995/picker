package com.grasswort.picker.oss.manager.aliyunoss.util;

import com.grasswort.picker.oss.manager.aliyunoss.constant.OssStipulation;
import com.grasswort.picker.oss.manager.aliyunoss.dto.OssRefDTO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname OssUtils
 * @Description OSS地址解析
 * @Date 2019/10/17 21:44
 * @blame Java Team
 */
public class OssUtils {
    /**
     *  预编译正则表达式
     */
    private static final Pattern OSS_PATTERN = Pattern.compile(OssStipulation.OSS_URL_REGEX);
    /**
     *
     * <p>Title: generateOssKeyName</p>
     * <p>Description: 生成OSS的objectName（不包含后缀以及“.”）</p>
     * @return
     * String
     */
    public static String generateOssKeyName() {
        String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        StringBuffer sbf = new StringBuffer();
        sbf.append(System.currentTimeMillis());
        sbf.append(Thread.currentThread().getName());
        sbf.append(RandomStringUtils.randomAlphabetic(10));
        return dir.concat("/").concat(DigestUtils.md5Hex(sbf.toString().getBytes()));
    }
    /**
     *
     * <p>Title: replenishOssUrl</p>
     * <p>Description: 补全OSSurl</p>
     * @return
     * String
     */
    public static String replenishOssUrl(String bucketName, String objectName, String disposeStyle) {
        return String.format(OssStipulation.OSS_URL_TEMPLATE, bucketName, objectName, disposeStyle);
    }

    /**
     *
     * <p>Title: findOssUrlFromText</p>
     * <p>Description: 从文本中寻找OSSurl</p>
     * @param text
     * @return
     * List<String>
     */
    public static Set<OssRefDTO> findOssUrlFromText(String text) {
        if (StringUtils.isBlank(text)) {
            return Collections.EMPTY_SET;
        }
        Matcher matcher = OSS_PATTERN.matcher(text);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list.stream().map(url -> resolverUrl(url)).collect(Collectors.toSet());
    }
    /**
     *
     * <p>Title: resolverUrl</p>
     * <p>Description: 解析OSS Url,私有方法保证url的格式</p>
     * @param url
     * @return
     * OssRefDTO
     */
    public static OssRefDTO resolverUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        String text = url.replace(OssStipulation.HOST_SUFFIX.concat("/"), "@").replace("_", "@");
        String[] strArray = text.split("@");
        String bucketName = strArray[0].replace("https://", "");
        String objectName = Arrays.stream(strArray).filter(s -> OssStipulation.OSS_KEY_NAME_LENGTH == s.indexOf("."))
                .findFirst().orElse("");
        OssRefDTO ref = new OssRefDTO();
        ref.setBucketName(bucketName);
        ref.setObjectKey(objectName);
        ref.setUrl(url);
        return ref;
    }

}
