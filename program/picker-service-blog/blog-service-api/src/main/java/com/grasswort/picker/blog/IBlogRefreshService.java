package com.grasswort.picker.blog;

/**
 * @author xuliangliang
 * @Classname IBlogRefreshService
 * @Description 刷新 ES 存储
 * @Date 2019/12/11 11:04
 * @blame Java Team
 */
public interface IBlogRefreshService {
    /**
     * 刷新某位作者相关博客 ES 存储
     * @param authorId
     */
    void refreshByAuthorId(Long authorId);

}
