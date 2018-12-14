package com.liweidong.basemvc.http;

import java.io.File;

/**
 * Created by Administrator on 2018/12/14.
 */

public interface FileCallbackListener<T> {
    /**
     * 访问成功
     */
    void callbackSuccess(String url,T file);

    /**
     * 下载失败抛出异常
     */
    void onFaliure(String url, int statusCode, String content, Throwable error);
}
