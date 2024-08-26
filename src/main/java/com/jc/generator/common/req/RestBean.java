package com.jc.generator.common.req;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;

/**
 * 通用返回类
 * @param code
 * @param data
 * @param message
 * @param <T>
 */
public record RestBean<T>(int code, String message, T data) {
    //请求成功
    public static <T> RestBean<T> success(String message,T data){
        return new RestBean<>(200,message,data);
    }
    public static <T> RestBean<T> success(T data){
        return new RestBean<>(200,"请求成功",data);
    }
    //请求成功 无data
    public static <T> RestBean<T> success(){
        return RestBean.success(null);
    }

    //请求失败
    public  static <T> RestBean<T> failure(int code,String message){
        return new RestBean<>(code,message,null);
    }

    public  static <T> RestBean<T> failure(int code,String message,T data){
        return new RestBean<>(code,message,data);
    }
    //转化为JSON字符串
    public String asJsonString(){
        //JSONWriter.Feature.WriteNulls 防止null值错误
        return JSON.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }

}
