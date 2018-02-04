package com.jxp.spring05.cglibproxy;

import org.junit.Test;


public class CglibProxy {
    
    @Test
    public void testCglibProxy(){
        Object targer = new PersonDaoImpl();
        Transaction transaction = new Transaction();
        MyIntercepter intercepter = new MyIntercepter(targer, transaction);
        PersonDaoImpl personDao = (PersonDaoImpl) intercepter.createIntercept();
        personDao.savePerson();
    }

}