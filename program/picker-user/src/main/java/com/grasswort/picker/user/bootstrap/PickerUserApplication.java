package com.grasswort.picker.user.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author xuliangliang
 * @Classname PickerUserApplication
 * @Description TODO
 * @Date 2019/9/22 10:24
 * @blame Java Team
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.grasswort.picker.user"})
public class PickerUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(PickerUserApplication.class, args);
    }
}
