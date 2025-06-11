package com.feng.transaction;

import java.io.Serializable;

/**
 * @author fengsy
 * @date 7/12/21
 * @Description
 */

public class Student implements Serializable {
    private Integer id;
    private String realname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
}
