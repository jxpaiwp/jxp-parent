package com.jxp.spring04.salary;

import javax.lang.model.SourceVersion;
import javax.lang.model.util.ElementScanner6;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.security.PrivateKey;

public class MyInterceptor implements InvocationHandler {
    private Object targer;
    private Logger logger;
    private Security security;
    private Privilege privilege;
    private SalaryManager salaryManager;

    public MyInterceptor(Object targer, Logger logger, Security security, Privilege privilege) {
        super();
        this.targer = targer;
        this.logger = logger;
        this.security = security;
        this.privilege = privilege;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.logger.logger();
        this.security.security();
        if ("admin".equals(this.privilege.getAccess())){
            method.invoke(targer);
        }else {
            System.out.println("该用户没有权限查看");
        }
        return null;
    }
}
