package com.liweidong.basemvc.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liweidong.basemvc.R;
import com.liweidong.basemvc.activitydemo.LogDownloadListener;
import com.liweidong.basemvc.base.BaseRecyclerAdapter;
import com.liweidong.basemvc.model.ApkModel;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;

import java.io.File;

/**
 * Created by Administrator on 2018/12/19.
 */

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name;
    public TextView priority;
    public ImageView icon;
    public Button download;

    private ApkModel apk;

    Context context;
    BaseRecyclerAdapter adapter;

    private String tag;

    public ViewHolder(View itemView, Context context, BaseRecyclerAdapter adapter) {
        super(itemView);

        this.context = context;
        this.adapter = adapter;


        name = (TextView) itemView.findViewById(R.id.name);
        priority = (TextView) itemView.findViewById(R.id.priority);
        icon = (ImageView) itemView.findViewById(R.id.icon);
        download = (Button) itemView.findViewById(R.id.download);

    }

    public void bind(ApkModel apk) {
        this.apk = apk;
        if (OkDownload.getInstance().getTask(apk.url) != null) {
            download.setText("已在队列");
            download.setEnabled(false);
        } else {
            download.setText("下载");
            download.setEnabled(true);
        }
        priority.setText(String.format("优先级：%s", apk.priority));
        name.setText(apk.name);

        //displayImage(apk.iconUrl, icon);

        Glide.with(context)//
                .load(apk.iconUrl)//
                .error(R.mipmap.ic_launcher)//
                .into(icon);

        itemView.setOnClickListener(this);

        download.setOnClickListener(this);
    }


    public void onClick(View v) {
/*        Intent intent = new Intent(context, DesActivity.class);
        intent.putExtra("apk", apk);
        context.startActivity(intent);*/
        switch (v.getId()) {
            case R.id.download:

            //这里只是演示，表示请求可以传参，怎么传都行，和okgo使用方法一样
            GetRequest<File> request = OkGo.<File>get(apk.url)//
                    .headers("aaa", "111")//
                    .params("bbb", "222");

            //这里第一个参数是tag，代表下载任务的唯一标识，传任意字符串都行，需要保证唯一,我这里用url作为了tag
            OkDownload.request(apk.url, request)//
                    .priority(apk.priority)//
                    .extra1(apk)//
                    .save()//
                    .register(new LogDownloadListener())//
                    .start();

            adapter.notifyDataSetChanged();

            break;
        }
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }


    public void refresh(Progress progress) {

        name.setText(progress.fraction+"");

    }

}
