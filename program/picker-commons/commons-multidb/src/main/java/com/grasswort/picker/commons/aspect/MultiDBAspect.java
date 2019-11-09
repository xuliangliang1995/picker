package com.grasswort.picker.commons.aspect;

import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.config.DBLocalHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author xuliangliang
 * @Classname MultiDBAspect
 * @Description 多数据源切换切面
 * @Date 2019/9/29 19:39
 * @blame Java Team
 */
@Aspect
@Component
@EnableAspectJAutoProxy
@Slf4j
public class MultiDBAspect {

    /**
     * 数据库更换切点
     */
    @Pointcut("@annotation(com.grasswort.picker.commons.annotation.DB)")
    public void dbPointCut() {}

    /**
     * Around
     * @param joinPoint
     * @return
     */
    @Around("dbPointCut()")
    public Object masterAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method targetMethod = methodSignature.getMethod();
        Method realMethod = joinPoint.getTarget().getClass().getMethod(signature.getName(), targetMethod.getParameterTypes());
        DB db = realMethod.getAnnotation(DB.class);
        try {
            DBLocalHolder.selectDBGroup(db.value());
            return joinPoint.proceed();
        } finally {
            DBLocalHolder.clear();
        }
    }

}
