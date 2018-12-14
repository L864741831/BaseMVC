package com.liweidong.basemvc.http;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * Created by Administrator on 2018/12/14.
 */

public class BaseFileCallbackListener<T> implements FileCallbackListener<T> {

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void callbackSuccess(String url, T file) {
        if (file instanceof File) {
            File file2 = (File) file;
            Logger.w("文件下载成功" + "\n" + url + "\n" + ((File) file).getPath());
        }
    }

    /**
     * 访问失败
     */
    public void onFaliure(String url, int statusCode, String content, Throwable error) {
        Logger.e("下载文件失败" + url + "\n" + statusCode + "\n" + content + "\n" + error);
    }

}
