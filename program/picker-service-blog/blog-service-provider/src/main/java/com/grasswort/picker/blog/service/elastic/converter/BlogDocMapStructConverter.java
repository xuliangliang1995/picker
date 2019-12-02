package com.grasswort.picker.blog.service.elastic.converter;

import com.grasswort.picker.blog.dto.blog.BlogItemWithAuthor;
import com.grasswort.picker.blog.elastic.entity.BlogDoc;
import org.mapstruct.Mapper;

/**
 * @author xuliangliang
 * @Classname BlogDocMapStructConverter
 * @Description 转换
 * @Date 2019/12/2 15:53
 * @blame Java Team
 */
@Mapper(componentModel = "spring")
public interface BlogDocMapStructConverter {

    BlogItemWithAuthor doc2BlogItemWithAuthor(BlogDoc doc);
}
