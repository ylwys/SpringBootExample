package com.pwrd.support;

/**
 * User: ylwys
 * Date: 2016/12/17
 * Time: 15:39
 */
public enum PageKey {

    LOGIN("/index", 0),
    ERROR("/error", 0),
    TEST("/test", 0);

    PageKey(String pageName, int level) {
        this.pageName = pageName;
        this.level = level;
    }

    private String pageName;

    private int level;


    public String getPageName() {
        return pageName;
    }

    public int getLevel() {
        return level;
    }
}
