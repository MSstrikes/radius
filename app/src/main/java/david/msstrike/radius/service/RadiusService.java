package david.msstrike.radius.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by msstrike on 2017/7/11.
 */

public class RadiusService extends Service{
    private Mybinder mybinder = new Mybinder();
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("service start");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("receive StartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("service destory");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mybinder;
    }
    class Mybinder extends Binder{
        public void startDownload(){
            System.out.println("开始下载");
        }
    }
}
