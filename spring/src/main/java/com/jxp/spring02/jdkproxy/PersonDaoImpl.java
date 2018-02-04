package com.jxp.spring02.jdkproxy;

public class PersonDaoImpl implements PersonDao {
    @Override
    public void savePerson() {
        System.out.println("save Person");
    }
}
