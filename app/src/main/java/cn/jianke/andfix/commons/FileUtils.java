package cn.jianke.andfix.commons;

import android.os.Environment;
import java.io.File;

/**
 * @className: FileUtils
 * @classDescription: 文件工具类
 * @author: leibing
 * @createTime: 2016/09/10
 */
public class FileUtils {
    /**
     * 是否路径存在
     * @author leibing
     * @createTime 2016/09/10
     * @lastModify 2016/09/10
     * @param path
     * @return
     */
    public static boolean isDirExist(String path){
        if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
                return true;
            }
        }
        return false;
    }
}
