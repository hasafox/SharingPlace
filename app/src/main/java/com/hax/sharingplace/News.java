package com.hax.sharingplace;

/**
 * Created by hax on 2018/5/14.
 */

public class News {
    private String ask;
    //private String agree;
    // private String disagree;

    public News(String ask) {
        this.ask = ask;
        //this.agree = agree;
        //this.disagree = disagree;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }
}

    /*public String getAgree() {
        return agree;
    }

    public void setAgree(String agree) {
        this.agree = agree;
    }

    public String getDisagree() {
        return disagree;
    }

    public void setDisagree(String disagree) {
        this.disagree = disagree;
    }
}*/
