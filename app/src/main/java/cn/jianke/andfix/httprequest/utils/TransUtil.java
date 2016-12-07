package cn.jianke.andfix.httprequest.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import cn.jianke.andfix.commons.StringUtil;
import cn.jianke.andfix.httprequest.InterfaceParameters;

/**
 * @className: TransUtils
 * @classDescription: 接口请求工具类
 * @author: leibing
 * @createTime: 2016/08/30
 */
public class TransUtil {
	// 添加json标签名称 add by leibing 2016/11/03
	public final static String JK_JSON_LIST = "jkjsonlist";
	// 返回json第一层数据是否存在json数组 add by leibing 2016/11/03
	public static boolean isJsonListData = false;
	// 返回的状态码被封装在msg中，即msg是一个对象 add by luchaoyue 2016/11/21
	public static boolean msgIsObject = false;

	/**
	 * 将TransData转出为JSON字符串
	 * @author leibing
	 * @createTime 2016/08/30
	 * @lastModify 2016/08/30
	 * @param map 参数名-值
	 * @return JSON数据
	 */
    public static String listToJson(Map<String,String> map ){
		JSONObject json = new JSONObject();
		try{
			for(Map.Entry<String,String> entry : map.entrySet()){
				 json.put(entry.getKey(), entry.getValue());
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
		return json.toString();
	}

	/**
	 * 改造json数据，增加一层标签
	 * @author leibing
	 * @createTime 2016/11/3
	 * @lastModify 2016/11/3
	 * @param jsonStr 改造前json字符串
	 * @return jsonObject.toString 改造后json字符串
	 */
	public static String remakeJsonData(String jsonStr) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JK_JSON_LIST, new JSONObject(jsonStr));
		return jsonObject.toString();
	}

	/**
	 * 改造json数组数据(兼容data数组标签下的数据不规范)
	 * @author leibing
	 * @createTime 2016/11/16
	 * @lastModify 2016/11/16
	 * @param jsonStr 改造前数据
	 * @return
	 */
	public static String remakeJsonArrayData(String jsonStr) throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonStr);
		if (StringUtil.isNotEmpty(jsonObject.optString(InterfaceParameters.DATA))){
			jsonObject.put(InterfaceParameters.DATA,
					new JSONArray(jsonObject.optString(InterfaceParameters.DATA)));
		}
		return jsonObject.toString();
	}

	/**
	 * 将JSON字符串转成TransData
	 * @author leibing
	 * @createTime 2016/08/30
	 * @lastModify 2016/08/30
	 * @param result json字符
	 * @return TransData
	 */
    public static TransData getResponse(String result){
		TransData response = null;
		if( StringUtil.isNotEmpty(result) ){
			try{			
				response = new TransData();
				JSONObject jsonObject = new JSONObject(result);
				// 如果json第一层数据存在json数组则改造数据，给数据添加一级标签 add by leibing 2016/11/03
				if (isJsonListData){
					// 设置状态 add by leibing 2016/11/03
					if (StringUtil.isNotEmpty(jsonObject.optString(InterfaceParameters.STATUS)))
						response.setStatus(jsonObject.optString(InterfaceParameters.STATUS));
					// 设置信息 add by leibing 2016/11/10
					if (StringUtil.isNotEmpty(jsonObject.optString(InterfaceParameters.MESSAGE)))
						response.setErrormsg(jsonObject.optString(InterfaceParameters.MESSAGE));
					// 设置信息 add by leibing 2016/11/16
					if (StringUtil.isNotEmpty(jsonObject.optString(InterfaceParameters.MSG)))
						response.setErrormsg(jsonObject.optString(InterfaceParameters.MSG));
					// 兼容data标签下数组非json数据 add by leibing 2016/11/16
					result = remakeJsonArrayData(result);
					// 改造后json字符串
					String remakeStr = remakeJsonData(result);
					JSONObject remakeJsonObject = new JSONObject(remakeStr);
					if (StringUtil.isNotEmpty(remakeJsonObject.optString(JK_JSON_LIST))) {
						response.setJkjsonlist(remakeJsonObject.optString(JK_JSON_LIST));
					}
					return response;
				}
				// 如果msg字段是一个对象，则改造数据 add by luchaoyue 2016/11/21
				if (msgIsObject){
					JSONObject object = jsonObject.optJSONObject(InterfaceParameters.MSG);
					if (object != null){
						// 设置状态 add by luchaoyue 2016/11/21
						if (StringUtil.isNotEmpty(object.optString(InterfaceParameters.CODE)))
							response.setStatus(object.optString(InterfaceParameters.CODE));
						// 设置数据 add by luchaoyue 2016/11/21
						response.setData(jsonObject.optString(InterfaceParameters.DATA));
					}
				}
				if (StringUtil.isNotEmpty(jsonObject.optString(InterfaceParameters.ERROR_CODE))) {
					response.setErrorcode(jsonObject.optString(InterfaceParameters.ERROR_CODE));
					response.setInfo(jsonObject.optString(InterfaceParameters.INFO));
					response.setErrormsg(jsonObject.optString(InterfaceParameters.ERROR_MSG));
				}else if (StringUtil.isNotEmpty(jsonObject.optString(InterfaceParameters.CODE))){
					response.setStatus(jsonObject.optString(InterfaceParameters.STATUS));
					response.setData(jsonObject.optString(InterfaceParameters.DATA));
					response.setCode(jsonObject.optString(InterfaceParameters.CODE));
					response.setMsg(jsonObject.optString(InterfaceParameters.MSG));
				}else if (StringUtil.isNotEmpty(jsonObject.optString(InterfaceParameters.RESULT))){
					response.setResult(jsonObject.optString(InterfaceParameters.RESULT));
				}else if (StringUtil.isNotEmpty(jsonObject.optString(InterfaceParameters.STATUS))){
					// 兼容热修复 add by leibing 2016/12/07
					response.setStatus(jsonObject.optString(InterfaceParameters.STATUS));
					response.setData(jsonObject.optString(InterfaceParameters.DATA));
					response.setMsg(jsonObject.optString(InterfaceParameters.MSG));
				}
            }catch(Exception e){
				e.printStackTrace();
			}
		}		
		return response;
	}
}
