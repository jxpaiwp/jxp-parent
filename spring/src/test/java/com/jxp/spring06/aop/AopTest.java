package com.jxp.spring06.aop;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Method;


/**
 * spring原理:
 *  1.先对目标类和切面进行实例化
 *  2.当spring解析到<aop:config 
 *  3.把切入点表达式解析出来execution(* com.jxp.spring06.aop.PersonDaoImpl.*(..))为spring容器中的bean匹配,
 *  4.如果匹配成功,则为容器中的bean创建代理对象,代理方法=目标方法+通知
 *  5.当客户端利用context.getbean的时候,如果该对象有代理对象则返回代理对象,如果没有代理对象,则返回对象本身
 *      (如果切入点表达式与容器中的bean没有一个匹配成功,则报错)
 */
public class AopTest {
    
    @Test
    public void testMethod(){
        Class class1 = Object.class;
        Method[] methods = class1.getMethods();
        for (Method method : methods) {
            System.out.println(method);
        }
    }
    
    @Test
    public void testTransaction(){
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-aop.xml");
        PersonDao personDao = (PersonDao) context.getBean("pesonDao");
        personDao.savePerson();
    }

}