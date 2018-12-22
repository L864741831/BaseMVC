package com.liweidong.basemvc.activitydemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.liweidong.basemvc.R;
import com.liweidong.basemvc.adapter.DownloadListAdapter;
import com.liweidong.basemvc.model.ApkModel;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/18.
 */

public class DownloadListActivity extends AppCompatActivity {

    //显示下载路径
    TextView targetFolder;
    //开始全部
    Button startAll;

    //下载任务列表
    RecyclerView recyclerView;
    //下载任务集合
    private List<ApkModel> apks;
    //下载任务集合
    DownloadListAdapter adapter;

    //OkDownload是对OkGo功能的一个扩展升级
    private OkDownload okDownload;



    private static final int REQUEST_PERMISSION_STORAGE = 0x01;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_list);

        targetFolder = (TextView)findViewById(R.id.targetFolder);
        startAll = (Button)findViewById(R.id.startAll);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        //初始化下载集合
        initData();
        //OkDownload全局配置
        okDownload = OkDownload.getInstance();
        //   默认路径为    /download/
        String path = Environment.getExternalStorageDirectory().getPath()+"/aba/";

        okDownload.setFolder(path); //设置全局下载路径 支持设置全局下载文件夹，以后每个任务如果不设置文件夹，将默认用这个路径，如果不设置，默认就是图中的目录（/storage/emulated/0/download/）。
        okDownload.getThreadPool().setCorePoolSize(3);  //可以设置同时下载的任务数量，如果不设置，默认就是3个，改方法只在第一次调用时生效，以后无效

                            /*
                    OkDownload不仅有全局相关的设置，还有对全部任务的同时操作能力。

                    startAll()：开始所有任务，或者继续下载所有暂停的任务都是这个方法
pauseAll()：将全部下载中的任务暂停
removeAll()：移除所有任务，无论这个任务是在下载中、暂停、完成还是其他任何状态，都可以直接移除这个任务，他有一个重载方法，接受一个boolen参数，true表示删除任务的时候同时删除文件，false表示只删除任务，但是文件保留在手机上。不传的话，默认为false，即不删除文件。
removeTask()：根据tag移除任务
getTaskMap()：获取当前所有下载任务的map
getTask()：根据tag获取任务
hasTask()：标识为tag的任务是否存在
                     */

        targetFolder.setText(String.format("下载路径: %s", OkDownload.getInstance().getFolder()));

        /*
        recyclerView动画和布局
         */
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        /*
        DownloadManager类，这个类是对下载任务的数据库进行增删改查的管理类，切记不要自己修改这里面的数据，
        不要直接使用DownloadManager的api，而应该使用OkDownload的api，这样才能确保数据的完整性和准确性。
         */
        //从数据库中恢复数据
        //从数据库取出所有任务url(tag)和状态
        //这个类的方法一般还需要和OkDownload的方法配合一下使用，一般用法就是获取数据后，将数据库的集合数据恢复成下载任务数据
        //分别表示从数据库中获取所有的下载记录，已完成的下载记录和未完成的下载记录。
        List<Progress> progressList = DownloadManager.getInstance().getAll();
        //将URL（tag）和状态存入OkDownload类的taskMap所有任务集合
        List<DownloadTask> tasks =  OkDownload.restore(progressList);

        /*
        启动任务，我们已经得到了DownloadTask任务对象，那么简单调用start启动他就好了，同时他还支持这么几个方法：
start()：开始一个新任务，或者继续下载暂停的任务都是这个方法
pause()：将一个下载中的任务暂停
remove()：移除一个任务，无论这个任务是在下载中、暂停、完成还是其他任何状态，都可以直接移除这个任务，他有一个重载方法，接受一个boolen参数，true表示删除任务的时候同时删除文件，false表示只删除任务，但是文件保留在手机上。不传的话，默认为false，即不删除文件。
restart()：重新下载一个任务。重新下载会先删除以前的任务，同时也会删除文件，然后从头开始重新下载该文件。
         */
        adapter = new DownloadListAdapter(this,apks);
        recyclerView.setAdapter(adapter);

        checkSDCardPermission();
    }

    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void cick(View v){
        switch (v.getId()){
            case R.id.startAll:

                for (ApkModel apk : apks) {

                    //这里只是演示，表示请求可以传参，怎么传都行，和okgo使用方法一样
                    GetRequest<File> request = OkGo.<File>get(apk.url)//
                            .headers("aaa", "111")//
                            .params("bbb", "222");

                    //这里第一个参数是tag，代表下载任务的唯一标识，传任意字符串都行，需要保证唯一,我这里用url作为了tag
                    OkDownload.request(apk.url, request)//request()：静态方法创建DownloadTask对象，接受两个参数，第一个参数是tag，表示当前任务的唯一标识，就像介绍中说的，所有下载任务按照tag区分，不同的任务必须使用不一样的tag，否者断点会发生错乱，如果相同的下载url地址，如果使用不一样的tag，也会认为是两个下载任务，不同的下载url地址，如果使用相同的tag，也会认为是同一个任务，导致断点错乱。切记，切记！！
                            .priority(apk.priority)//priority()：表示当前任务的下载优先级，他是一个int类型的值，只要在int的大小范围内，数值越大，优先级越高，也就会优先下载。当然也可以不设置，默认优先级为0，当所有任务优先级都一样的时候，就会按添加顺序下载。
                            .extra1(apk)//extra()：这个方法相当于数据库的扩展字段，我们知道我们断点下载是需要保存下载进度信息的，而我们这个框架是保存在数据库中，数据库的字段都是写死的，如果用户想在我们的下载数据库中保存自己的数据就做不到了，所以我们这里提供了三个扩展字段，允许用户保存自己想要的数据，如果不需要的话，也不用调用该方法。
                            .save()//如果你是第一次下载这个标识为这个tag的任务，那么你一定要调用save()方法，先将该数据写入数据库。
                            .register(new LogDownloadListener())//如果你对标识为tag的任务进行了一些参数的修改，比如修改了extra数据，修改了下载目录，下载文件夹等等，也必须调用save()方法，更新数据库。
                            .start();


                    /*
                    floder()：单独指定当前下载任务的文件夹目录，如果你是6.0以上的系统，记得下载的时候先自己获取sd卡的运行时权限，否则文件夹创建不成功，无法下载。当然也可以不指定，默认下载路径/storage/emulated/0/download。
                    fileName()：手动指定下载的文件名，一般来说是不需要手动指定的，也建议不要自己指定，除非你明确知道你要下载的是什么，或者你想改成你自己的文件名。如果不指定，文件名将按照以下规则自动获取，
                    register()：这是个注册监听的方法，我们既然要下载文件，那么我们肯定要知道下载的进度和状态是吧，就在这里注册我们需要的监听，监听可以注册多个，同时生效，当状态发生改变的时候，每个监听都会收到通知。当然如果你只是想下载一个文件，不关心他的回调，那么你不用注册任何回调。
                     */

                    /*
                    我们都知道，网络请求是个长时间的事情，如果你在下载过程中，关闭了Activity，而你的回调又还在这个页面中更新UI，那么久会发生内存泄露

                    如果你要实现页面进度的实时刷新，那么该监听就只需要在当前页面有效就行了，关闭页面之前，记得把监听移除，这样就不会泄露了，代码如下，我们看到了取消监听的时候需要传递一个tag，那么这个tag是你创建监听的时候传递的那个tag，不是创建下载任务的那个task，不要搞混了。监听是监听的，任务是任务的。

                     有人问了，我要是取消了，我想在其他页面，其他地方监听到这个下载任务怎么办？你当然可以继续注册一个监听，这个监听里面不要干与UI相关的事，不要持有Activity的引用，那么你这个监听是不会造成泄露的。

                    如果你还不放心，你可以自己额外起一个service，在service中下载任务，注册监听，这样就不怕泄露了。
                     */
                    adapter.notifyDataSetChanged();
                }

                break;
        }
    }

    /** 检查SD卡权限 */
    protected void checkSDCardPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //获取权限
            } else {
                Toast.makeText(DownloadListActivity.this,"权限被禁止，无法下载文件！",Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void initData() {
        apks = new ArrayList<>();
        ApkModel apk1 = new ApkModel();
        apk1.name = "爱奇艺";
        apk1.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0c10c4c0155c9adf1282af008ed329378d54112ac";
        apk1.url = "http://121.29.10.1/f5.market.mi-img.com/download/AppStore/0b8b552a1df0a8bc417a5afae3a26b2fb1342a909/com.qiyi.video.apk";
        apks.add(apk1);
        ApkModel apk2 = new ApkModel();
        apk2.name = "微信";
        apk2.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/00814b5dad9b54cc804466369c8cb18f23e23823f";
        apk2.url = "http://116.117.158.129/f2.market.xiaomi.com/download/AppStore/04275951df2d94fee0a8210a3b51ae624cc34483a/com.tencent.mm.apk";
        apks.add(apk2);
        ApkModel apk3 = new ApkModel();
        apk3.name = "新浪微博";
        apk3.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/01db44d7f809430661da4fff4d42e703007430f38";
        apk3.url = "http://60.28.125.129/f1.market.xiaomi.com/download/AppStore/0ff41344f280f40c83a1bbf7f14279fb6542ebd2a/com.sina.weibo.apk";
        apks.add(apk3);
        ApkModel apk4 = new ApkModel();
        apk4.name = "QQ";
        apk4.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/072725ca573700292b92e636ec126f51ba4429a50";
        apk4.url = "http://121.29.10.1/f3.market.xiaomi.com/download/AppStore/0ff0604fd770f481927d1edfad35675a3568ba656/com.tencent.mobileqq.apk";
        apks.add(apk4);
        ApkModel apk5 = new ApkModel();
        apk5.name = "陌陌";
        apk5.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/06006948e655c4dd11862d060bd055b4fd2b5c41b";
        apk5.url = "http://121.18.239.1/f4.market.xiaomi.com/download/AppStore/096f34dec955dbde0597f4e701d1406000d432064/com.immomo.momo.apk";
        apks.add(apk5);
        ApkModel apk6 = new ApkModel();
        apk6.name = "手机淘宝";
        apk6.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/017a859792d09d7394108e0a618411675ec43f220";
        apk6.url = "http://121.29.10.1/f3.market.xiaomi.com/download/AppStore/0afc00452eb1a4dc42b20c9351eacacab4692a953/com.taobao.taobao.apk";
        apks.add(apk6);
        ApkModel apk7 = new ApkModel();
        apk7.name = "酷狗音乐";
        apk7.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0f2f050e21e42f75c7ecca55d01ac4e5e4e40ca8d";
        apk7.url = "http://121.18.239.1/f5.market.xiaomi.com/download/AppStore/053ed49c1545c6eec3e3e23b31568c731f940934f/com.kugou.android.apk";
        apks.add(apk7);
        ApkModel apk8 = new ApkModel();
        apk8.name = "网易云音乐";
        apk8.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/02374548ac39f3b7cdbf5bea4b0535b5d1f432f23";
        apk8.url = "http://121.18.239.1/f4.market.xiaomi.com/download/AppStore/0f458c5661acb492e30b808a2e3e4c8672e6b55e2/com.netease.cloudmusic.apk";
        apks.add(apk8);
        ApkModel apk9 = new ApkModel();
        apk9.name = "ofo共享单车";
        apk9.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0fe1a5c6092f3d9fa5c4c1e3158e6ff33f6418152";
        apk9.url = "http://60.28.125.1/f4.market.mi-img.com/download/AppStore/06954949fcd48414c16f726620cf2d52200550f56/so.ofo.labofo.apk";
        apks.add(apk9);
        ApkModel apk10 = new ApkModel();
        apk10.name = "摩拜单车";
        apk10.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0863a058a811148a5174d9784b7be2f1114191f83";
        apk10.url = "http://60.28.125.1/f4.market.xiaomi.com/download/AppStore/00cdeb4865c5a4a7d350fe30b9f812908a569cc8a/com.mobike.mobikeapp.apk";
        apks.add(apk10);
        ApkModel apk11 = new ApkModel();
        apk11.name = "贪吃蛇大作战";
        apk11.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/09f7f5756d9d63bb149b7149b8bdde0769941f09b";
        apk11.url = "http://60.22.46.1/f3.market.xiaomi.com/download/AppStore/0b02f24ffa8334bd21b16bd70ecacdb42374eb9cb/com.wepie.snake.new.mi.apk";
        apks.add(apk11);
        ApkModel apk12 = new ApkModel();
        apk12.name = "蘑菇街";
        apk12.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0ab53044735e842c421a57954d86a77aea30cc1da";
        apk12.url = "http://121.29.10.1/f5.market.xiaomi.com/download/AppStore/07a6ee4955e364c3f013b14055c37b8e4f6668161/com.mogujie.apk";
        apks.add(apk12);
        ApkModel apk13 = new ApkModel();
        apk13.name = "聚美优品";
        apk13.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/080ed520b76d943e5533017a19bc76d9f554342d0";
        apk13.url = "http://121.29.10.1/f5.market.mi-img.com/download/AppStore/0e70a572cd5fd6a3718941328238d78d71942aee0/com.jm.android.jumei.apk";
        apks.add(apk13);
        ApkModel apk14 = new ApkModel();
        apk14.name = "全民K歌";
        apk14.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0f1f653261ff8b3a64324097224e40eface432b99";
        apk14.url = "http://60.28.123.129/f4.market.xiaomi.com/download/AppStore/04f515e21146022934085454a1121e11ae34396ae/com.tencent.karaoke.apk";
        apks.add(apk14);
        ApkModel apk15 = new ApkModel();
        apk15.name = "书旗小说";
        apk15.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0c9ce345aa2734b1202ddf32b6545d9407b18ba0b";
        apk15.url = "http://60.28.125.129/f5.market.mi-img.com/download/AppStore/02d9c4035b248753314f46600cf7347a306426dc1/com.shuqi.controller.apk";
        apks.add(apk15);
    }


}
