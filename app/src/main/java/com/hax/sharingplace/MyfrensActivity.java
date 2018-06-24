package com.hax.sharingplace;

import android.content.Intent;
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
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyfrensActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private List<Friend> friendList = new ArrayList<>();

    private SharedPreferences pref;
    private String uname, address = "http://ip/FindYou/Friends";
    private boolean flag;

    private String address1 = "http://ip/FindYou/PostStatus";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = new Bundle();
            bundle = msg.getData();
            String temp = bundle.getString("result");

            String[] rs = temp.split(":");
            int n = rs.length;

            for (int i = 0, j = 0; i < n; i += 2, j++)
                friendList.add(j, new Friend(rs[i], rs[i+1]));

            /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyfrensActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);*/
                FriendAdapter adapter = new FriendAdapter(friendList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            adapter.setOnItemClickListener(new FriendAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    Friend friend = friendList.get(position);
                    String bname = friend.getName();
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("aname", uname);
                    params.put("bname", bname);
                    try {
                        String completeURL = HttpUtil.getURLWithParams(address1, params);
                        HttpUtil.sendHttpRequest(completeURL, new HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) {
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
                    }catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onLongClick(int position) {

                }
            });

        }
    };

    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = new Bundle();
            bundle = msg.getData();
            String temp = bundle.getString("result");
            //Log.d("temp", temp);
            String[] rs = temp.split(":");

            if ("1".equals(rs[0])){
                Intent intent = new Intent(MyfrensActivity.this, FrenadrActivity.class);
                intent.putExtra("name", rs[1]);
                startActivity(intent);
            }
            else
                Toast.makeText(MyfrensActivity.this, R.string.allow, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfrens);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        /*if (friendList != null) {
            FriendAdapter adapter = new FriendAdapter(friendList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }*/

        pref = PreferenceManager.getDefaultSharedPreferences(this);
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("name", uname);
                if (flag) {
                    try {
                        String completeURL = HttpUtil.getURLWithParams(address, params);
                        HttpUtil.sendHttpRequest(completeURL, new HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) {
                                //Log.d("response", response);
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
                }
                Looper.loop();
            }
        }).start();
    }


}
