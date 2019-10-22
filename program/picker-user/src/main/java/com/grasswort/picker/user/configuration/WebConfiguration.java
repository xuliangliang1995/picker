package com.grasswort.picker.user.configuration;

import com.grasswort.picker.user.interceptor.TokenInterceptor;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * @author xuliangliang
 * @Classname WebConfiguration
 * @Description 配置
 * @Date 2019/10/2 12:55
 * @blame Java Team
 */
@Configuration
@EnableSwagger2Doc
@EnableSwagger2
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

    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class )
                .configure()
                .failFast(true)
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        return validator;
    }
}
