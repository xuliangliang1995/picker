package com.grasswort.picker.blog.dto.curve;

import lombok.Data;

/**
 * @author xuliangliang
 * @Classname RetentionCurveItem
 * @Description 记忆曲线
 * @Date 2019/11/10 12:06
 * @blame Java Team
 */
@Data
public class RetentionCurveItem {

    private Long curveId;

    private Integer order;

    private Integer intervalDay;

    public static final class Builder {
        private Long curveId;
        private Integer order;
        private Integer intervalDay;

        private Builder() {
        }

        public static Builder aRetentionCurveItem() {
            return new Builder();
        }

        public Builder withCurveId(Long curveId) {
            this.curveId = curveId;
            return this;
        }

        public Builder withOrder(Integer order) {
            this.order = order;
            return this;
        }

        public Builder withIntervalDay(Integer intervalDay) {
            this.intervalDay = intervalDay;
            return this;
        }

        public RetentionCurveItem build() {
            RetentionCurveItem retentionCurveItem = new RetentionCurveItem();
            retentionCurveItem.setCurveId(curveId);
            retentionCurveItem.setOrder(order);
            retentionCurveItem.setIntervalDay(intervalDay);
            return retentionCurveItem;
        }
    }
}
