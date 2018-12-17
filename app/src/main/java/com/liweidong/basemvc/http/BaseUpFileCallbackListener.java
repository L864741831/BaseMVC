package com.liweidong.basemvc.http;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * Created by Administrator on 2018/12/14.
 */

public class BaseUpFileCallbackListener implements UpFileCallbackListener {

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }


    @Override
    public void callbackSuccess(String url, String str) {
        Logger.w("文件上传成功" + "\n" + url + "\n" + str);
    }

    @Override
    public void onFaliure(String url, int statusCode, String content, Throwable error) {
        Logger.e("文件上传失败" + url + "\n" + statusCode + "\n" + content + "\n" + error);
    }
}
