package com.tk.controller;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 配置数据源
 */

@Configuration
public class DatabaseConfiguration {
    @Bean
    public DruidDataSource dataSource() {

        DruidDataSource dataSource = new DruidDataSource();
        //系统参数
        String url = System.getProperty("url");
        String user = System.getProperty("user");
        String password = System.getProperty("password");

        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://"+url+"/odm?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
        dataSource.setUsername(user);
        dataSource.setPassword(password);

//        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://62.234.120.239:3306/generator?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
//        dataSource.setUsername("root");
//        dataSource.setPassword("123456");

        //配置初始化大小、最小、最大
        dataSource.setInitialSize(20);
        dataSource.setMinIdle(10);
        dataSource.setMaxActive(2000);
        //配置获取连接等待超时的时间
        dataSource.setMaxWait(600000);

        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);

        //打开PSCache，并且指定每个连接上PSCache的大小
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);

        //超过时间限制是否回收
        dataSource.setRemoveAbandoned(true);
        //超时时间；单位为秒
        dataSource.setRemoveAbandonedTimeout(30);
        //关闭abanded连接时输出错误日志
        dataSource.setLogAbandoned(true);

        return dataSource;
    }
}
