package com.liweidong.basemvc.http;

/**
 *自定义回调接口
 */
public interface HttpCallbackListener<T> {

    /**
     * 无网络可用
     */
    void callbackNoNetwork();

    /**
     * 访问成功
     */
    void callbackSuccess(String url, T element);

    /**
     * 访问失败抛出异常
     */
    void onFaliure(String url, int statusCode, String content, Throwable error);

    /**
     * json格式错误
     */
    void callbackErrorJSONFormat(String url);

}
