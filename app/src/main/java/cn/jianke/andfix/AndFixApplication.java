package cn.jianke.andfix;

import android.app.Application;
import cn.jianke.andfix.module.HotFixPatch;

/**
 * @className: AndFixApplication
 * @classDescription: 应用管理
 * @author: leibing
 * @createTime: 2016/09/10
 */
public class AndFixApplication extends Application{
    // 单例(应用实例)
    private static AndFixApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化应用实例
        instance = this;
        // 初始化热修复
        HotFixPatch.init(this);
    }

    /**
     * 获取应用实例
     * @author leibing
     * @createTime 2016/12/3
     * @lastModify 2016/12/3
     * @param
     * @return
     */
    public synchronized static AndFixApplication getInstance(){
        if (instance == null)
            instance = new AndFixApplication();
        return instance;
    }
}
