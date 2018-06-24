package com.hax.sharingplace;

/**
 * Created by hax on 2018/4/30.
 */

public class BusData {
    public String user;
    public String email;
    public boolean login;

    public BusData(String user, String email, boolean login){
        this.user = user;
        this.email = email;
        this.login = login;
    }

    public String getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public boolean getLogin(){
        return login;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }
}
