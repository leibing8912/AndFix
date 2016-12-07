package cn.jianke.andfix.httprequest.utils;

/**
 * @className : TransData
 * @classDescription : Http请求返回信息类
 * @author : leibing
 * @createTime : 2016/08/30
 */
public class TransData {
	// 错误码  errorcode.equals("0") 成功 or 失败
	public String errorcode = "";
	// 数据
	public String info = "";
	// 错误消息
	public String errormsg = "";

	// 状态
	public String status = "";
	// 数据
	public String data = "";
	// 返回码
	public String code = "";
	// 消息
	public String msg = "";

	// 第一级数据为json数组，给其添加一个标签 add by leibing 2016/11/03
	public String jkjsonlist = "";
	// 结果
	public String result = "";

	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public String getJkjsonlist() {
		return jkjsonlist;
	}

	public void setJkjsonlist(String jkjsonlist) {
		this.jkjsonlist = jkjsonlist;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
