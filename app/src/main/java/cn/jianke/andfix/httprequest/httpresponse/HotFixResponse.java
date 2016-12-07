package cn.jianke.andfix.httprequest.httpresponse;

import java.io.Serializable;

/**
 * @className: HotFixResponse
 * @classDescription: 热更新数据响应
 * @author: leibing
 * @createTime: 2016/12/7
 */
public class HotFixResponse implements Serializable{
    // 序列号，用于反序列化
    private static final long serialVersionUID = 1821551851133789722L;
    // 热更新包文件名
    public String fileName = "";
    // 热更新包的url
    public String url = "";
    // 热更新上传时间
    public String uploadTime = "";
}
