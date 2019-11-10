package com.grasswort.picker.quartz.service;

import org.springframework.stereotype.Service;

/**
 * @author xuliangliang
 * @Classname TestService
 * @Description TestService
 * @Date 2019/11/9 17:55
 * @blame Java Team
 */
@Service
public class TestService {

    public String doSomeThing() {
        System.out.println("do MyJob ..." + System.currentTimeMillis());
        return "success";
    }
}
