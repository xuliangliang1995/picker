package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.LexiconResponse;

/**
 * @author xuliangliang
 * @Classname IBlogAnalyzerWordService
 * @Description 词库
 * @Date 2019/12/3 11:38
 * @blame Java Team
 */
public interface ILexiconService {
    /**
     * 获取词库（所有标签和热搜前十（热搜前十暂不加））
     * @return
     */
    LexiconResponse lexicon();
}
