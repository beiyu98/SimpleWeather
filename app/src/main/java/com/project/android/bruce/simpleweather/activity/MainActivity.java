package com.project.android.bruce.simpleweather.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.project.android.bruce.simpleweather.R;
import com.project.android.bruce.simpleweather.model.CityWeather;
import com.project.android.bruce.simpleweather.service.UpdateService;
import com.project.android.bruce.simpleweather.util.ParserTool;
import com.project.android.bruce.simpleweather.util.PureNetUtil;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MainActivity 是程序的主活动界面
 */
public class MainActivity extends AppCompatActivity {

    private TextView cityName,weather,temperature,refreshTime,swipeTv,systemTime;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String returnData;

    private CityWeather cityWeather;

    public static final int SUCCEED = 1;

    SharedPreferences pre;

    private static final  String TAG = "MainActivity";

    private String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String time = sdf.format(date);
        return time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        onSwipeViewListener();

        UpdateBroadcastReceiver receiver = new UpdateBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.project.android.bruce.simpleweather.receiver");
        registerReceiver(receiver,filter);
    }

    private void onSwipeViewListener() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiper_container);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                swipeTv.setVisibility(View.VISIBLE);
                swipeTv.setText("正在刷新");
                threadRefresh();
            }
        });
    }

    private void initViews() {
        swipeTv = (TextView) findViewById(R.id.refresh_tv);
        cityName = (TextView) findViewById(R.id.city_name);
        weather = (TextView) findViewById(R.id.weather);
        temperature = (TextView) findViewById(R.id.temperature);
        refreshTime = (TextView) findViewById(R.id.refresh_time);
        systemTime = (TextView) findViewById(R.id.system_time);

        pre = getSharedPreferences("savedata",MODE_PRIVATE);
        String name = pre.getString("cityName","");
        String detail = pre.getString("weather","");
        String saveTemperature = pre.getString("temperature","");
        String data = pre.getString("refreshTime","");
        if (name!=null&detail!=null&saveTemperature!=null&data!=null){
            cityName.setText(name);
            weather.setText(detail);
            temperature.setText(saveTemperature);
            refreshTime.setText(data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_location:
                Intent intent = new Intent(this,SelectCityActivity.class);
                startActivityForResult(intent,1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK) {
                    returnData = data.getStringExtra("data");
                    startService(new Intent(this, UpdateService.class));
                    threadRefresh();
                    Log.d("MainActivity","returnData="+returnData);
                }
                break;
            default:
        }
    }

    private void threadRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("MainActivity","多线程内部："+returnData);
                if (returnData == null){
                    returnData = pre.getString("cityName","");
                }
                String responseData = PureNetUtil.getData(returnData);
                ParserTool parserTool = new ParserTool();
                try {
                    cityWeather = parserTool.parserData(responseData);
                    Message message = Message.obtain();
                    message.obj = cityWeather;
                    message.what = SUCCEED;
                    mHandler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public class UpdateBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            threadRefresh();
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SUCCEED:
                    cityWeather = (CityWeather) msg.obj;
                    cityName.setText(cityWeather.getCityName());
                    weather.setText(cityWeather.getDetail());
                    temperature.setText(cityWeather.getTemperature());
                    refreshTime.setText(cityWeather.getData());

                    systemTime.setText(getTime());
                    Log.d(TAG,"更新时间："+getTime());

                    pre = getSharedPreferences("savedata",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pre.edit();
                    editor.putString("cityName", cityWeather.getCityName());
                    editor.putString("weather", cityWeather.getDetail());
                    editor.putString("temperature", cityWeather.getTemperature());
                    editor.putString("refreshTime", cityWeather.getData());
                    editor.commit();

                    swipeTv.setText("刷新完成");
                    swipeTv.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);

                    Notification.Builder builder = new Notification.Builder(MainActivity.this);
                    builder.setSmallIcon(R.drawable.barlogo);
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                    builder.setAutoCancel(true);
                    builder.setContentTitle("天气更新通知");
                    builder.setContentText(cityWeather.getCityName()+":"+cityWeather.getDetail());
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(0, builder.build());

                    break;
            }

        }
    };
}
