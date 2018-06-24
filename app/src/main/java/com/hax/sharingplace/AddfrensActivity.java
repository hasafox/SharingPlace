package com.hax.sharingplace;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddfrensActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText inputUser;

    private TextView select;

    private RecyclerView recyclerView;

    boolean flag;

    private SharedPreferences pref;

    private String uname, sname;

    boolean rs0, rs1, rs2;

    private List<News> newsList = new ArrayList<>();

    private String address = "http://ip/FindYou/AddFriends"; //ip means your server ip

    private String address1 = "http://ip/FindYou/Newmsg";

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = new Bundle();
            bundle = msg.getData();
            String temp = bundle.getString("result");
            String rs[] = temp.split(":");

            rs0 = Boolean.parseBoolean(rs[0]);
            rs1 = Boolean.parseBoolean(rs[1]);
            rs2 = Boolean.parseBoolean(rs[2]);
            if (!rs0)
                Toast.makeText(AddfrensActivity.this, R.string.nulluser, Toast.LENGTH_SHORT).show();
            else if (rs1)
                Toast.makeText(AddfrensActivity.this, R.string.friends, Toast.LENGTH_SHORT).show();
            else if (rs2)
                Toast.makeText(AddfrensActivity.this, R.string.sendadd, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(AddfrensActivity.this, R.string.wait, Toast.LENGTH_SHORT).show();
        }
    };

    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = new Bundle();
            bundle = msg.getData();
            String temp = bundle.getString("result");

            String rs[] = temp.split(":");

            int n = rs.length;
            for (int i = 0; i < n; i++)
                newsList.add(i, new News(rs[i]));

            /*NewsAdapter adapter = new NewsAdapter(newsList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();*/
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfrens);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        toolbar = (Toolbar) findViewById(R.id.add_toolbar);
        inputUser = (EditText) findViewById(R.id.input_username);
        select = (TextView) findViewById(R.id.select);

        recyclerView = (RecyclerView) findViewById(R.id.add_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        if(newsList != null) {
            NewsAdapter adapter = new NewsAdapter(newsList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        flag = pref.getBoolean("login", false);
        uname = pref.getString("user", "");

        setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        sname = inputUser.getText().toString().trim();
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("uname", uname);
                        params.put("sname", sname);
                        if (flag) {
                            try {
                                String comleteURL = HttpUtil.getURLWithParams(address, params);
                                HttpUtil.sendHttpRequest(comleteURL, new HttpCallbackListener() {
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
                        } else
                            Toast.makeText(AddfrensActivity.this, R.string.gologin, Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }).start();
            }
        });

        if (flag) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("uname", uname);
                    try {
                        String comleteURL = HttpUtil.getURLWithParams(address1, params);
                        HttpUtil.sendHttpRequest(comleteURL, new HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) {
                                //Log.d("response", response);
                                Bundle bundle = new Bundle();
                                bundle.putString("result", response);
                                Message msg = new Message();
                                msg.setData(bundle);
                                handler1.sendMessage(msg);
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

}
