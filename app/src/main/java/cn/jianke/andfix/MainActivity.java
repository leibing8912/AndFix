package cn.jianke.andfix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import cn.jianke.andfix.commons.FileDownLoaderUtils;
import cn.jianke.andfix.commons.StringUtil;
import cn.jianke.andfix.httprequest.ApiCallback;
import cn.jianke.andfix.httprequest.api.ApiHotFixPatch;
import cn.jianke.andfix.httprequest.httpresponse.HotFixResponse;
import cn.jianke.andfix.module.HotFixPatch;

/**
 * @className:MainActivity
 * @classDescription: 热修复首页
 * @author: leibing
 * @createTime: 2016/12/7
 */
public class MainActivity extends AppCompatActivity{
    // 测试
    private TextView mainTv;
    // 热更新api接口
    private ApiHotFixPatch mApiHotFixPatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化热更新api接口
        mApiHotFixPatch = new ApiHotFixPatch();
        // 此处向服务端请求是否需要更新热修复包
        updateHotFixPatch();
        // findView
        mainTv = (TextView) findViewById(R.id.tv_main);
        findViewById(R.id.btn_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotFixPatch.loadPatch();
            }
        });
        mainTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainTv.setText(getVersion("版本为V1.2"));
            }
        });
    }

    /**
     *
     * @author leibing
     * @createTime 2016/12/3
     * @lastModify 2016/12/3
     * @param
     * @return
     */
    private void updateHotFixPatch() {
        // 初始化热更新api接口
        mApiHotFixPatch = new ApiHotFixPatch();
        if (StringUtil.isNotEmpty(BuildConfig.VERSION_NAME)) {
            // 请求健客医生热更新数据
            mApiHotFixPatch.getHotUpdatePatchForDoctor(BuildConfig.VERSION_NAME,
                    "1", MainActivity.this,
                    new ApiCallback<HotFixResponse>() {
                @Override
                public void onSuccess(HotFixResponse response) {
                    System.out.println("ddddddddddddddddddd onSuccess");
                    dealHotFixEvent(response);
                }

                @Override
                public void onError(String err_msg) {
                    System.out.println("ddddddddddddddddddd onError");
                }

                @Override
                public void onFailure() {
                    System.out.println("ddddddddddddddddddd onFailure");
                }
            });
        }
    }

    /**
     * 处理热修复事件
     * @author leibing
     * @createTime 2016/12/7
     * @lastModify 2016/12/7
     * @param response
     * @return
     */
    private void dealHotFixEvent(HotFixResponse response) {
        if (response == null)
            return;
        String fileUrl = response.url;
        System.out.println("dddddddddddddddddddd fileUrl = " + fileUrl);
        // 若链接为空则返回
        if (StringUtil.isEmpty(fileUrl))
            return;
        FileDownLoaderUtils.getInstance().downLoaderFile(MainActivity.this, fileUrl,
                HotFixPatch.JIANKE_PATCHES, HotFixPatch.APATCH_NAME);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public String getVersion(String version){
        return version;
    }
}
