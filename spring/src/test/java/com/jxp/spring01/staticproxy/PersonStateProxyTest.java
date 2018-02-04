package com.jxp.spring01.staticproxy;

import org.junit.Test;

import static org.junit.Assert.*;

public class PersonStateProxyTest {
    
    /**
     * 静态代理的缺点
     *  1.静态代理并没有做到事务的重用
     *  2.假设dao有100个类,100个proxy,借口中有多少个方法,
     *      在proxy中就要实现多少次方法,有多少方法就要开启多少次事务
     *  3.如果一个proxy实现了多个接口,其中一个接口方法变化(添加一个方法),那么proxy也要做相应的变化
     */
    @Test
    public void savePerson() {
        PersonDao personDao = new PersonDaoImpl();
        Transaction transaction = new Transaction();
        PersonStateProxy proxy = new PersonStateProxy(personDao, transaction);
        proxy.savePerson();
                
    }
}