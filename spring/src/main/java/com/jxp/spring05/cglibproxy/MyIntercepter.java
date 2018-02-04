package com.jxp.spring05.cglibproxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

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
public class MyIntercepter implements MethodInterceptor{
    private Object targer; //目标类
    private Transaction transaction;//事务

    public MyIntercepter(Object targer, Transaction transaction) {
        super();
        this.targer = targer;
        this.transaction = transaction;
    }
    //代码增强类
    public Object createIntercept(){
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(this);//设置拦截器
        /**
         * 将来用cglib生成的代理类是不是目标类的子类,
         */
        enhancer.setSuperclass(targer.getClass());//生成代理类是父类是目标类
        return enhancer.create();
    }


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        this.transaction.beginTransaction();
        method.invoke(targer);
        this.transaction.commit();
        return null;
    }
}
