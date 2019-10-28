package com.grasswort.picker.oss.manager.aliyunoss.file;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.grasswort.picker.oss.manager.aliyunoss.Oss;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * @author xuliangliang
 * @Classname FileUpload
 * @Description 阿里云OSS文件上传
 * @Date 2019/10/17 21:43
 * @blame Java Team
 */
public class FileUpload {
    private FileUpload() {};

    /**
     *
     * <p>Title: instance</p>
     * <p>Description: 获取实例（单例）</p>
     * @return
     * FileUpload
     */
    public static FileUpload instance() {
        return LazyHolder.sington;
    }

    /**
     *
     * <p>Title: upload</p>
     * <p>Description: 简单上传（上传字符串）</p>
     * @param text
     * void
     */
    public void upload(String bucketName, String objectName, String text) {
        Oss.client().putObject(bucketName, objectName, new ByteArrayInputStream(text.getBytes()));
    }

    /**
     *
     * <p>Title: upload</p>
     * <p>Description: 简单上传（上传byte数组）</p>
     * @param bucketName
     * @param objectName
     * @param byteArray
     * void
     */
    public void upload(String bucketName, String objectName, byte[] byteArray) {
        Oss.client().putObject(bucketName, objectName, new ByteArrayInputStream(byteArray));
    }

    /**
     *
     * <p>Title: upload</p>
     * <p>Description: 简单上传（上传网络流）</p>
     * @param bucketName
     * @param objectName
     * @param url
     * @throws OSSException
     * @throws ClientException
     * @throws IOException
     * void
     */
    public void upload(String bucketName, String objectName, URL url) throws OSSException, ClientException, IOException {
        Oss.client().putObject(bucketName, objectName, url.openStream());
    }

    /**
     *
     * <p>Title: upload</p>
     * <p>Description: 简单上传（上传文件流）</p>
     * @param bucketName
     * @param objectName
     * @param fis
     * void
     */
    public void upload(String bucketName, String objectName, FileInputStream fis) {
        Oss.client().putObject(bucketName, objectName, fis);
    }

    /**
     *
     * <p>Title: upload</p>
     * <p>Description: 上传本地文件</p>
     * @param bucketName
     * @param objectName
     * @param file
     * void
     */
    public void upload(String bucketName, String objectName, File file) {
        Oss.client().putObject(bucketName, objectName, file);
    }

    /**
     *
     * <p>Title: FileUpload.java<／p>
     * <p>Description: 懒加载单例<／p>
     * <p>Copyright: Copyright (c) 2019<／p>
     * <p>Company: grasswort<／p>
     *
     * @author 树林里面丢了鞋
     * @date 2019年1月4日
     * @version 1.0
     */
    private static class LazyHolder {
        static final FileUpload sington = new FileUpload();
    }
}
