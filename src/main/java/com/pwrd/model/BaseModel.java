package com.pwrd.model;



/**
 * User: ylwys
 * Date: 2016/12/21
 * Time: 11:08
 */
public class BaseModel {

    public long id;

    public BaseModel() {
    }

    public BaseModel(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
