package com.jxp.spring06.aop;

public class PersonDaoImpl implements PersonDao {
    @Override
    public void savePerson() {
        System.out.println("save person");
    }
}
