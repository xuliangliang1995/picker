package com.grasswort.picker.oss.controller;

import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.oss.IOssRefService;
import com.grasswort.picker.oss.constants.SysRetCodeConstants;
import com.grasswort.picker.oss.dto.OssKeySaveRequest;
import com.grasswort.picker.oss.dto.OssKeySaveResponse;
import com.grasswort.picker.oss.manager.aliyunoss.Oss;
import com.grasswort.picker.oss.manager.aliyunoss.constant.OssStipulation;
import com.grasswort.picker.oss.manager.aliyunoss.dto.OssRefDTO;
import com.grasswort.picker.oss.manager.aliyunoss.util.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author xuliangliang
 * @Classname OssController
 * @Description oss
 * @Date 2019/10/18 17:29
 * @blame Java Team
 */
@Slf4j
@RestController
public class OssController {

    @Reference(version = "1.0", timeout = 5000)
    IOssRefService iOssRefService;

    /**
     * 上传 oss
     * @return
     */
    @PostMapping("/upload")
    public ResponseData uploadOss(
            @RequestParam(value = "file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String objectName = OssUtils.generateOssKeyName().concat(suffix);
        try {
            String ossUrl = OssUtils.replenishOssUrl(OssStipulation.DEFAULT_BUCKET_NAME, objectName, OssStipulation.DefaultBucketDisposeStyle.COMPRESS);
            OssRefDTO ref = OssUtils.resolverUrl(ossUrl);
            OssKeySaveResponse saveResponse = iOssRefService.saveKey(
                    OssKeySaveRequest.Builder.anOssKeySaveRequest()
                    .withBucketName(ref.getBucketName())
                    .withObjectKey(ref.getObjectKey())
                    .build()
            );
            if (SysRetCodeConstants.SUCCESS.getCode().equals(saveResponse.getCode())) {
                Oss.FileHandler.UPLOAD.upload(OssStipulation.DEFAULT_BUCKET_NAME, objectName, file.getBytes());
                log.info("上传oss文件成功：{}", ossUrl);
                return new ResponseUtil<>().setData(ossUrl);
            }
            log.info("上传oss文件失败：{}", ossUrl);
            return new ResponseUtil<>().setErrorMsg(saveResponse.getMsg());
        } catch (IOException e) {
            e.printStackTrace();
            log.info("上传oss文件失败：{}", e.getMessage());
        }
        return new ResponseUtil<>().setErrorMsg("上传失败");
    }
}
