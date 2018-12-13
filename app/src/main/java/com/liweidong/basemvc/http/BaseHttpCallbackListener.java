package com.liweidong.basemvc.http;

import android.content.Context;
import android.util.Log;

import com.orhanobut.logger.Logger;


/**
 * 自定义回调接口实现类
 */
public class BaseHttpCallbackListener<T> implements HttpCallbackListener<T> {

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 无网络可用
     */
    public void callbackNoNetwork() {
/*        Log.i("=====callbackNoNetwork","没有网络连接");//打印返回数据*/
        Logger.e("没有网络连接");
    }

    /**
     * 访问成功
     */
    public void callbackSuccess(String url, T element) {
        if (element instanceof Element) {
            Element element2 = (Element) element;
/*            Log.i("=====callbackSuccess",url+"\n"+element2.results);//打印返回数据*/
            Logger.w("接口访问成功" + "\n" + url + "\n" + element2.toString());
        }
    }

    /**
     * 访问失败
     */
    public void onFaliure(String url, int statusCode, String content, Throwable error) {
/*        Log.i("=====onFaliure","接口访问失败"+url+"\n"+statusCode+"\n"+content+"\n"+error);//打印返回数据*/
        Logger.e("接口访问失败" + url + "\n" + statusCode + "\n" + content + "\n" + error);
    }

    /**
     * json格式错误
     */
    public void callbackErrorJSONFormat(String url) {
/*        Log.i("=====JSONFormat","json解析失败"+url);//打印返回数据*/
        Logger.e("json解析失败" + "\n" + url);
    }


}
