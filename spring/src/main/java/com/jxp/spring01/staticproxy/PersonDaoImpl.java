package com.jxp.spring01.staticproxy;

public class PersonDaoImpl implements PersonDao {
    @Override
    public void savePerson() {
        System.out.println("save Person");
    }
}
