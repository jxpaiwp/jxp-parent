package com.jxp.spring02.jdkproxy;

import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.junit.Assert.*;


/**
 * 1.拦截器的invoke方法在什么时候执行
 *      答:当在代理对象调用方法的时候,进入了拦截器的invoke方法
 * 2.代理对象的方法体的内容是什么
 *      答:拦截器的invoke方法的内容就是代理对象方法的内容
 * 3.拦截器中的invoke方法的参数method是谁在什么时候传递过来的
 *      代理对象调用方法的时候,进入了拦截器的invoke方法,invoke的方法method就是代理对象调用的方法
 *      
 * 
 */
public class JDKProxy {
    /**
     * 1.创建目标方法
     * 2.创建事务
     * 3.创建一个拦截器
     * 4.动态产生代理对象
     */
    @Test
    public void testJDKProxy(){
        Object target = new PersonDaoImpl();
        Transaction transaction = new Transaction();
        MyIntercepter intercepter = new MyIntercepter(target, transaction);
        /**
         * 1.目标类的加载器
         * 2.目标类实现的所有接口
         * 3.拦截器
         * 返回的是一个代理对象,代理类和目标类实现共同的接口
         */
        PersonDao personDao = (PersonDao) Proxy.newProxyInstance(target.getClass().getClassLoader()
                , target.getClass().getInterfaces(), intercepter);
        personDao.savePerson();
        /**
         * 问题1:
         */
    }

}