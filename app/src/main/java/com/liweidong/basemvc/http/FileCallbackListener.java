package com.liweidong.basemvc.http;

import java.io.File;

/**
 * Created by Administrator on 2018/12/14.
 */

public interface FileCallbackListener<T> {
    void callbackSuccess(String url,T file);
}
