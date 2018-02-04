package com.jxp.spring01.staticproxy;

import com.sun.imageio.spi.OutputStreamImageOutputStreamSpi;

public class Transaction {
    public void beginTransaction(){
        System.out.println("begin Transaction");
    }
    public void commit(){
        System.out.println("commit");
    }
}
