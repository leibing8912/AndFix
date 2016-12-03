package cn.jianke.andfix.module;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import com.alipay.euler.andfix.BuildConfig;
import com.alipay.euler.andfix.patch.PatchManager;
import java.io.File;
import java.io.IOException;
import cn.jianke.andfix.commons.FileDownLoaderUtils;
import cn.jianke.andfix.commons.FileUtils;

/**
 * @className: HotFixPatch
 * @classDescription: 热修复(用于修复线上的bug)
 * @author: leibing
 * @createTime: 2016/09/10
 */
public class HotFixPatch {
    // 补丁文件名
    public static final String APATCH_NAME = BuildConfig.APPLICATION_ID
            + "_" + BuildConfig.VERSION_CODE + "_" + "fix.apatch";
    // 补丁文件夹
    public static final String JIANKE_PATCHES="/jianke_patches/";
    // 热修复管理
    public static PatchManager mPatchManager;

    /**
     * 初始化热修复
     * @author leibing
     * @createTime 2016/09/10
     * @lastModify 2016/09/10
     * @param context
     * @return
     */
    public static void init(Context context){
        // 初始化patch管理类
        mPatchManager = new PatchManager(context);
        // 初始化patch版本
        mPatchManager.init(getVersionName(context));
        // 加载已经添加到PatchManager中的patch
        mPatchManager.loadPatch();
    }

    /**
     * 获取版本名
     * @author leibing
     * @createTime 2016/09/10
     * @lastModify 2016/09/10
     * @param context 上下文
     * @return
     */
    private static String getVersionName(Context context){
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 加载补丁
     * @author leibing
     * @createTime 2016/09/10
     * @lastModify 2016/09/10
     * @param
     * @return
     */
    public static void loadPatch() {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + FileDownLoaderUtils.DOWN_DIR + JIANKE_PATCHES;
        FileUtils.isDirExist(dirPath);
        String patchFileStr = dirPath + APATCH_NAME;
        try {
            File f = new File(patchFileStr);
            if (f.exists() && mPatchManager != null) {
                // 移除所有包
                mPatchManager.removeAllPatch();
                // 加载指定包
                mPatchManager.addPatch(patchFileStr);
                // 复制且加载补丁成功后，删除下载的补丁
                f.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
