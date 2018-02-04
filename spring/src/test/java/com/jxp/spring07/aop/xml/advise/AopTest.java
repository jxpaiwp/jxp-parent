package com.jxp.spring07.aop.xml.advise;

import javafx.application.Application;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class AopTest {
    @Test
    public void testAop(){
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-aop.xml");
        PersonDao personDao = (PersonDao) context.getBean("personDao");
        personDao.savePerson();
    }

}