package com.hax.sharingplace;

import com.squareup.otto.Bus;

/**
 * Created by hax on 2018/4/30.
 */

public class OttoBus extends Bus {
    private volatile static OttoBus bus;
    private OttoBus (){
    }
    public static OttoBus getInstance() {
        if (bus == null) {
            synchronized (OttoBus.class){
                if(bus==null){
                    bus = new OttoBus();
                }
            }
        }
        return bus;
    }
}

