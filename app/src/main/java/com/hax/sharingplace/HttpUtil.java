package com.hax.sharingplace;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hax on 2018/4/28.
 */

public class HttpUtil{
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                        response.append(":");
                    }
                    if (listener != null)
                        listener.onFinish(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null)
                        listener.onError(e);
                } finally {
                    if (connection != null)
                        connection.disconnect();
                }

            }
        }).start();
    }
    public static String getURLWithParams(String address, HashMap<String, String> params) throws
            UnsupportedEncodingException{
        final String encode = "utf-8";
        StringBuilder url = new StringBuilder(address);
        url.append("?");
        for(Map.Entry<String, String> entry:params.entrySet())
        {
            url.append(entry.getKey()).append("=");
            url.append(URLEncoder.encode(entry.getValue(), encode));
            url.append("&");
        }
        url.deleteCharAt(url.length() - 1);
        return url.toString();
    }
}
