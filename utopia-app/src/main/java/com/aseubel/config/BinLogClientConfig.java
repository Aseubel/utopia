package com.aseubel.config;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class BinLogClientConfig {

    @Value("${spring.datasource.url:jdbc:mysql://127.0.0.1:3306/utopia?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&serverTimezone=UTC&useSSL=true}")
    private String mysqlUrl;

    @Value("${spring.datasource.username:root}")
    private String mysqlUser;

    @Value("${spring.datasource.password:123456}")
    private String mysqlPassword;

    @Bean(name = "binaryLogClient")
    public BinaryLogClient binaryLogClient() {
        int mysqlPort = Integer.parseInt(mysqlUrl.split(":")[3].split("/")[0]);
        String mysqlHost = mysqlUrl.split(":")[2].replace("/", "");
        BinaryLogClient binaryLogClient = new BinaryLogClient(mysqlHost, mysqlPort, mysqlUser, mysqlPassword);
        return binaryLogClient;
    }

}
