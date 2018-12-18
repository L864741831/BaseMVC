package com.liweidong.basemvc.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by Administrator on 2018/12/14.
 */

public class ApkUtil {
    /**
     * 描述：打开并安装文件.
     *
     * @param context the mContext
     * @param file    apk文件路径
     *
     *                7.0以上不能用
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
