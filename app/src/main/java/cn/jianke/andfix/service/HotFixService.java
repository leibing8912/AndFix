package cn.jianke.andfix.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.alipay.euler.andfix.BuildConfig;

import cn.jianke.andfix.R;
import cn.jianke.andfix.commons.FileDownLoaderUtils;
import cn.jianke.andfix.commons.StringUtil;
import cn.jianke.andfix.httprequest.ApiCallback;
import cn.jianke.andfix.httprequest.api.ApiHotFixPatch;
import cn.jianke.andfix.module.HotFixPatch;

/**
 * @className: HotFixService
 * @classDescription: 热修复服务(此方法暂停，用进入首页方式触发请求热更新数据方案)
 * @author: leibing
 * @createTime: 2016/09/10
 */
public class HotFixService extends Service{
    // 向服务器请求周期(四个小时请求一次)
    private final static long HOT_FIX_TIME = 4*60*60;
    // 热修复Handler
    private Handler hotFixHandler = new Handler();
    // 用于请求服务器,是否更新当前热修复包
    private Runnable hotFixRunnable = new Runnable() {
        @Override
        public void run() {
            // 向服务端请求是否需要更新热修复包
            updateHotFixPatch();
            // 每隔一段时间向服务端请求一次数据
            hotFixHandler.postDelayed(hotFixRunnable, HOT_FIX_TIME);
            // 启动服务为前台服务
            startForeground();
        }
    };

    /**
     * 向服务端请求是否需要更新热修复包
     * @author leibing
     * @createTime 2016/12/3
     * @lastModify 2016/12/3
     * @param
     * @return
     */
    private void updateHotFixPatch() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 启动服务为前台服务( 让该service前台运行，避免手机休眠时系统自动杀掉该服务)
     * @author leibing
     * @createTime 2016/09/07
     * @lastModify 2016/09/07
     * @param
     * @return
     */
    public void startForeground(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        // 设置头像
        builder.setSmallIcon(R.mipmap.ic_launcher);
        // 设置标题
        builder.setContentTitle("foreground service");
        // 设置内容
        builder.setContentText("try to avoid this service be killed!");
        // 创建notification
        Notification notification = builder.build();
        //如果 id 为 0 ，那么状态栏的 notification 将不会显示。
        startForeground(0, notification);
    }

    @Override
    public void onDestroy() {
        // 取消前台进程
        stopForeground(true);

        // 服务被杀死时继续重启服务
        Intent intent = new Intent();
        intent.setClass(this, HotFixService.class);
        startService(intent);

        super.onDestroy();
    }
}
