package com.pwrd.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: ylwys
 * Date: 2016/12/17
 * Time: 15:29
 */

@Entity
@Table(name = "t_user")
public class User extends BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String account;

    private String password;

    private int level;

    public User() {
    }

    public User(long id) {
        this.id = id;
    }

    public User(String account, String password, int level) {
        this.account = account;
        this.password = password;
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
