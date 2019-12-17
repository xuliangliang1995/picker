package com.grasswort.picker.elastic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author xuliangliang
 * @Classname AuthorBean
 * @Description 作者
 * @Date 2019/12/16 17:17
 * @blame Java Team
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "book", type = "author", shards = 1, replicas = 1)
public class AuthorBean {
    @Id
    private Long authorId;
    @Field(type = FieldType.Text)
    private String name;
}
