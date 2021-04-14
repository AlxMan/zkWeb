package com.wxc.zk;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

public class Utils {
    public static void loadData(Properties pro, String cfg) throws IOException {
        pro.load(new StringReader(cfg));
    }

    public static void createDbPool(Properties pro) {
        // 解析配置信息，创建数据库连接池
        ConnectionManager.DRIVER = pro.getProperty("driverClassName");
        ConnectionManager.Url = pro.getProperty("url");
        ConnectionManager.USERNAME = pro.getProperty("username");
        ConnectionManager.PASSWORD = pro.getProperty("password");
        ConnectionManager.initCount = Integer.parseInt(pro.getProperty("initCount"));
        ConnectionManager.maxCount = Integer.parseInt(pro.getProperty("maxCount"));
        ConnectionManager.currentCount = Integer.parseInt(pro.getProperty("currentCount"));
        ConnectionManager.init();
    }
}
