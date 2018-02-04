package com.jxp.spring07.aop.xml.advise;

import jdk.management.resource.internal.inst.SocketOutputStreamRMHooks;
import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class Transaction {
    /**
     * 前置通知:
     *      在目标方法之前执行
     *      参数:joinPoint连接点:连接点=客户端调用那个方法,哪个方法就是连接点
     */
    public void beginTransaction(JoinPoint joinpoint){
        System.out.println(joinpoint);
        // 连接点的名称:
        String name = joinpoint.getSignature().getName();
        System.out.println("连接点的名称:" + name);
        System.out.println("目标类:" + joinpoint.getTarget().getClass());
        System.out.println("begin transaction");
    }

    /**
     * 后置通知:
     *      在目标方法之后执行(如果目标方法出异常,那么后置通知将不在执行)
     *      参数:joinpoint连接点
     */
    public void commit(JoinPoint joinPoint,Object val){
        System.out.println("切入点表达式" + joinPoint.toString());
        System.out.println("连接点的名称" + joinPoint.getSignature().getName());
        System.out.println("目标方法" + joinPoint.getTarget());
        System.out.println("目标方法的返回值" + val);
        System.out.println("commit");
    }

    /**
     * 最终通知:
     *      无论目标方法是否抛出异常,都将执行
     */
    public void finallyMethod(JoinPoint joinPoint){
        System.out.println(joinPoint);
        System.out.println("最终通知");
    }

    /**
     * 异常通知:
     *      接收目标方法抛出的异常
     */
    public void throwingMethod(JoinPoint joinPoint,Throwable ex){
        System.out.println("切入点表达式" + joinPoint);
        System.out.println("异常通知:" + ex.getMessage());
    }


    /**
     * 环绕通知:
     *      环绕通知
     * @param joinPoint
     */
    public void aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("环绕通知");
        joinPoint.proceed();
        
    }
}
