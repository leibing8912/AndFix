package cn.jianke.andfix.httprequest;

import android.app.Activity;
import cn.jianke.andfix.AppManager;
import cn.jianke.andfix.httprequest.httpresponse.BaseResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @className: JkApiCallback
 * @classDescription: 健客网统一Api回调
 * @author: leibing
 * @createTime: 2016/08/30
 */
public class JkApiCallback<T> implements Callback<T> {
    // 回调
    private ApiCallback<T> mCallback;
    // 页面实例
    private Activity activity;

    /**
     * Constructor
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     * @param mCallback 回调
     * @param activity 页面实例
     * @return
     */
    public JkApiCallback(ApiCallback<T> mCallback, Activity activity){
        this.mCallback = mCallback;
        this.activity = activity;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (activity == null){
            throw new NullPointerException("activity == null");
        }
        if (mCallback == null){
            throw new NullPointerException("mCallback == null");
        }
        // 处理是否当前页，如果非当前页则无需回调更新UI
        if (!AppManager.getInstance().isCurrent(activity)){
            // activity去引用,避免内存泄漏
            activity = null;
            return;
        }

        // 处理回调
        if (response != null && response.body() != null){
            if (((BaseResponse)response.body()).isSuccess()){
                mCallback.onSuccess((T)response.body());
            }else {
                mCallback.onError(((BaseResponse) response.body()).getErrormsg());
            }
        }else {
            mCallback.onFailure();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (mCallback == null){
            throw new NullPointerException("mCallback == null");
        }
        mCallback.onFailure();
    }
}
