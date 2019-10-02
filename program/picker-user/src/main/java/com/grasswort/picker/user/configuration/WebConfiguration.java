package com.grasswort.picker.user.configuration;

import com.grasswort.picker.user.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xuliangliang
 * @Classname WebConfiguration
 * @Description 配置
 * @Date 2019/10/2 12:55
 * @blame Java Team
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Bean
    public TokenInterceptor tokenInterceptor() {
        return new TokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor())
                .addPathPatterns("/**");
    }
}
