package com.hax.sharingplace;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class UpdateActivity extends AppCompatActivity {

    private EditText udtEmail;
    String email;

    private Button btnSubmit;

    private String address = "http://ip/FindYou/Update";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = new Bundle();
            bundle = msg.getData();
            String temp = bundle.getString("result");
            String rs[] = temp.split(":");
            boolean result = Boolean.parseBoolean(rs[0]);
            if (result)
                Toast.makeText(UpdateActivity.this, R.string.findemail, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(UpdateActivity.this, R.string.unemail, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        udtEmail = (EditText) findViewById(R.id.udt_email);
        btnSubmit = (Button) findViewById(R.id.udt_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = udtEmail.getText().toString().trim();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("email", email);
                        try {
                            String completeURL = HttpUtil.getURLWithParams(address, params);
                            HttpUtil.sendHttpRequest(completeURL, new HttpCallbackListener() {
                                @Override
                                public void onFinish(String response) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("result", response);
                                    Message msg = new Message();
                                    msg.setData(bundle);
                                    handler.sendMessage(msg);
                                }
                                @Override
                                public void onError(Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Looper.loop();
                    }
                }).start();
            }
        });
    }
}
