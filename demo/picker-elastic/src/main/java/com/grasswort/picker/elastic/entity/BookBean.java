package com.grasswort.picker.elastic.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author xuliangliang
 * @Classname BookBean
 * @Description TODO
 * @String 2019/11/30 9:51
 * @blame Java Team
 */
@Document(indexName = "book", type = "_doc")
public class BookBean {
    @Id
    private String id;
    private String title;
    private String author;
    private String postString;

    public BookBean() {
    }

    public BookBean(String id, String title, String author, String postString) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.postString = postString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPostString() {
        return postString;
    }

    public void setPostString(String postString) {
        this.postString = postString;
    }

    @Override
    public String toString() {
        return "BookBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", postString='" + postString + '\'' +
                '}';
    }
}
