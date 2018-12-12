package com.liweidong.basemvc.http;

/**
 * Created by Administrator on 2018/12/12.
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
}
