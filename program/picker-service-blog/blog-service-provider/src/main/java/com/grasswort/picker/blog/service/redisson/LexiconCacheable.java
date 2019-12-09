package com.grasswort.picker.blog.service.redisson;

import org.redisson.api.RBucket;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author xuliangliang
 * @Classname LexiconCacheable
 * @Description 词库
 * @Date 2019/12/3 11:54
 * @blame Java Team
 */
@Component
public class LexiconCacheable {
    @Resource
    private RedissonClient redissonClient;
    private final String LEXICON_BUCKET = "pk_lexicon:bucket";
    private final String LEXICON_MODIFIED_TIME = "pk_lexicon:modified";

    /**
     * 更新词库
     * @param words
     */
    public void cacheLexicon(Set<String> words) {
        RSet<String> lexicon = redissonClient.getSet(LEXICON_BUCKET);
        lexicon.addAll(words);
        RBucket<Long> modifiedTime = redissonClient.getBucket(LEXICON_MODIFIED_TIME);
        modifiedTime.set(System.currentTimeMillis());
    }

    /**
     * 添加新分词
     * @param word
     */
    public void addWord(String word) {
        RSet<String> lexicon = redissonClient.getSet(LEXICON_BUCKET);
        lexicon.addAsync(word);
    }

    /**
     * 获取词库
     * @return
     */
    public Set<String> lexicon() {
        RSet<String> lexicon = redissonClient.getSet(LEXICON_BUCKET);
        return lexicon.readAll();
    }

    /**
     * 获取上次更新时间
     * @return
     */
    public Long lastModifiedTime() {
        RBucket<Long> modifiedTime = redissonClient.getBucket(LEXICON_MODIFIED_TIME);
        return modifiedTime.get();
    }


}
