package cn.jianke.andfix.httprequest;

import android.text.TextUtils;
import com.google.gson.Gson;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import cn.jianke.andfix.commons.StringUtil;
import cn.jianke.andfix.httprequest.httpresponse.BaseResponse;
import cn.jianke.andfix.httprequest.utils.TransData;
import cn.jianke.andfix.httprequest.utils.TransUtil;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @className: JkApiConvertFactory
 * @classDescription: this converter decode the response.
 * @author: leibing
 * @createTime: 2016/8/30
 */
public class JkApiConvertFactory extends Converter.Factory{

    public static JkApiConvertFactory create() {
        return create(new Gson());
    }

    public static JkApiConvertFactory create(Gson gson) {
        return new JkApiConvertFactory(gson);
    }

    private JkApiConvertFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new GsonResponseBodyConverter<>(type);
    }

    final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Type type;

        GsonResponseBodyConverter(Type type) {
            this.type = type;
        }

        @Override public T convert(ResponseBody value) throws IOException {
            BaseResponse baseResponse;
            String reString;
            try {
                reString = value.string();
                // 解析Json数据返回TransData对象
                TransData transData = TransUtil.getResponse(reString);
                try {
                    // 如果返回json第一层数据存在json数组，则做此兼容处理 add by leibing 2016/11/03
                    if (TransUtil.isJsonListData) {
                        TransUtil.isJsonListData = false;
                        if (!TextUtils.isEmpty(transData.getJkjsonlist())) {
                            baseResponse = new Gson().fromJson(transData.getJkjsonlist(), type);
                            baseResponse.setSuccess(transData.getStatus().equals("0"));
                            baseResponse.setErrormsg(transData.getErrormsg());
                            baseResponse.setData(transData.getJkjsonlist());
                        } else {
                            baseResponse = (BaseResponse) StringUtil.getObject(((Class) type)
                                    .getName());
                            baseResponse.setSuccess(transData.getStatus().equals("0"));
                            baseResponse.setErrormsg(transData.getErrormsg());
                            baseResponse.setData(transData.getData());
                        }
                        return (T) baseResponse;
                    }
                    // 兼容处方签返回数据 add by luchaoyue 2016/11/21
                    if (TransUtil.msgIsObject){
                        TransUtil.msgIsObject = false;
                        if (transData.getStatus().equals("0") && !TextUtils.isEmpty(transData.getData())){
                            baseResponse = new Gson().fromJson(transData.getData(), type);
                            baseResponse.setSuccess(transData.getStatus().equals("0"));
                            baseResponse.setMsg(transData.getMsg());
                            baseResponse.setData(transData.getData());
                        } else {
                            baseResponse = (BaseResponse) StringUtil.getObject(((Class) type)
                                    .getName());
                            baseResponse.setSuccess(transData.getStatus().equals("0"));
                            baseResponse.setMsg(transData.getMsg());
                            baseResponse.setData(transData.getData());
                        }
                    }

                    // 兼容资讯详情返回数据 add by leibing 2016/11/02
                    if (StringUtil.isNotEmpty(transData.getResult())){
                        baseResponse = (BaseResponse) StringUtil.getObject(((Class) type).getName());
                        baseResponse.setSuccess(transData.getResult().equals("1"));
                        return (T) baseResponse;
                    }
                    if (StringUtil.isEmpty(transData.getErrorcode())){
                        if (transData.getStatus().equals("0") && !TextUtils.isEmpty(transData.getData())){
                            baseResponse = new Gson().fromJson(transData.getData(), type);
                            baseResponse.setSuccess(transData.getStatus().equals("0"));
                            baseResponse.setMsg(transData.getMsg());
                            baseResponse.setData(transData.getData());
                        } else {
                            baseResponse = (BaseResponse) StringUtil.getObject(((Class) type)
                                    .getName());
                            baseResponse.setSuccess(transData.getStatus().equals("0"));
                            baseResponse.setMsg(transData.getMsg());
                            baseResponse.setData(transData.getData());
                        }
                    }else if(StringUtil.isNotEmpty(transData.getStatus())){
                        // 兼容热修复 add by leibing 2016/12/07
                        if (transData.getStatus().equals("2001") && !TextUtils.isEmpty(transData.getData())){
                            baseResponse = new Gson().fromJson(transData.getData(), type);
                            baseResponse.setSuccess(transData.getStatus().equals("2001"));
                            baseResponse.setMsg(transData.getMsg());
                            baseResponse.setData(transData.getData());
                        }else {
                            baseResponse = (BaseResponse) StringUtil.getObject(((Class) type)
                                    .getName());
                            baseResponse.setSuccess(transData.getStatus().equals("2001"));
                            baseResponse.setMsg(transData.getMsg());
                            baseResponse.setData(transData.getData());
                        }
                    }else {
                        if (transData.getErrorcode().equals("0") && !TextUtils.isEmpty(transData.getInfo())) {
                            baseResponse = new Gson().fromJson(transData.getInfo(), type);
                            baseResponse.setSuccess(transData.getErrorcode().equals("0"));
                            baseResponse.setErrormsg(transData.getErrormsg());
                            baseResponse.setInfo(transData.getInfo());
                        } else {
                            baseResponse = (BaseResponse) StringUtil.getObject(((Class) type)
                                    .getName());
                            baseResponse.setSuccess(transData.getErrorcode().equals("0"));
                            baseResponse.setErrormsg(transData.getErrormsg());
                            baseResponse.setInfo(transData.getInfo());
                        }
                    }
                    return (T) baseResponse;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //从不返回一个空的Response.
            baseResponse = (BaseResponse) StringUtil.getObject(((Class) type).getName());
            try {
                baseResponse.setSuccess(false);
                //JkApiConvertFactory can not recognize the response!
                baseResponse.setErrormsg("");
                baseResponse.setMsg("");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return (T)baseResponse;
            }
        }
    }
}
