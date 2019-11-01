package com.grasswort.picker.oss.service;

import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.config.DBLocalHolder;
import com.grasswort.picker.oss.IOssRefService;
import com.grasswort.picker.oss.constants.DBGroup;
import com.grasswort.picker.oss.constants.SysRetCodeConstants;
import com.grasswort.picker.oss.dao.entity.OssKey;
import com.grasswort.picker.oss.dao.entity.OssKeyRef;
import com.grasswort.picker.oss.dao.persistence.OssKeyMapper;
import com.grasswort.picker.oss.dao.persistence.OssKeyRefMapper;
import com.grasswort.picker.oss.dto.*;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.Objects;

/**
 * @author xuliangliang
 * @Classname OssRefServiceImpl
 * @Description Oss 记录引用服务
 * @Date 2019/10/17 22:27
 * @blame Java Team
 */
@Service(version = "1.0")
public class OssRefServiceImpl implements IOssRefService {

    @Autowired OssKeyMapper ossKeyMapper;

    @Autowired OssKeyRefMapper ossKeyRefMapper;

    /**
     * 存储 key
     *
     * @param saveRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public OssKeySaveResponse saveKey(OssKeySaveRequest saveRequest) {
        OssKeySaveResponse saveResponse = new OssKeySaveResponse();

        Example example = new Example(OssKey.class);
        example.createCriteria()
                .andEqualTo("ossKey", saveRequest.getObjectKey())
                .andEqualTo("ossBucket", saveRequest.getBucketName());

        int count = ossKeyMapper.selectCountByExample(example);
        if (count == 0) {
            OssKey ossKey = new OssKey();
            ossKey.setOssBucket(saveRequest.getBucketName());
            ossKey.setOssKey(saveRequest.getObjectKey());
            Date now = new Date(System.currentTimeMillis());
            ossKey.setGmtCreate(now);
            ossKey.setGmtModified(now);

            DBLocalHolder.selectDBGroup(DBGroup.MASTER);
            ossKeyMapper.insertUseGeneratedKeys(ossKey);
        }

        saveResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        saveResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());

        return saveResponse;
    }

    /**
     * 提交引用
     *
     * @param refRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public OssRefResponse recordRef(OssRefRequest refRequest) {
        OssRefResponse refResponse = new OssRefResponse();

        Example example = new Example(OssKey.class);
        example.createCriteria()
                .andEqualTo("ossKey", refRequest.getObjectKey())
                .andEqualTo("ossBucket", refRequest.getBucketName());

        this.saveKey(
                OssKeySaveRequest.Builder.anOssKeySaveRequest()
                        .withObjectKey(refRequest.getObjectKey())
                        .withBucketName(refRequest.getBucketName())
                        .build()
        );

        OssKey ossKey = ossKeyMapper.selectOneByExample(example);
        if (null != ossKey) {
            OssKeyRef ref = new OssKeyRef();
            ref.setOssKeyId(ossKey.getId());
            ref.setDelKey(refRequest.getDelKey());
            Date now = new Date(System.currentTimeMillis());
            ref.setGmtCreate(now);
            ref.setGmtModified(now);
            DBLocalHolder.selectDBGroup(DBGroup.MASTER);
            ossKeyRefMapper.insertUseGeneratedKeys(ref);
            refResponse.setId(ref.getId());
        }

        refResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        refResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        return refResponse;
    }

    /**
     * 删除引用
     *
     * @param delRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public OssRefDelResponse delRef(OssRefDelRequest delRequest) {
        OssRefDelResponse delResponse = new OssRefDelResponse();

        Long refId = delRequest.getRefId();
        OssKeyRef ref = ossKeyRefMapper.selectByPrimaryKey(refId);

        boolean canDel = null != ref && Objects.equals(ref.getDelKey(), delRequest.getDelKey());
        if (canDel) {
            DBLocalHolder.selectDBGroup(DBGroup.MASTER);
            ossKeyRefMapper.deleteByPrimaryKey(refId);
        }

        delResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        delResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        return delResponse;
    }
}
