package com.complex.utils;

import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.HashMap;

/***
 * yyj
 * 2020/09/01
 * 赤湾
 * */
@Data
public class AJAXReturn extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    public static final String CODE_TAG = "code";
    public static final String MSG_TAG = "msg";
    public static final String Count_TAG = "count";
    public static final String DATA_TAG = "data";

    /**
     * state type
     * */
    public enum Type {
        /**
         * SUCCESS
         */
        SUCCESS(0),
        /**
         * WARN
         */
        WARN(2),
        /**
         * ERROR
         */
        ERROR(500),
        /**
         * UNAUTH*/
        UNAUTH(3),
        UNLOGIN(4);
        private final int value;
        Type(int value) {
            this.value = value;
        }
        public int value() {
            return this.value;
        }
    }

    /**
     * 状态类型
     */
    public Type type;

    /**
     * 状态码
     */
    public int code;

    /**
     * 返回内容
     */
    public String msg;

    /**
     * 数据对象
     */
    public Object data;
    /**
     * 初始化一个新创建的 AJAXReturn 对象，使其表示一个空消息。
     */
    public AJAXReturn() {
    }

    /**
     * 初始化一个新创建的 AJAXReturn 对象
     * @param type 状态类型
     * @param msg  返回内容
     */
    public AJAXReturn(Type type, String msg) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AJAXReturn 对象
     * @param type 状态类型
     * @param msg  返回内容
     * @param data 数据对象
     */
    public AJAXReturn(Type type, String msg, Object data) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        /* 数据为空的时候，还是需要把参数传给前台   huangqr @2019.7.19
        if (StringUtils.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }*/
        super.put(DATA_TAG, data);
    }
    public AJAXReturn(Type type, String msg, Object data, int count) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        super.put(Count_TAG, count);
        super.put(DATA_TAG, data);
    }
    /**
     * 返回成功消息
     * @return 成功消息
     */
    public static AJAXReturn success() {
        return AJAXReturn.success("successful operation ");
    }
    public static AJAXReturn success(Object data) {
        return AJAXReturn.success("successful operation", data);
    }
    public static AJAXReturn success(String msg) {
        return AJAXReturn.success(msg, null);
    }
    public static AJAXReturn success(String msg, Object data) {
        return new AJAXReturn(Type.SUCCESS, msg, data);
    }
    /**
     * 返回成功消息
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static AJAXReturn success(String msg, Object data,int count) {
        return new AJAXReturn(Type.SUCCESS, msg, data,count);
    }
    /**
     * 返回警告消息
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AJAXReturn warn(String msg) {
        return AJAXReturn.warn(msg, null);
    }

    /**
     * 返回警告消息
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AJAXReturn warn(String msg, Object data) {
        return new AJAXReturn(Type.WARN, msg, data);
    }

    /**
     * 返回错误消息
     * @return
     */
    public static AJAXReturn error() {
        return AJAXReturn.error("操作失败");
    }

    /**
     * 返回错误消息
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AJAXReturn error(String msg) {
        return AJAXReturn.error(msg, null);
    }

    /**
     * 返回错误消息
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AJAXReturn error(String msg, Object data) {
        return new AJAXReturn(Type.ERROR, msg, data);
    }

    /**
     * 无权限返回
     * @return
     */
    public static AJAXReturn unauth() {
        return new AJAXReturn(Type.UNAUTH, "您没有访问权限！", null);
    }
    /**
     * 无权限
     *
     * @param msg
     * @return com.wanda.labor.framework.web.domain.AJAXReturn
     * @exception
     */
    public static AJAXReturn unauth(String msg) {
        return new AJAXReturn(Type.UNAUTH, msg, null);
    }
    /**
     * 未登录或登录超时。请重新登录
     *
     * @param
     * @return com.wanda.labor.framework.web.domain.AJAXReturn
     * @exception
     */
    public static AJAXReturn unlogin() {
        return new AJAXReturn(Type.UNLOGIN, "未登录或登录超时。请重新登录！", null);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public static class SUCCESS{

        public static AJAXReturn data(Object data){
            return new AJAXReturn(Type.SUCCESS, "操作成功 Operation Successful", data);
        }

        public static AJAXReturn iMessagesg(String msg){
            return new AJAXReturn(Type.SUCCESS, msg, null);
        }

        public static AJAXReturn imsgAndData(String msg,Object data){
            return new AJAXReturn(Type.SUCCESS, msg, data);
        }
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("code", getCode())
                .append("msg", getMsg()).append("data", getData()).toString();
    }

}
