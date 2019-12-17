package com.grasswort.picker.blog.elastic.entity;

import com.grasswort.picker.blog.dto.topic.MenuLink;
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
 * @Classname TopicDoc
 * @Description 专题
 * @Date 2019/12/14 16:38
 * @blame Java Team
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "pk_topic", type = "_doc", shards = 3, replicas = 2)
public class TopicDoc {
    /**
     * 专题 ID
     */
    @Id
    private Long topicId;
    /**
     * 作者ID
     */
    @Field(type = FieldType.Text)
    private String pickerId;
    /**
     * 所属者
     */
    @Field(type = FieldType.Text)
    private String ownerName;
    /**
     * 所属者头像
     */
    @Field(type = FieldType.Text, index = false)
    private String ownerAvatar;
    /**
     * 标题
     */
    @Field(type = FieldType.Text, analyzer = EsAnalyzer.IK)
    private String title;
    /**
     * 封面配图
     */
    @Field(type = FieldType.Text, index = false)
    private String coverImg;
    /**
     * 摘要
     */
    @Field(type = FieldType.Text, analyzer = EsAnalyzer.IK)
    private String summary;
    /**
     * 博客状态 0、私密 1、公开
     */
    @Field(type = FieldType.Integer)
    private Integer status;
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
     * 菜单
     */
    @Field(type = FieldType.Object)
    private List<MenuLink> links;

}
