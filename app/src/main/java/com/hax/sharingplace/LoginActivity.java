package com.hax.sharingplace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText account;
    String userName;

    private EditText password;
    String passWord;

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private CheckBox rempwr;

    private Button btnLogin;

    private boolean result;

    private String address = "http://ip/FindYou/Login";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = new Bundle();
            bundle = msg.getData();
            String temp = bundle.getString("result");
            String rs[] = temp.split(":");
            result = Boolean.parseBoolean(rs[0]);
            if (result) {
                if (rempwr.isChecked()) {
                    editor.putBoolean("remember_password", true);
                    userName = account.getText().toString().trim();
                    passWord = password.getText().toString().trim();
                    editor.putString("account", userName);
                    editor.putString("password", passWord);

                } else {
                    editor.clear();
                }
                editor.putString("user", userName);
                editor.putString("email", rs[1]);
                editor.putBoolean("login", true);
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                OttoBus.getInstance().post(new BusData(userName, rs[1], true));
                finish();
                Toast.makeText(LoginActivity.this, R.string.login, Toast.LENGTH_SHORT).show(); //登录成功提示
            }else {
                Toast.makeText(LoginActivity.this, R.string.geterror, Toast.LENGTH_SHORT).show();  //登录失败提示
                OttoBus.getInstance().post(new BusData(null, null, false));
                editor.clear();
                editor.commit();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        rempwr = (CheckBox) findViewById(R.id.rempwr);
        btnLogin = (Button) findViewById(R.id.btn_login);

        editor = pref.edit();
        boolean isRemember = pref.getBoolean("remember_password", false);
        pref.getBoolean("login", false);
        if (isRemember) {
            userName = pref.getString("account", "");
            passWord = pref.getString("password", "");
            account.setText(userName);
            password.setText(passWord);
            rempwr.setChecked(true);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = account.getText().toString().trim();
                passWord = new Md5().getPassWordMD5(password.getText().toString().trim());
                if (userName.length() == 0|| password.getText().toString().trim().length() == 0) {
                    Toast.makeText(LoginActivity.this, R.string.getnull, Toast.LENGTH_SHORT).show();
                    OttoBus.getInstance().post(new BusData(null, null, false));
                    editor.clear();
                    editor.commit();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("username", userName);
                            params.put("password", passWord);
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

    public void onRgsClick(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onFgtClick(View view) {
        Intent intent = new Intent(LoginActivity.this, UpdateActivity.class);
        startActivity(intent);
    }
}
