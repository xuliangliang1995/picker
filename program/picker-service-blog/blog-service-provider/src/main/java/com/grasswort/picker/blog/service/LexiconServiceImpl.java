package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.ILexiconService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.persistence.ext.BlogLabelDao;
import com.grasswort.picker.blog.dto.LexiconResponse;
import com.grasswort.picker.blog.service.redisson.LexiconCacheable;
import com.grasswort.picker.commons.annotation.DB;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author xuliangliang
 * @Classname LexiconServiceImpl
 * @Description 词库
 * @Date 2019/12/3 11:48
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class LexiconServiceImpl implements ILexiconService {

    @Resource BlogLabelDao blogLabelDao;

    @Resource LexiconCacheable lexiconCacheable;

    /**
     * 获取词库（所有标签）
     *
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public LexiconResponse lexicon() {
        LexiconResponse lexiconResponse = new LexiconResponse();

        Long lastModifiedTime = lexiconCacheable.lastModifiedTime();
        if (lastModifiedTime == null) {
            Set<String> labels = blogLabelDao.getAllLabels();
            lexiconCacheable.cacheLexicon(labels);
            lastModifiedTime = lexiconCacheable.lastModifiedTime();
        }
        Set<String> lexicons = lexiconCacheable.lexicon();

        lexiconResponse.setLexicon(lexicons);
        lexiconResponse.setLastModified(lastModifiedTime);
        lexiconResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        lexiconResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        return lexiconResponse;
    }
}
