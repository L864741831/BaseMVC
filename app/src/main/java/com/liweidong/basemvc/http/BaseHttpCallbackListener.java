package com.liweidong.basemvc.http;

import android.content.Context;
import android.util.Log;


/**
 * Created by Administrator on 2018/12/12.
 */

public class BaseHttpCallbackListener<T> implements HttpCallbackListener<T>  {

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void callbackNoNetwork() {
        Log.i("info","没有网络连接");//打印返回数据
    }

    @Override
    public void callbackSuccess(String url, T element) {
        if (element instanceof Element) {
            Element element2 = (Element) element;
            Log.i("info",url+"\n"+element2.results);//打印返回数据
        }
    }


}
