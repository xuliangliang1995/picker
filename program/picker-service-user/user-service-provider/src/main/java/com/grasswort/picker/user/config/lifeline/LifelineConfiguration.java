package com.grasswort.picker.user.config.lifeline;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * @author xuliangliang
 * @Classname LifelineConfiguration
 * @Description 有效期配置
 * @Date 2019/10/10 16:30
 * @blame Java Team
 */
@Component
@Getter
@NacosPropertySource(dataId = "lifeline", autoRefreshed = true, type = ConfigType.YAML)
public class LifelineConfiguration {
    /**
     * 验证码有效期
     */
    @NacosValue(value = "${lifeline.captcha.minutes:10}")
    private Integer captchaLifeMinutes;
    /**
     * 账户激活码有效期
     */
    @NacosValue(value = "${lifeline.activation-code.minutes:30}")
    private Integer activationCodeLifeMinutes;
    /**
     * access_token 提权有效期（当验证用户身份后，提升用户权限时长）
     */
    @NacosValue(value = "${lifeline.token-privilege.minutes:10}")
    private Integer tokenPrivilegeLifeMinutes;
    /**
     * access_token 有效期
     */
    @NacosValue(value = "${lifeline.access-token.hours:24}")
    private Integer accessTokenLifeHours;
    /**
     * refresh_token 有效期
     */
    @NacosValue(value = "${lifeline.refresh-token.hours:168}")
    private Integer refreshTokenLifeHours;

    /**
     * lifeline:
     *     captcha:
     *         minutes: 10
     *     activation-code:
     *         minutes: 30
     *     token-privilege:
     *         minutes: 10
     *     access-token:
     *         hours: 24
     *     refresh-token:
     *         hours: 168
     */
}
