package com.grasswort.picker.blog.service.elastic;

import com.grasswort.picker.blog.constant.BlogStatusEnum;
import com.grasswort.picker.blog.elastic.entity.BlogDoc;
import com.grasswort.picker.blog.service.elastic.dto.SearchParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

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

    /*@Resource
    BlogMapper blogMapper;

    @Resource
    BlogDocRepository blogDocRepository;

    @Resource BlogDocConverter blogDocConverter;*/

    @Resource ElasticsearchTemplate elasticsearchTemplate;

    @Resource BlogDocHighLightMapper blogDocHighLightMapper;

    private final static HighlightBuilder HIGH_LIGHT_BUILDER = new HighlightBuilder().preTags("<strong style='color:red'>").postTags("</strong>");

    /**
     *
     * @param searchParams
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<BlogDoc> search(SearchParams searchParams, int pageNo, int pageSize) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        String keyword = searchParams.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            boolQueryBuilder.must(
                    QueryBuilders.boolQuery()
                            .should(QueryBuilders.fuzzyQuery("title", keyword))
                            .should(QueryBuilders.fuzzyQuery("labels", keyword))
                            .should(QueryBuilders.fuzzyQuery("summary", keyword))
            );
        }

        Long authorId = searchParams.getAuthorId();
        BoolQueryBuilder boolFilterBuilder = QueryBuilders.boolQuery();
        boolFilterBuilder.filter(QueryBuilders.termQuery("status", BlogStatusEnum.NORMAL.status()));
        if (authorId != null && authorId > 0L) {
            boolFilterBuilder.filter(QueryBuilders.termQuery("authorId", authorId));
        }

        Sort sort = StringUtils.isNotBlank(keyword)
                ? new Sort(Sort.Direction.DESC, "_score").and(new Sort(Sort.Direction.DESC, "gmtModified"))
                : new Sort(Sort.Direction.DESC, "gmtModified");

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        SearchQuery query = new NativeSearchQueryBuilder()
                .withFilter(boolFilterBuilder)
                .withQuery(boolQueryBuilder)
                .withPageable(pageable)
                .withHighlightBuilder(HIGH_LIGHT_BUILDER)
                .withHighlightFields(new HighlightBuilder.Field("title"), new HighlightBuilder.Field("summary"))
                .build();

        AggregatedPage<BlogDoc> page = elasticsearchTemplate.queryForPage(query, BlogDoc.class, blogDocHighLightMapper);
        return page;
    }



}
