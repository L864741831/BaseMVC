/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.liweidong.basemvc.activitydemo;

import android.util.Log;
import android.widget.TextView;

import com.liweidong.basemvc.adapter.viewholder.ViewHolder;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.download.DownloadListener;

import java.io.File;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2017/6/7
 * 描    述：
 * 修订历史：
 * ================================================
 *
 * 服务端返回的响应码是206，注意是206，这个很重要，只有206才能实现断点下载，表示本次返回的是部分响应体，并不是全部的数据。
 *
 服务端一定要返回Content-Length，注意，是一定要返回Content-Length这个响应头，
 如果没有，该值默认是-1，这个值表示当前要下载的文件有多大，如果服务端不给的话，客户端在下载过程中是不可能知道我要下载的文件有多大的，所以常见的问题就是进度是负数。

 下载文件的时候，响应体会打印一句话：maybe [binary boby], emitted!，这句话表示当前的数据是二进制文件，控制台没法打印也没必要打印出来，所以不用打印了。
 */
public class LogDownloadListener extends DownloadListener {

    /*
    我们看到了取消监听的时候需要传递一个tag，那么这个tag是你创建监听的时候传递的那个tag，不是创建下载任务的那个task，不要搞混了。监听是监听的，任务是任务的。
     */

    //DownloadListener的构造方法需要传一个tag，这个tag唯一标识当前listener，主要目的是方便取消监听，同时可以防止数据错乱。
    public LogDownloadListener() {
        super("LogDownloadListener");
    }


    /*
    onStart()方法是在下载请求之前执行的，所以可以做一些请求之前相关的事，比如修改请求参数，加密，显示对话框等等。
     */
    public void onStart(Progress progress) {
        System.out.println("onStart: " + progress);
    }

    @Override
    public void onProgress(Progress progress) {
        System.out.println("onProgress: " + progress);

    }

    @Override
    public void onError(Progress progress) {
        System.out.println("onError: " + progress);
        progress.exception.printStackTrace();
    }

    /*
    下载完后，最后会调用onFinish()，不过我设计成在调用onFinish()之前，还会额外调用一次onProgress()方法，这样的好处可以在onProgress方法中捕获到所有的状态变化，方便管理。
     */
    public void onFinish(File file, Progress progress) {
        System.out.println("onFinish: " + progress);
    }

    @Override
    public void onRemove(Progress progress) {
        System.out.println("onRemove: " + progress);

    }
}
