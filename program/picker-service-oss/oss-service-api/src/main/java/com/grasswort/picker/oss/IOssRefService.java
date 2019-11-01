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
     * 存储 key（存储 oss 文件 对应的地址，48 小时内不提交引用的话，系统将认为该 oss 文件已经弃用。会删除 oss 文件）
     * @param saveRequest
     * @return
     */
    OssKeySaveResponse saveKey(OssKeySaveRequest saveRequest);

    /**
     * 提交引用 （创建对 oss 文件的引用）
     * @param refRequest
     * @return
     */
    OssRefResponse recordRef(OssRefRequest refRequest);

    /**
     * 删除引用 （删除对 oss 文件的引用。 当引用为 0 时，oss 文件将会被删除）
     * @param delRequest
     * @return
     */
    OssRefDelResponse delRef(OssRefDelRequest delRequest);

}
