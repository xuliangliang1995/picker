package com.grasswort.picker.blog.service.hotword;

import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * @author xuliangliang
 * @Classname SearchHotWordService
 * @Description 搜索热词
 * @Date 2019/12/2 16:11
 * @blame Java Team
 */
@Service
public class SearchHotWordService {

    @Resource
    RedissonClient redissonClient;

    private static final String KEY = "search_hot_word";

    /**
     * 统计搜索词
     * @param keyword
     */
    public void staticsSearchHotWord(String keyword) {
        if (keyword != null && keyword.length() > 0) {
            RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(KEY);
            Double score = sortedSet.getScore(keyword);
            if (score != null) {
                sortedSet.addAndGetRank(score + 1.0, keyword);
            } else {
                sortedSet.addScore(keyword, 1.0);
            }
        }
    }

    /**
     * 获取热词
     * @param size
     * @return
     */
    public Collection<String> hotSearchWord(int size) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(KEY);
        return sortedSet.valueRangeReversed(0, size);
    }
}
