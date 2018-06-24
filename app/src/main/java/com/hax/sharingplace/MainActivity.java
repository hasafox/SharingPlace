package com.hax.sharingplace;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
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
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public LocationClient mLocationClient;

    private TextView  navEmail, navUser;

    private MapView mapView;

    private BaiduMap baiduMap;

    private DrawerLayout mDrawerLayout;

    private boolean isFirstLocate = true;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Bus bus;

    private double x, y;

    private String address = "http://ip/FindYou/GetLocation";

    //private FloatingActionButton iLocation;

    private long exitTime = 0;

    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        //iLocation = (FloatingActionButton) findViewById(R.id.icLocation);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        flag = pref.getBoolean("login", false);
        //Log.d("flag", String.valueOf(flag));

        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        mapView.removeViewAt(1);

        navView.setCheckedItem(R.id.nav_home);

        View headerView = navView.getHeaderView(0);
        navEmail = headerView.findViewById(R.id.nav_email);
        navUser = headerView.findViewById(R.id.nav_username);
        navEmail.setText(pref.getString("email", ""));
        navUser.setText(pref.getString("user", getResources().getString(R.string.login_warn)));

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()){
                   case R.id.nav_home:
                       mDrawerLayout.closeDrawers();
                       break;
                   case R.id.nav_my_friend:
                       Intent myintent = new Intent(MainActivity.this, MyfrensActivity.class);
                       startActivity(myintent);
                       break;
                   case R.id.nav_add_friend:
                       Intent addintent = new Intent(MainActivity.this, AddfrensActivity.class);
                       startActivity(addintent);
                       break;
                   case R.id.nav_about_us:
                       Intent abtintent = new Intent(MainActivity.this, AboutActivity.class);
                       startActivity(abtintent);
                       break;
                   case R.id.nav_exit:
                       if (!flag){
                           Toast.makeText(MainActivity.this, R.string.gologin, Toast.LENGTH_SHORT).show();
                   }else {
                           if ((System.currentTimeMillis() - exitTime) > 2000) {
                               Toast.makeText(MainActivity.this, R.string.touchagain, Toast.LENGTH_SHORT).show();
                               exitTime = System.currentTimeMillis();
                           }else {
                               editor.putBoolean("login", false);
                               editor.putString("user", getResources().getString(R.string.login_warn));
                               editor.putString("email","");
                               editor.commit();
                               finish();
                               System.exit(0);
                           }
                       }
                       break;
                   default:break;
               }
                return true;
            }
        });
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }else {
            requestLocation();
        }
        bus=OttoBus.getInstance();
        bus.register(this);
    }

    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setOpenGps(true);
        option.setScanSpan(5000);
        option.setNeedDeviceDirect(true);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    //申请用户权限
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatus newMapStatus = new MapStatus.Builder().target(ll).zoom(18f).build();
            MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(newMapStatus);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        x = location.getLatitude();
        y = location.getLongitude();
        locationBuilder.latitude(x);
        locationBuilder.longitude(y);
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("x", String.valueOf(x));
                params.put("y", String.valueOf(y));
                params.put("name", (String)navUser.getText());
                if (flag) {
                    try {
                        String completeURL = HttpUtil.getURLWithParams(address, params);
                        HttpUtil.sendHttpRequest(completeURL, new HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) {
                                //Log.d("response", response);
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

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(location);
            }
        }

        public void onConnectHotSpotMessage(String s, int i) {

       }
    }
    public void onIconClick(View view){
        if (!flag) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
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
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        bus.unregister(this);
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            onBackPressed();
            return false;
        }else
            return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    @Subscribe
    public void setContent(BusData data){
        navUser.setText(data.getUser());
        navEmail.setText(data.getEmail());
        flag = data.getLogin();
    }
}
