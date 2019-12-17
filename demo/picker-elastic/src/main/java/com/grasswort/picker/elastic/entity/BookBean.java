package com.grasswort.picker.elastic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

/**
 * @author xuliangliang
 * @Classname BookBean
 * @Description
 * @String 2019/11/30 9:51
 * @blame Java Team
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "book", type = "doc", shards = 1, replicas = 1)
public class BookBean {
    @Id
    private Long id;
    @Field(type = FieldType.Text)
    private String title;
    @Parent(type = "author")
    private String authorId;
    @Field(type = FieldType.Text)
    private String postString;

}
