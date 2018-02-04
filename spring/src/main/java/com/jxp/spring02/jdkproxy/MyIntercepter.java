package com.jxp.spring02.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 拦截器:
 *  1.目标类导入,
 *  2.事务导入
 *  
 * jdk动态代理的缺点:
 *  1.功能单一,只能处理目标对象的目标方法,和处理事务
 *  2.
 */
public class MyIntercepter implements InvocationHandler {
    private Object targer; //目标类
    private Transaction transaction;//事务

    public MyIntercepter() {
    }

    public MyIntercepter(Object targer, Transaction transaction){
        super();
        this.targer = targer;
        this.transaction = transaction;
    }
    /**
     * invoke方法的功能
     *  1.开启事务,
     *  2.调用目标方法
     *  3.提交事务
     * @param proxy
     * @param method 目标方法
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        if ("savePerson".equals(name)||"updatePerson".equals(name)||"deletePerson".equals(name)){
            this.transaction.beginTransaction(); //开启事务
            method.invoke(targer);//调用目标对象的方法
            this.transaction.commit();//事务的提交
        }else {
            method.invoke(targer);
        }
        return null;
    }
}
