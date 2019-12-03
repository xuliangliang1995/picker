package com.grasswort.picker.blog.elastic.entity;

import com.grasswort.picker.blog.elastic.constants.EsAnalyzer;
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

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getPickerId() {
        return pickerId;
    }

    public void setPickerId(String pickerId) {
        this.pickerId = pickerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public static final class Builder {
        private Long blogId;
        private Integer status;
        private String pickerId;
        private String title;
        private String summary;
        private String coverImg;
        private List<String> labels;
        private Integer version;
        private String author;
        private String authorAvatar;
        private String markdown;
        private Date gmtCreate;
        private Date gmtModified;

        private Builder() {
        }

        public static Builder aBlogDoc() {
            return new Builder();
        }

        public Builder withBlogId(Long blogId) {
            this.blogId = blogId;
            return this;
        }

        public Builder withStatus(Integer status) {
            this.status = status;
            return this;
        }

        public Builder withPickerId(String pickerId) {
            this.pickerId = pickerId;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder withCoverImg(String coverImg) {
            this.coverImg = coverImg;
            return this;
        }

        public Builder withLabels(List<String> labels) {
            this.labels = labels;
            return this;
        }

        public Builder withVersion(Integer version) {
            this.version = version;
            return this;
        }

        public Builder withAuthor(String author) {
            this.author = author;
            return this;
        }

        public Builder withAuthorAvatar(String authorAvatar) {
            this.authorAvatar = authorAvatar;
            return this;
        }

        public Builder withMarkdown(String markdown) {
            this.markdown = markdown;
            return this;
        }

        public Builder withGmtCreate(Date gmtCreate) {
            this.gmtCreate = gmtCreate;
            return this;
        }

        public Builder withGmtModified(Date gmtModified) {
            this.gmtModified = gmtModified;
            return this;
        }

        public BlogDoc build() {
            BlogDoc blogDoc = new BlogDoc();
            blogDoc.setBlogId(blogId);
            blogDoc.setStatus(status);
            blogDoc.setPickerId(pickerId);
            blogDoc.setTitle(title);
            blogDoc.setSummary(summary);
            blogDoc.setCoverImg(coverImg);
            blogDoc.setLabels(labels);
            blogDoc.setVersion(version);
            blogDoc.setAuthor(author);
            blogDoc.setAuthorAvatar(authorAvatar);
            blogDoc.setMarkdown(markdown);
            blogDoc.setGmtCreate(gmtCreate);
            blogDoc.setGmtModified(gmtModified);
            return blogDoc;
        }
    }
}
