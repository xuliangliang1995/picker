package com.grasswort.picker.commons.tkmapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author xuliangliang
 * @Classname TkMapper
 * @Description Mybatis 生成 Mapper 接口父接口
 * @Date 2019/9/29 15:09
 * @blame Java Team
 */
public interface TkMapper<T> extends Mapper<T>, MySqlMapper<T> {}
