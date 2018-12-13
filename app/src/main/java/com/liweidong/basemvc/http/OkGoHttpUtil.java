package com.liweidong.basemvc.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.liweidong.basemvc.app.Constants;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.base.Request;

import java.util.List;

/**
 * OkGo网络请求帮助类
 */
public class OkGoHttpUtil {

    /**
     * @param context            上下文
     * @param url                请求地址
     * @param params             请求参数
     * @param showProgressDialog 是否显示加载对话框
     * @param loadingText        加载对话框显示文字
     * @param callbackListener   请求结果或错误回调实现类
     */
    public static void doGet(Context context, String url, MyParams params, boolean showProgressDialog, String loadingText, HttpCallbackListener callbackListener) {

        if (!NetWorkUtil.isNetworkAvailable(context)) {
            // 没有网络时可用
            if (callbackListener != null) {
                callbackListener.callbackNoNetwork();
            }
            return;
        }

        /*
        OkGo实现get请求实现类
         */
        GetRequest request;
        request = OkGo.get(url)
                .tag(context);

        // 添加请求参数
        addGetParams(request, params);

        // 发送请求
        request.execute(new ElementCallback(context, url, showProgressDialog, loadingText, callbackListener));
    }

    /**
     * 添加get请求参数
     *
     * @param request
     * @param params
     */
    private static void addGetParams(GetRequest request, MyParams params) {
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
                    } else if (item.value instanceof Boolean) {
                        request.params(item.key, (Boolean) item.value);
                    }
                }
            }
        }
    }

    /**
     * ElementCallback实现AbsCallback回调接口
     */
    private static class ElementCallback extends AbsCallback<Element> {

        private Context context;
        private String url;// 接口的url路径
        private boolean showProgressDialog;
        private String loadingText;
        private HttpCallbackListener callbackListener;

        public ElementCallback(Context context, String url, boolean showProgressDialog, String loadingText, HttpCallbackListener callbackListener) {
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

/*            Log.i("========onSuccess", response.body().results);*/
            Log.i("========onSuccess", "onSuccess");

            //这里为接口返回状态
            if (response.body().error == false) {
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
/*            Log.i("========convertResponse", str);*/
            Log.i("========convertResponse", "convertResponse");


            return JSON.parseObject(str, Element.class);
        }

        /*
         请求网络开始前，UI线程
          */
        public void onStart(Request<Element, ? extends Request> request) {
            super.onStart(request);

/*            Log.i("========onStart", "显示对话框");*/
            Log.i("========onStart", "onStart");


        }

        /*
         * 请求网络结束后，UI线程
         */
        public void onFinish() {
            super.onFinish();

/*            Log.i("========onFinish", "关闭对话框");*/
            Log.i("========nFinish", "nFinish");

        }

        /*
         * 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程
         */
        public void onError(Response<Element> response) {
            super.onError(response);
            Throwable e = response.getException();

            if (callbackListener != null) {
                Log.i("123========nFinish", e.getClass()+"");
                //注意JSONException是com.alibaba.fastjson包下的;
                if (e.getClass() == JSONException.class) {
                    callbackListener.callbackErrorJSONFormat(url);
                } else {
                    callbackListener.onFaliure(url, Constants.HTTP_SERVER_ERROR_CODE, e.getMessage(), e);
                }
            }

        }


    }

}
