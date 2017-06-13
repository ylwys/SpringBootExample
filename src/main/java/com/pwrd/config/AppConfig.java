package com.pwrd.config;

/**
 * User: ylwys
 * Date: 2016/12/17
 * Time: 13:53
 */

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 应用配置
 */

@ConfigurationProperties(prefix = "config")
public class AppConfig {
    private String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
