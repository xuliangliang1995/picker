package com.grasswort.picker.oss;

import com.grasswort.picker.oss.dto.*;

/**
 * @author xuliangliang
 * @Classname IOssRefService
 * @Description Oss 引用服务（引用了 Oss 的需要在服务器创建引用，不然文件会被清理）
 * @Date 2019/10/17 21:17
 * @blame Java Team
 */
public interface IOssRefService {
    /**
     * 存储 key
     * @param saveRequest
     * @return
     */
    OssKeySaveResponse saveKey(OssKeySaveRequest saveRequest);

    /**
     * 提交引用
     * @param refRequest
     * @return
     */
    OssRefResponse recordRef(OssRefRequest refRequest);

    /**
     * 删除引用
     * @param delRequest
     * @return
     */
    OssRefDelResponse delRef(OssRefDelRequest delRequest);

}
