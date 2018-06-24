package com.hax.sharingplace;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText regAccount, regOldpwr, regNewpwr, regEmail;

    private Button regReg;

    private String account, oldpwr, newpwr, email;

    private String address = "http://ip/FindYou/Register";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = new Bundle();
            bundle = msg.getData();
            String temp = bundle.getString("result");
            String rs[] = temp.split(":");
            if (Boolean.parseBoolean(rs[0])) {
                Toast.makeText(RegisterActivity.this, R.string.hasuser, Toast.LENGTH_SHORT).show();
            }
            else if (Boolean.parseBoolean(rs[1])) {
                Toast.makeText(RegisterActivity.this, R.string.hasemail, Toast.LENGTH_SHORT).show();
            }else if (Boolean.parseBoolean(rs[2])) {
                    Toast.makeText(RegisterActivity.this, R.string.regsecs, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(RegisterActivity.this, R.string.dknull, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regAccount = (EditText) findViewById(R.id.reg_account);
        regOldpwr = (EditText) findViewById(R.id.reg_oldpwr);
        regNewpwr = (EditText) findViewById(R.id.reg_newpwr);
        regEmail = (EditText) findViewById(R.id.reg_email);
        regReg = (Button) findViewById(R.id.reg_register);

        regReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account = regAccount.getText().toString().trim();
                oldpwr = regOldpwr.getText().toString().trim();
                newpwr = regNewpwr.getText().toString().trim();
                email = regEmail.getText().toString().trim();
                Log.e(account, email);
                String a[] = new String[]{account, oldpwr, newpwr, email};
                 if (a[0].length() == 0||a[1].length() == 0||a[2].length() == 0||a[3].length() == 0) {
                    Toast.makeText(RegisterActivity.this, R.string.notEmpty, Toast.LENGTH_SHORT).show();
                }
                else if (oldpwr.equals(newpwr) == false) {
                    Toast.makeText(RegisterActivity.this, R.string.notEqual, Toast.LENGTH_SHORT).show();
                } else {
                    newpwr = new Md5().getPassWordMD5(newpwr);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("username", account);
                            params.put("email", email);
                            params.put("password", newpwr);
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
            }
        });
    }
}
