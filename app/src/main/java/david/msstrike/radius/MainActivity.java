package david.msstrike.radius;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import david.msstrike.radius.service.RadiusService;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView showDegree;
    private Button startBtn;
    private Button stopBtn;
    private Button bindBtn;
    private Button unbindBtn;
    private boolean flagShowNotification = false;
    private NotificationManager manager = null;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showDegree = (TextView) findViewById(R.id.degrees);
        initSensorEntity();
        startBtn = (Button) findViewById(R.id.start);
        stopBtn = (Button) findViewById(R.id.stop);
        bindBtn = (Button) findViewById(R.id.bind);
        unbindBtn = (Button) findViewById(R.id.unbind);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MainActivity.this, RadiusService.class);
                startService(startIntent);
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent = new Intent(MainActivity.this, RadiusService.class);
                stopService(stopIntent);
            }
        });
        bindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //flagShowNotification = true;
                /*Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 10000);*/
                setNotificationHeader(1);

            }
        });
        unbindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagShowNotification = false;
                manager.cancel(1);
            }
        });
    }

    private void initSensorEntity() {
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.registerListener(MainActivity.this,
                sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float degree = event.values[0];
            showDegree.setText("此时的角度:" + degree);
            if (flagShowNotification == true){
                setNotificationHeader(degree);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void setNotificationHeader(float degree) {
        Intent mainIntent = new Intent(this,MainActivity.class);
        Intent intent = new Intent()
        PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent , PendingIntent.FLAG_UPDATE_CURRENT);
        //获得管理器
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //实例化构造器
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
        remoteViews.setImageViewResource(R.id.notification_image, R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.notification_title, "这个是标题");
        remoteViews.setTextViewText(R.id.notification_text, "这个是正文");
        //进行相关配置
        /*builder.setSmallIcon(R.mipmap.ic_launcher)
                //设置通知标题
                .setContentTitle("最简单的Notification")
                //设置通知内容
                .setContentText("此时的角度："+ degree)
                .setTicker("开始定位")
                //点击后自动清除
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent);*/
                //呼吸灯
                //.setLights(0xFF0000,3000,3000);
        builder.setContent(remoteViews)
                .setContentIntent(mainPendingIntent)
                .setWhen(System.currentTimeMillis())
                .setTicker("开始定位")
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        //设置该值后显示呼吸灯
        //notification.flags = Notification.FLAG_SHOW_LIGHTS;
        manager.notify(1, notification);
    }
}
