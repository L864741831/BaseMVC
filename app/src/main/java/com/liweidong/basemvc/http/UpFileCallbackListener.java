package com.liweidong.basemvc.http;

/**
 * Created by Administrator on 2018/12/14.
 */

public interface UpFileCallbackListener {
    /**
     * 访问成功
     */
    void callbackSuccess(String url, String str);

    /**
     * 下载失败抛出异常
     */
    void onFaliure(String url, int statusCode, String content, Throwable error);

}
