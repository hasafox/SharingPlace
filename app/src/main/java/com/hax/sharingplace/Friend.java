package com.hax.sharingplace;

/**
 * Created by hax on 2018/5/11.
 */

public class Friend {
    private String name;
    private String status;

    public Friend(String name, String status){
        this.name = name;
        this.status = status;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
