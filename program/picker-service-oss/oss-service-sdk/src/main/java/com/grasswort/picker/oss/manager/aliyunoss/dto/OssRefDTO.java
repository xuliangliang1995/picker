package com.grasswort.picker.oss.manager.aliyunoss.dto;

import lombok.Data;

/**
 * @author xuliangliang
 * @Classname OssRefDTO
 * @Description DTO
 * @Date 2019/10/17 21:42
 * @blame Java Team
 */
@Data
public class OssRefDTO {
    /**
     * 存储空间名称
     */
    private String bucketName;
    /**
     * 对象名称
     */
    private String objectKey;
    /**
     * url
     */
    private String url;
}
