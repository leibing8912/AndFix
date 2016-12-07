package cn.jianke.andfix.data.cache;

import java.io.Serializable;

/**
 * @className:HotFixCache
 * @classDescription: 热修复缓存
 * @author: leibing
 * @createTime: 2016/12/7
 */
public class HotFixCache implements Serializable{
    // 序列号，用于反序列化
    private static final long serialVersionUID = 729026500606118692L;
    // 热更新时间
    public long hotFoxTime = -1;
    // 热更新包文件名
    public String fileName = "";
    // 热更新包的url
    public String url = "";
    // 热更新上传时间
    public String uploadTime = "";
}
