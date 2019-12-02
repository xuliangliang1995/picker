package com.grasswort.picker.blog.service.elastic;

import com.alibaba.fastjson.JSON;
import com.grasswort.picker.blog.elastic.entity.BlogDoc;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xuliangliang
 * @Classname BlogDocHighLightMapper
 * @Description 博客高亮映射
 * @Date 2019/12/2 15:37
 * @blame Java Team
 */
@Component
public class BlogDocHighLightMapper implements SearchResultMapper {
    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
        List<T> chunk = new ArrayList<>();

        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            Map<String, Object> smap = searchHit.getSourceAsMap();
            Map<String, HighlightField> hmap = searchHit.getHighlightFields();
            chunk.add((T) createEsDoc(smap,hmap));
        }

        AggregatedPage<T> result = new AggregatedPageImpl<T>(chunk,pageable, searchResponse.getHits().getTotalHits());
        return result;
    }

    private Object createEsDoc(Map<String, Object> smap, Map<String, HighlightField> hmap) {
        for (String key : smap.keySet()) {
            if (hmap.containsKey(key)) {
                smap.put(key, hmap.get(key).fragments()[0].toString());
            }
        }
        String json = JSON.toJSONString(smap);

        return JSON.toJavaObject(JSON.parseObject(json), BlogDoc.class);
    }
}
