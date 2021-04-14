package com.wxc.zk;

import org.junit.Test;

public class PublisherTest {
    Publisher publisher = new Publisher();

    @Test
    public void publish() {
        String cfg = "driverClassName=com.mysql.jdbc.Driver" +
                "url=jdbc:mysql://linux123:3306/azkaban" +
                "username=hive" +
                "password=12345678\n" +
                "initCount=5\n" +
                "maxCount=10\n" +
                "currentCount=5";
        publisher.publish(cfg);
        System.out.println(publisher.zkClient.readData("/webapp/dblinkcfg", true).toString());
    }
}
