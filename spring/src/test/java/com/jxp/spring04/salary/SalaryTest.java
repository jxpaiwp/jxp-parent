package com.jxp.spring04.salary;

import com.sun.deploy.config.SecuritySettings;
import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

public class SalaryTest {
    @Test
    public void testSalary(){
        Object targer=new SalaryManagerImpl();
        Logger logger = new Logger();
        Security securitys = new Security();
        Privilege privilege = new Privilege();
        privilege.setAccess("admin");
        MyInterceptor interceptor = new MyInterceptor(targer,logger,securitys,privilege);
        /**
         * 参数1.目标的加载器,
         *     2.目标类的所有方法
         *     3.拦截器
         */
        SalaryManager salaryManager = (SalaryManager) Proxy.newProxyInstance(targer.getClass().getClassLoader(),
                targer.getClass().getInterfaces(), interceptor);
        salaryManager.showSalary();
        
        
    }

}