package com.grasswort.picker.redisson.score;

import com.grasswort.picker.redisson.client.SRedissonClient;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import sun.swing.StringUIClientPropertyKey;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author xuliangliang
 * @Classname ScoreSortTest
 * @Description TODO
 * @Date 2019/11/29 15:06
 * @blame Java Team
 */
public class HotWordTest {

    private static final String KEY = "search_hot_word";

    public static void main(String[] args) {
        /*staticsSearchHotWord("Java");
        staticsSearchHotWord("python");
        staticsSearchHotWord("Java");*/
        staticsSearchHotWord("php");
        System.out.println("当前热搜：");
        hotSearchWord().stream().forEach(word -> System.out.println(word));
    }

    private static void staticsSearchHotWord(String keyword) {
        if (keyword != null && keyword.length() > 0) {
            RedissonClient client = SRedissonClient.client;
            RScoredSortedSet<String> sortedSet = client.getScoredSortedSet(KEY);
            Double score = sortedSet.getScore(keyword);
            if (score != null) {
                sortedSet.addAndGetRank(score + 1.0, keyword);
            } else {
                sortedSet.addScore(keyword, 1.0);
            }
        }
    }

    private static Collection<String> hotSearchWord() {
        RedissonClient client = SRedissonClient.client;
        RScoredSortedSet<String> sortedSet = client.getScoredSortedSet(KEY);
        return sortedSet.valueRangeReversed(0, 10);
    }
}
