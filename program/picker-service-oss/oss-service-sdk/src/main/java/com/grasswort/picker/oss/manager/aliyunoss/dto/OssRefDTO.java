package com.grasswort.picker.oss.manager.aliyunoss.dto;

import lombok.Data;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OssRefDTO ossRefDTO = (OssRefDTO) o;
        return Objects.equals(bucketName, ossRefDTO.bucketName) &&
                Objects.equals(objectKey, ossRefDTO.objectKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bucketName, objectKey);
    }
}
