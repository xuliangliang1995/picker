package com.grasswort.picker.commons.aspect;

import com.grasswort.picker.commons.config.DBLocalHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * @author xuliangliang
 * @Classname MultiDBAspect
 * @Description TODO
 * @Date 2019/9/29 19:39
 * @blame Java Team
 */
@Aspect
@Component
@EnableAspectJAutoProxy
@Slf4j
public class MultiDBAspect {

    /**
     * 切 @Master
     */
    @Pointcut("@annotation(com.grasswort.picker.commons.annotation.Master)")
    public void masterPointCut() {}

    /**
     * master 增强
     * @param joinPoint
     * @return
     */
    @Around("masterPointCut()")
    public Object masterAround(ProceedingJoinPoint joinPoint) throws Throwable {
        DBLocalHolder.master();
        return joinPoint.proceed();
    }

    /**
     * 切 @Slave
     */
    @Pointcut("@annotation(com.grasswort.picker.commons.annotation.Slave)")
    public void slavePointCut() {};

    /**
     * slave 增强（slave 执行完清空数据库选择）
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("slavePointCut()")
    public Object slaveAround(ProceedingJoinPoint joinPoint) throws Throwable {
        DBLocalHolder.slave();
        try {
            return joinPoint.proceed();
        } finally {
            DBLocalHolder.clear();
        }
    }
}
