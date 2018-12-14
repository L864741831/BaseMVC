package com.liweidong.basemvc.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.liweidong.basemvc.app.Constants;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;

import java.io.File;
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
/*                .headers("Versions","1.0.0") //版本名称*/
                .tag(context);

        // 添加请求参数
        addGetParams(request, params);

        // 发送请求
        request.execute(new ElementCallback(context, url, showProgressDialog, loadingText, callbackListener));
    }


    /**
     * @param context            上下文
     * @param url                请求地址
     * @param params             请求参数
     * @param showProgressDialog 是否显示加载对话框
     * @param loadingText        加载对话框显示文字
     * @param callbackListener   请求结果或错误回调实现类
     */
    public static void doPost(Context context, String url, MyParams params, boolean showProgressDialog, String loadingText, HttpCallbackListener callbackListener) {

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
        PostRequest request;
        request = OkGo.post(url)
/*                .headers("Versions","1.0.0") //版本名称*/
                .tag(context);

        // 添加请求参数
        addPostParams(request, params);

        // 发送请求
        request.execute(new ElementCallback(context, url, showProgressDialog, loadingText, callbackListener));
    }



    public static void download(Context context, String url, boolean showProgressDialog, String loadingText, FileCallbackListener callbackListener){

        /*
        FileCallback()：空参构造
FileCallback(String destFileName)：可以额外指定文件下载完成后的文件名
FileCallback(String destFileDir, String destFileName)：可以额外指定文件的下载目录和下载完成后的文件名

文件目录如果不指定,默认下载的目录为 sdcard/download/

         */
        OkGo.<File>get(url)
                .tag(context)
                .execute(new DownloadFileCallback(context,url,showProgressDialog,loadingText,callbackListener));
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
     * 添加post请求参数
     *
     * @param request
     * @param params
     */
    private static void addPostParams(PostRequest request, MyParams params) {
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
                    } else if (item.value instanceof File) {
                        request.params(item.key, (File) item.value);
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
/*            Log.i("========onSuccess", "onSuccess");*/
            Logger.w("onSuccess返回数据进行操作的回调" + "\n" + response.body().toString());

            if (response.body() == null) {
                Logger.e("解析JSON数据为空，请检查！！！");
                return;
            }

            if (response.body().code==2) {
                if (callbackListener != null) {
                    callbackListener.callbackSuccess(url, response.body());
                }
                return;
            }

            //这里为接口返回状态
            if (response.body().error == false) {
                if (callbackListener != null) {
                    callbackListener.callbackSuccess(url, response.body());
                }
                return;
            }



        }

        /*
        拿到响应后，将数据转换成需要的格式，子线程中执行，可以是耗时操作
         */
        @Override
        public Element convertResponse(okhttp3.Response response) throws Throwable {
            String str = response.body().string();
/*            Log.i("========convertResponse", str);*/
/*            Log.w("========convertResponse", "convertResponse");*/
            Logger.w("convertResponse拿到响应" + "\n" + str);

            return JSON.parseObject(str, Element.class);
        }

        /*
         请求网络开始前，UI线程
          */
        public void onStart(Request<Element, ? extends Request> request) {
            super.onStart(request);

/*            Log.w("========onStart", "onStart");*/
            Logger.w("onStart");

        }

        /*
         * 请求网络结束后，UI线程
         */
        public void onFinish() {
            super.onFinish();

/*            Log.w("========onFinish", "onFinish");*/
            Logger.w("onFinish");

        }

        /*
         * 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程
         */
        public void onError(Response<Element> response) {
            super.onError(response);
            Throwable e = response.getException();

            if (callbackListener != null) {
/*                Log.i("123========nFinish", e.getClass()+"");*/
                Logger.e("onError");
                //注意JSONException是com.alibaba.fastjson包下的;
                if (e.getClass() == JSONException.class) {
                    callbackListener.callbackErrorJSONFormat(url);
                }
                //isSuccessful()：本次请求是否成功，判断依据是是否发生了异常。
                if(!response.isSuccessful()) {
                    callbackListener.onFaliure(url, response.code(), response.message(), e);
                }

            }

        }


/*        @Override
        public void onCacheSuccess(Response<Element> response) {
            super.onCacheSuccess(response);

            Logger.w("onCacheSuccess读取缓存成功" + "\n" + response.body().toString());
        }*/


    }

    private static class DownloadFileCallback extends FileCallback{

        Context context;
        String url;
        boolean showProgressDialog;
        String loadingText;
        FileCallbackListener callbackListener;

        public DownloadFileCallback(Context context, String url, boolean showProgressDialog, String loadingText, FileCallbackListener callbackListener){
            this.context = context;
            this.url = url;
            this.showProgressDialog = showProgressDialog;
            this.loadingText = TextUtils.isEmpty(loadingText) ? "下载中……" : loadingText;
            this.callbackListener = callbackListener;
            if (callbackListener != null && callbackListener instanceof BaseFileCallbackListener) {
                // 设置context
                BaseFileCallbackListener baseFileCallbackListener = (BaseFileCallbackListener) callbackListener;
                baseFileCallbackListener.setContext(context);
            }
        }

        /** 对返回数据进行操作的回调， UI线程 */
        public void onSuccess(Response<File> response) {
            //file为文件数据
            Logger.w("onSuccess"+response.body().getPath());
            Log.i("downloadfile","文件路径"+response.body().getPath());

            callbackListener.callbackSuccess(url,response.body());
        }

        /** 下载过程中的进度回调，UI线程 */
        public void downloadProgress(Progress progress) {
            super.downloadProgress(progress);
            //这里回调下载进度(该回调在主线程，可以直接更新UI)
            Logger.w("downloadProgress"+progress.fraction);
            Logger.w("downloadProgress"+progress.fileName);

            Log.i("downloadfile","进度"+progress.fraction);
            Log.i("downloadfile","文件名"+progress.fileName);

        }

        /** 请求网络开始前，UI线程 */
        public void onStart(Request<File, ? extends Request> request) {
            super.onStart(request);
            Logger.w("onStart");
            Log.i("downloadfile","onStart");
        }

        /** 请求网络结束后，UI线程 */
        public void onFinish() {
            super.onFinish();
            Logger.w("onFinish");
            Log.i("downloadfile","onFinish");
        }

    }

}
