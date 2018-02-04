package com.jxp.spring01.staticproxy;

import javax.sound.midi.Track;

public class PersonStateProxy implements PersonDao{
    private PersonDao personDao;
    private Transaction transaction;


    public PersonStateProxy (PersonDao personDao,Transaction transaction) {
        super();
        this.personDao = personDao;
        this.transaction = transaction;
    }

    @Override
    public void savePerson() {
        //1开启事务
        //2执行目标方法
        //3开启事务
        transaction.beginTransaction();
        personDao.savePerson();
        transaction.commit();
    }
}
