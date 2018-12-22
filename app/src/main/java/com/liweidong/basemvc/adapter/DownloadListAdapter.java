package com.liweidong.basemvc.adapter;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.liweidong.basemvc.R;
import com.liweidong.basemvc.adapter.viewholder.ViewHolder;
import com.liweidong.basemvc.base.BaseRecyclerAdapter;
import com.liweidong.basemvc.model.ApkModel;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;

import java.util.List;

/**
 * Created by Administrator on 2018/12/19.
 */

public class DownloadListAdapter extends BaseRecyclerAdapter<ApkModel, ViewHolder> {

    Context context;

    public DownloadListAdapter(Context context, List<ApkModel> datas) {
        super(context, datas);
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_download_list, parent, false);
        return new ViewHolder(view, context, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ApkModel apkModel = mDatas.get(position);



        holder.bind(apkModel);





        //Log.i("taggggg",apkModel.url);;
    }


}
