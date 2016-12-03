package cn.jianke.andfix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cn.jianke.andfix.module.HotFixPatch;

public class MainActivity extends AppCompatActivity{
    // 测试
    private TextView mainTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    public String getVersion(String version){
        return version;
    }
}
