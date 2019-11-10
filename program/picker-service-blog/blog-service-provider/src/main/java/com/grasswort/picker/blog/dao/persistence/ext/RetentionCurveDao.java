package com.grasswort.picker.blog.dao.persistence.ext;

import com.grasswort.picker.blog.dao.entity.RetentionCurve;
import com.grasswort.picker.commons.tkmapper.TkMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author xuliangliang
 * @Classname RetentionCurveDao
 * @Description 记忆曲线
 * @Date 2019/11/10 13:14
 * @blame Java Team
 */
public interface RetentionCurveDao extends TkMapper<RetentionCurve> {

    @Select("select interval_day from pk_retention_curve where `order` = #{order}")
    Integer selectIntervalDayByOrder(@Param("order") Integer order);
}
