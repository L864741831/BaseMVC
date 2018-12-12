package com.liweidong.basemvc.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;

import java.util.List;

/**
 * Created by Administrator on 2018/12/12.
 */

public class OkGoHttpUtil {

    public static void doGet(Context context, String url, MyParams params, boolean showProgressDialog, String loadingText, HttpCallbackListener callbackListener) {

/*        if (!NetWorkUtil.isNetworkAvailable(context)) {
            // 没有网络时可用
            if (callbackListener != null) {
                callbackListener.callbackNoNetwork();
            }
            return;
        }*/


        GetRequest request;
        request = OkGo.get(url)
                .tag(context);

        // 添加请求参数
        addGetParams(request,params);

        // 发送请求
        request.execute(new ElementCallback(context, url, showProgressDialog, loadingText, callbackListener));
    }

    /**
     * 添加get请求参数
     *
     * @param request
     * @param params
     */
    private static void addGetParams(GetRequest request,MyParams params){
        if (params != null && !params.isEmpty()) {
            List<KeyValue> list = params.getParamsList();
            if (list != null && !list.isEmpty()) {
                for (KeyValue item : list) {
                    if (item.value instanceof String) {
                        request.params(item.key, (String) item.value);
                    } else if (item.value instanceof Integer) {
                        request.params(item.key, (Integer) item.value);
                    } else if (item.value instanceof Double) {
                        request.params(item.key, (Double) item.value);
                    } else if (item.value instanceof Float) {
                        request.params(item.key, (Float) item.value);
                    }else if (item.value instanceof Boolean) {
                        request.params(item.key, (Boolean) item.value);
                    }
                }
            }
        }
    }

    private static class ElementCallback extends AbsCallback<Element>{

        private Context context;
        private String url;// 接口的url路径
        private boolean showProgressDialog;
        private String loadingText;
        private HttpCallbackListener callbackListener;

        public ElementCallback(Context context, String url,  boolean showProgressDialog, String loadingText, HttpCallbackListener callbackListener){
            this.context = context;
            this.url = url;
            this.showProgressDialog = showProgressDialog;
            this.loadingText = TextUtils.isEmpty(loadingText) ? "加载中……" : loadingText;
            this.callbackListener = callbackListener;
            if (callbackListener != null && callbackListener instanceof BaseHttpCallbackListener) {
                // 设置context
                BaseHttpCallbackListener baseHttpCallbackListener = (BaseHttpCallbackListener) callbackListener;
                baseHttpCallbackListener.setContext(context);
            }
        }

        /**
         * 对返回数据进行操作的回调， UI线程
         */
        @Override
        public void onSuccess(Response<Element> response) {

            Log.i("123========","123========");

            Log.i("123========",response.body().results);

            if(response.body().error == false){
                Log.i("========",response.body().results);

                if (callbackListener != null) {
                    callbackListener.callbackSuccess(url, response.body());
                }

            }
        }

        /*
        拿到响应后，将数据转换成需要的格式，子线程中执行，可以是耗时操作
         */
        @Override
        public Element convertResponse(okhttp3.Response response) throws Throwable {
            String str = response.body().string();
            Log.i("========",str);

            return JSON.parseObject(str, Element.class);
        }

    }

}
