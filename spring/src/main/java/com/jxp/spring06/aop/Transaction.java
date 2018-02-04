package com.jxp.spring06.aop;

import org.aspectj.lang.JoinPoint;

public class Transaction {
    public void beginTransaction(JoinPoint joinPoint){
        System.out.println("连接点的名称" + joinPoint.getSignature().getName());
        System.out.println("目标方法"+joinPoint.getTarget());
        System.out.println("begin transaction");
    }
    public void commit(JoinPoint joinPoint){
        System.out.println(joinPoint.getSignature().getName());
        System.out.println("commit");
    }
}
