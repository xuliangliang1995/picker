package com.grasswort.picker.blog.elastic.entity;

import com.grasswort.picker.blog.elastic.constants.EsAnalyzer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

/**
 * @author xuliangliang
 * @Classname BlogDoc
 * @Description elastic 存储博客内容
 * @Date 2019/11/30 15:19
 * @blame Java Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "pk_blog", type = "_doc", shards = 3, replicas = 2)
public class BlogDoc {
    /**
     * 博客 ID
     */
    @Id
    private Long blogId;
    /**
     * 博客状态 0、正常 1、回收中 2、删除
     */
    @Field(type = FieldType.Integer)
    private Integer status;
    /**
     * 作者ID
     */
    @Field(type = FieldType.Text, index = false)
    private String pickerId;
    /**
     * 标题
     */
    @Field(type = FieldType.Text, analyzer = EsAnalyzer.IK)
    private String title;
    /**
     * 摘要
     */
    @Field(type = FieldType.Text, analyzer = EsAnalyzer.IK)
    private String summary;
    /**
     * 封面配图
     */
    @Field(type = FieldType.Text, index = false)
    private String coverImg;
    /**
     * 标签
     */
    @Field(type = FieldType.Text, analyzer = EsAnalyzer.IK)
    private List<String> labels;
    /**
     * 版本
     */
    @Field(type = FieldType.Integer, index = false)
    private Integer version;

    /**
     * 作者 id
     */
    @Field(type = FieldType.Long)
    private Long authorId;

    /**
     * 作者
     */
    @Field(type = FieldType.Text)
    private String author;

    /**
     * 作者头像
     */
    @Field(type = FieldType.Text, index = false)
    private String authorAvatar;

    /**
     * markdown
     */
    @Field(type = FieldType.Text, analyzer = EsAnalyzer.IK)
    private String markdown;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date)
    private Date gmtCreate;

    /**
     * 最后一次更新时间
     */
    @Field(type = FieldType.Date)
    private Date gmtModified;
    /**
     * 热度
     */
    @Field(type = FieldType.Double)
    private Double heat;
    /**
     * 喜欢
     */
    @Field(type = FieldType.Long)
    private Long like;
    /**
     * 收藏
     */
    @Field(type = FieldType.Long)
    private Long favorite;
    /**
     * 浏览量
     */
    @Field(type = FieldType.Long)
    private Long browse;

}
