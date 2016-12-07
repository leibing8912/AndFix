package cn.jianke.andfix.httprequest.api;

import android.app.Activity;
import org.json.JSONException;
import org.json.JSONObject;
import cn.jianke.andfix.data.SpLocalCache;
import cn.jianke.andfix.data.cache.HotFixCache;
import cn.jianke.andfix.httprequest.ApiCallback;
import cn.jianke.andfix.httprequest.JkApiCallback;
import cn.jianke.andfix.httprequest.JkApiRequest;
import cn.jianke.andfix.httprequest.httpresponse.HotFixResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @className: ApiHotFixPatch
 * @classDescription: 热修复请求api
 * @author: leibing
 * @createTime: 2016/12/7
 */
public class ApiHotFixPatch {
    // api
    private ApiStore mApiStore;
    // 热修复HOST地址
    public final static String HOTFIX_PATCH_HOST_URL = "https://mbp.jianke.com";
    // 热修复缓存
    private SpLocalCache<HotFixCache> mHotFixCache;

    /**
     * Constructor
     * @author leibing
     * @createTime 2016/12/7
     * @lastModify 2016/12/7
     * @param
     * @return
     */
    public ApiHotFixPatch(){
        // 初始化api
        mApiStore = JkApiRequest.getInstance().create(ApiStore.class, HOTFIX_PATCH_HOST_URL);
        // 初始化热修复缓存
        mHotFixCache = new SpLocalCache<HotFixCache>(HotFixCache.class);
    }

    /**
     * 获取热修复信息(网上药店)
     * @author leibing
     * @createTime 2016/12/7
     * @lastModify 2016/12/7
     * @param clientVersion app版本
     * @param os 系统平台（1:android;2:ios）
     * @param activity 页面实例
     * @param callback 回调
     * @return
     */
    public void getHotUpdatePatchForMall(final String clientVersion ,
                                  final String os,
                                  final Activity activity,
                                  final ApiCallback<HotFixResponse> callback){
        convertToJsonString(activity, new CallBackListener() {
            @Override
            public void getBody(String body) {
                Call<HotFixResponse> mCall =  mApiStore.getHotUpdatePatchForMall(body, clientVersion, os);
                mCall.enqueue(new JkApiCallback<HotFixResponse>(callback, activity));
            }
        });
    }

    /**
     * 获取热修复信息（健客医生）
     * @author leibing
     * @createTime 2016/12/7
     * @lastModify 2016/12/7
     * @param clientVersion app版本
     * @param os 系统平台（1:android;2:ios）
     * @param activity 页面实例
     * @param callback 回调
     * @return
     */
    public void getHotUpdatePatchForDoctor(final String clientVersion ,
                                         final String os,
                                         final Activity activity,
                                         final ApiCallback<HotFixResponse> callback){
        convertToJsonString(activity, new CallBackListener() {
            @Override
            public void getBody(String body) {
                System.out.println("dddddddddddddddddd body = " + body);
                Call<HotFixResponse> mCall =  mApiStore.getHotUpdatePatchForDoctor(body, clientVersion, os);
                mCall.enqueue(new JkApiCallback<HotFixResponse>(callback, activity));
            }
        });
    }

    /**
     * 更新lastHotUpdateTime转化成json格式字符串
     * @author leibing
     * @createTime 2016/10/27
     * @lastModify 2016/10/27
     * @param activity 页面实例
     * @param callBackListener 回调监听
     * @return
     */
    private void convertToJsonString(Activity activity, final CallBackListener callBackListener){
        if (activity == null)
            return;
        if (mHotFixCache == null)
            mHotFixCache = new SpLocalCache<HotFixCache>(HotFixCache.class);
        if (activity.getApplicationContext() == null)
            return;
        mHotFixCache.read(activity.getApplicationContext(),
                new SpLocalCache.LocalCacheCallBack() {
            @Override
            public void readCacheComplete(Object obj) {
                long subTime = 0;
                long currentTime = System.currentTimeMillis();
                long lastTime = 0;
                try {
                if (obj != null){
                    HotFixCache cache = (HotFixCache) obj;
                    if (cache != null){
                        if (cache.hotFoxTime != -1){
                            lastTime = cache.hotFoxTime;
                        }else {
                            String str = "1970-01-01 00:00:00 000";
                            java.text.SimpleDateFormat sdf =
                                    new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
                            lastTime = sdf.parse(str).getTime();
                        }
                    }else {
                        String str = "1970-01-01 00:00:00 000";
                        java.text.SimpleDateFormat sdf =
                                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
                        lastTime = sdf.parse(str).getTime();
                    }
                }else {
                    String str = "1970-01-01 00:00:00 000";
                    java.text.SimpleDateFormat sdf =
                            new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
                    lastTime = sdf.parse(str).getTime();
                }
                }catch (Exception ex){
                }
                // 计算上次更新到现在热修复毫秒差值
                subTime = currentTime - lastTime;

                JSONObject reauestJson=new JSONObject();
                try{
                    reauestJson.put("lastHotUpdateTime",subTime + "") ;
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                String body = reauestJson.toString();
                if (callBackListener != null)
                    callBackListener.getBody(body);
            }
        });
    }

    /**
     * @interfaceName: ApiStore
     * @interfaceDescription: Api集合
     * @author: leibing
     * @createTime: 2016/12/7
     */
    private interface ApiStore {
        // 健客网上药店热修复接口
        @FormUrlEncoded
        @POST("/mbm/mall/api/sysAdmin/getHotUpdatePatch")
        Call<HotFixResponse> getHotUpdatePatchForMall(
                @Field("body") String body,
                @Field("clientVersion") String clientVersion,
                @Field("os") String os);

        // 健客医生热修复接口
        @FormUrlEncoded
        @POST("/mbm/doctor/api/sysDoctorAdmin/getHotUpdatePatch")
        Call<HotFixResponse> getHotUpdatePatchForDoctor(
                @Field("body") String body,
                @Field("clientVersion") String clientVersion,
                @Field("os") String os);
    }

    /**
     * @interfaceName: CallBackListener
     * @interfaceDescription: 回调监听
     * @author: leibing
     * @createTime: 2016/12/7
     */
    public interface CallBackListener{
        // 获取特定参数
        void getBody(String body);
    }
}
