package cn.jianke.andfix.commons;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * @className: FileDownLoaderUtils
 * @classDescription: 文件下载器
 * @author: leibing
 * @createTime: 2016/12/1
 */
public class FileDownLoaderUtils {
    // 下载默认路径
    public final static String DOWN_DIR = "/download";
    // 单例
    private static FileDownLoaderUtils instance;
    // 下载器
    private DownloadManager downloadManager;
    // 下载id
    private long downLoadId;

    /**
     * 私有构造函数
     * @author leibing
     * @createTime 2016/12/1
     * @lastModify 2016/12/1
     * @param
     * @return
     */
    private FileDownLoaderUtils(){
    }

    /**
     * 单例
     * @author leibing
     * @createTime 2016/12/1
     * @lastModify 2016/12/1
     * @param
     * @return
     */
    public static FileDownLoaderUtils getInstance(){
        if (instance == null) {
            synchronized (FileDownLoaderUtils.class) {
                if (instance == null)
                    instance = new FileDownLoaderUtils();
            }
        }
        return instance;
    }

    /**
     * 下载文件
     * @author leibing
     * @createTime 2016/12/1
     * @lastModify 2016/12/1
     * @param context 上下文
     * @param fileUrl 下载文件远程地址
     * @param destinationDir 下载目录
     * @param fileName 下载文件名称
     * @return
     */
    public void downLoaderFile(Context context, String fileUrl,
                               String destinationDir, String fileName){
        System.out.println("dddddddddddddddddddd fileName = " + fileName);
//        try {
//            // 若上下文、下载文件远程地址、下载目录、下载文件名称均不为空则开始下载
//            if (context != null
//                    && StringUtil.isNotEmpty(fileUrl)
//                    && StringUtil.isNotEmpty(destinationDir)
//                    && StringUtil.isNotEmpty(fileName)){
//                downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
//                Uri uri = Uri.parse(fileUrl);
//                DownloadManager.Request request = new DownloadManager.Request(uri);
//                // 指定下载路径
//                request.setDestinationInExternalPublicDir(destinationDir, fileName);
//                // 设置Notification隐藏
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
//                // 下载id，可以用来查询用
//                downLoadId = downloadManager.enqueue(request);
//            }
//        }catch (Exception ex){
//        }
    }

    /**
     * 取消下载文件（如果一个下载被取消了，所有相关联的文件，部分下载的文件和完全下载的文件都会被删除）
     * @author leibing
     * @createTime 2016/12/1
     * @lastModify 2016/12/1
     * @param
     * @return
     */
    public void cancelDownFile(Context context){
        try {
            if (downloadManager == null){
                downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            }
            downloadManager.remove(downLoadId);
        }catch (Exception ex){
        }
    }
    
    /**
     *
     * @author leibing
     * @createTime 2016/12/1
     * @lastModify 2016/12/1
     * @param
     * @return
     */
    public boolean queryIsHasFileDownLoader(Context context, int downLoadId){
        try {
            if (downloadManager == null){
                downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            }
            DownloadManager.Query query = new DownloadManager.Query() ;
            query.setFilterById(downLoadId);
            Cursor cursor = downloadManager.query(query) ;
            if (!cursor.moveToFirst()){
                // 没有记录
                return false;
            } else {
                //有记录
                return true;
            }
        }catch (Exception ex){
        }
        return false;
    }
}
