package com.hax.sharingplace;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class FrenadrActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MapView mapView;
    private BaiduMap baiduMap;

    private TimerTask timerTask;
    private Timer timer = new Timer();

    private String name, address = "http://ip/FindYou/PostLocation";

    public LocationClient mlocationClient;

    boolean isFirstLocate = true;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = new Bundle();
            bundle = msg.getData();
            String temp = bundle.getString("result");
            //Log.d("temp", temp);
            String rs[] = temp.split(":");
            getLocation(Double.parseDouble(rs[0]),Double.parseDouble(rs[1]));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mlocationClient = new LocationClient(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_frenadr);

        mapView = (MapView) findViewById(R.id.adr_mapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        mapView.removeViewAt(1);

        toolbar = (Toolbar) findViewById(R.id.fri_toolbar);
        setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    String completeURL = HttpUtill.getURLWithParams(address, params);
                    HttpUtill.sendHttpRequest(completeURL, new HttpCallbackListener() {
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

                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 100, 5000);
        requestLocation();
    }

    private void requestLocation() {
        initLocation();
        mlocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setOpenGps(true);
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mlocationClient.setLocOption(option);
    }


    private void getLocation(double x, double y){
        if (isFirstLocate) {
            LatLng ll = new LatLng(x, y);
            MapStatus newMapStatus = new MapStatus.Builder().target(ll).zoom(18f).build();
            MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(newMapStatus);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(x);
        locationBuilder.longitude(y);
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }




    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        mlocationClient.stop();
        timer.cancel();
    }

}
