package com.jxp.spring07.aop.xml.advise;

public class PersonDaoImpl implements PersonDao {
    @Override
    public String savePerson() {
//        int i=1/0;
        System.out.println("save person");
        return "1111";
    }
}
