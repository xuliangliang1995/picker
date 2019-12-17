package com.grasswort.picker.blog.dto.topic;

import com.grasswort.picker.blog.constant.EsAnalyzer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author xuliangliang
 * @Classname MenuLink
 * @Description 菜单链接
 * @Date 2019/12/17 11:34
 * @blame Java Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuLink {
    @Field(type = FieldType.Text, analyzer = EsAnalyzer.IK)
    private String title;
    @Field(type = FieldType.Text, index = false)
    private String blogId;

}
