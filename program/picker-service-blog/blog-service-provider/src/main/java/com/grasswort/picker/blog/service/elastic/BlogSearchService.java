package com.grasswort.picker.blog.service.elastic;

import com.grasswort.picker.blog.constant.BlogStatusEnum;
import com.grasswort.picker.blog.elastic.entity.BlogDoc;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author xuliangliang
 * @Classname BlogSearchService
 * @Description 博客查询
 * @Date 2019/11/30 16:12
 * @blame Java Team
 */
@Slf4j
@Service
public class BlogSearchService {

    @Resource ElasticsearchTemplate elasticsearchTemplate;

    @Resource BlogDocHighLightMapper blogDocHighLightMapper;

    private final static HighlightBuilder HIGH_LIGHT_BUILDER = new HighlightBuilder().preTags("<strong style='color:red'>").postTags("</strong>");


    @PostConstruct
    public void test() {
        Page<BlogDoc> items = search("elastic", 0, 10);
        log.info("查询结果：{}", items.getTotalElements());
        items.getContent().forEach(blogDoc -> {
            log.info(blogDoc.getTitle());
        });
    }

    /**
     *
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<BlogDoc> search(String keyword, int pageNo, int pageSize) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(
                QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchQuery("title", keyword).boost(3.0f))
                        .should(QueryBuilders.prefixQuery("title", keyword).boost(2.0f))
                        .should(QueryBuilders.matchQuery("labels", keyword).boost(2.0f))
                        .should(QueryBuilders.prefixQuery("labels", keyword).boost(1.0f))
                        .should(QueryBuilders.matchQuery("summary", keyword).boost(1.0f))
                        .should(QueryBuilders.prefixQuery("summary", keyword).boost(1.0f))
        );

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        SearchQuery query = new NativeSearchQueryBuilder()
                .withFilter(QueryBuilders.termQuery("status", BlogStatusEnum.NORMAL.status()))
                .withQuery(boolQueryBuilder)
                .withPageable(pageable)
                .withHighlightBuilder(HIGH_LIGHT_BUILDER)
                .withHighlightFields(new HighlightBuilder.Field("title"), new HighlightBuilder.Field("summary"))
                .build();

        AggregatedPage<BlogDoc> page = elasticsearchTemplate.queryForPage(query, BlogDoc.class, blogDocHighLightMapper);
        return page;
    }



}
