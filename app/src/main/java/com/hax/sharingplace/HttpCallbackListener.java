package com.hax.sharingplace;

/**
 * Created by hax on 2018/4/28.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
