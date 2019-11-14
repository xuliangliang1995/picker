package com.grasswort.picker.wechat.config;

import com.binarywang.spring.starter.wxjava.mp.config.WxMpAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author xuliangliang
 * @Classname WechatConfiguration
 * @Description 微信配置
 * @Date 2019/11/14 15:27
 * @blame Java Team
 */
@Configuration
@Import(WxMpAutoConfiguration.class)
public class WechatConfiguration {

}
