package com.jxp.spring05.cglibproxy;

public class PersonDaoImpl implements PersonDao {
    @Override
    public void savePerson() {
        System.out.println("save Person");
    }
}
