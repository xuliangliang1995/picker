package com.grasswort.picker.user.service;

import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.oss.IOssRefService;
import com.grasswort.picker.oss.constants.OssConstants;
import com.grasswort.picker.oss.dto.OssRefRequest;
import com.grasswort.picker.oss.dto.OssRefResponse;
import com.grasswort.picker.oss.manager.aliyunoss.dto.OssRefDTO;
import com.grasswort.picker.oss.manager.aliyunoss.util.OssUtils;
import com.grasswort.picker.user.IUserPrivateMpQrcodeService;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dao.entity.UserOssRef;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import com.grasswort.picker.user.dao.persistence.UserOssRefMapper;
import com.grasswort.picker.user.dto.PrivateMpQrcodeRequest;
import com.grasswort.picker.user.dto.PrivateMpQrcodeResponse;
import com.grasswort.picker.user.dto.UploadPrivateMpQrcodeRequest;
import com.grasswort.picker.user.dto.UploadPrivateMpQrcodeResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname UserPrivateMpQrcodeServiceImpl
 * @Description 用户个人公众号二维码服务
 * @Date 2019/11/22 21:10
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class UserPrivateMpQrcodeServiceImpl implements IUserPrivateMpQrcodeService {

    @Autowired UserMapper userMapper;

    @Autowired UserOssRefMapper userOssRefMapper;

    @Reference(version = "1.0", timeout = 10000) IOssRefService iOssRefService;
    /**
     * 上传个人微信公众号二维码
     *
     * @param uploadPrivateMpQrcodeRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public UploadPrivateMpQrcodeResponse uploadPrivateMpQrcode(UploadPrivateMpQrcodeRequest uploadPrivateMpQrcodeRequest) {
        UploadPrivateMpQrcodeResponse uploadResponse = new UploadPrivateMpQrcodeResponse();

        Long userId = uploadPrivateMpQrcodeRequest.getUserId();
        String qrcode = uploadPrivateMpQrcodeRequest.getPrivateMpQrcode();

        boolean userNotExists = ! userMapper.existsWithPrimaryKey(userId);
        if (userNotExists) {
            uploadResponse.setCode(SysRetCodeConstants.USER_NOT_EXISTS.getCode());
            uploadResponse.setMsg(SysRetCodeConstants.USER_NOT_EXISTS.getMsg());
            return uploadResponse;
        }

        this.processQrcodeRef(qrcode);

        User userSelective = new User();
        userSelective.setId(userId);
        userSelective.setPrivateMpQrcode(qrcode);
        userSelective.setGmtModified(new Date(System.currentTimeMillis()));
        userMapper.updateByPrimaryKeySelective(userSelective);
        uploadResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        uploadResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return uploadResponse;
    }

    /**
     * 获取个人公众号二维码
     *
     * @param privateMpQrcodeRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public PrivateMpQrcodeResponse getPrivateMpQrcode(PrivateMpQrcodeRequest privateMpQrcodeRequest) {
        PrivateMpQrcodeResponse qrcodeResponse = new PrivateMpQrcodeResponse();

        Long userId = privateMpQrcodeRequest.getUserId();
        String qrcode = userMapper.getPrivateMpQrcode(userId);

        qrcodeResponse.setQrcode(qrcode);
        qrcodeResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        qrcodeResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return qrcodeResponse;
    }

    /**
     * 处理二维码 oss 引用
     * @param qrcode
     */
    private void processQrcodeRef(String qrcode) {
        OssRefDTO ref = OssUtils.resolverUrl(qrcode);
        if (ref != null) {
            // 1、判断是否建议远程应用
            Example example = new Example(UserOssRef.class);
            example.createCriteria().andEqualTo("ossKey", ref.getObjectKey())
                    .andEqualTo("ossBucket", ref.getBucketName());

            int count = userOssRefMapper.selectCountByExample(example);
            if (count == 0) {
                final String DEL_KEY = RandomStringUtils.randomAlphabetic(OssConstants.OSS_DEL_KEY_LENGTH);
                OssRefResponse refResponse = iOssRefService.recordRef(
                        OssRefRequest.Builder.anOssRefRequest()
                                .withObjectKey(ref.getObjectKey())
                                .withBucketName(ref.getBucketName())
                                .withDelKey(DEL_KEY)
                                .build()
                );

                final long OSS_REF_ID = Optional.ofNullable(refResponse)
                        .map(r -> r.isSuccess() ? r.getId() : 0L)
                        .orElse(0L);
                Date now = DateTime.now().toDate();
                UserOssRef remoteRef = new UserOssRef();
                remoteRef.setOssKey(ref.getObjectKey());
                remoteRef.setOssBucket(ref.getBucketName());
                remoteRef.setOssRefId(OSS_REF_ID);
                remoteRef.setOssRefDelKey(DEL_KEY);
                remoteRef.setGmtCreate(now);
                remoteRef.setGmtModified(now);
                userOssRefMapper.insert(remoteRef);
            }
        }
    }
}
