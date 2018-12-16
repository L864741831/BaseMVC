package com.liweidong.basemvc.activitydemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.liweidong.basemvc.R;
import com.liweidong.basemvc.alipay.PayResult;
import com.liweidong.basemvc.http.BaseFileCallbackListener;
import com.liweidong.basemvc.http.BaseHttpCallbackListener;
import com.liweidong.basemvc.http.Element;
import com.liweidong.basemvc.http.MyParams;
import com.liweidong.basemvc.http.OkGoHttpUtil;
import com.liweidong.basemvc.utils.ApkUtil;
import com.lzy.okgo.OkGo;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

/*
http
 */
public class HttpActivity extends AppCompatActivity {

    TextView tv_result;

    private String orderInfo = "";
    private final int SDK_PAY_FLAG = 1;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();

                    JSONObject obj = null;//最外层的JSONObject对象
                    String out_trade_no_info = "";
                    try {
                        obj = new JSONObject(resultInfo);
                        JSONObject object = obj.getJSONObject("alipay_trade_app_pay_response");
                        out_trade_no_info = object.getString("out_trade_no");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        getAliPayState(out_trade_no_info);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(HttpActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

        ;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        tv_result = (TextView) findViewById(R.id.tv_result);
    }

    public void get(View v) {
        switch (v.getId()) {
            case R.id.btn_get:
                get();
                break;
            case R.id.btn_post:
                post();
                break;
            case R.id.btn_pay:
                pay();
                break;
            case R.id.btn_getfile:
                getFile();
                break;
        }
    }


    public void get() {
        //https://gank.io/api/data/%E7%A6%8F%E5%88%A9/1/1
        //https://gank.io/api/data/福利/1/1
        MyParams params = new MyParams();
/*                params.put("username", phone);
                params.put("password",pwd);*/
/*
https://gank.io/api/data/福利/1/1
 */

/*
 {"error":false,"results":[{"_id":"5bfe1a5b9d2122309624cbb7","createdAt":"2018-11-28T04:32:27.338Z","desc":"2018-11-28","publishedAt":"2018-11-28T00:00:00.0Z","source":"web","type":"\u798f\u5229","url":"https://ws1.sinaimg.cn/large/0065oQSqgy1fxno2dvxusj30sf10nqcm.jpg","used":true,"who":"lijinshanmx"}]}

 */
        OkGoHttpUtil.doGet(HttpActivity.this, "https://gank.io/api/data/福利/1/1", params, false, "", new BaseHttpCallbackListener<Element>() {
            @Override
            public void callbackSuccess(String url, Element element) {
                super.callbackSuccess(url, element);

/*                        Log.i("========",element.results);*/
                tv_result.setText(element.results);
            }
        });
    }

    public void post() {
/*             http://47.94.90.205
                /a/login

http://47.94.90.205/a/login

{username=200005,password=123456,mobileLogin=true,app=true,imei=863064010142216}


{"msg":"登录成功","code":2,"rows":{"photo":"https://tianyou001.oss-cn-
beijing.aliyuncs.com/photo/392f1d685f5843ab8271aa565b717e591530323433220.png","realName":"1","mobile"
:"18345200005","name":"200005","id":"d71b851c923546d9a412cbe7482add42","userType":"3","blood":"4","vi
pType":"0","birthday":"2018/12/11","no":"18345200005","sexId":"3"}}

                */
        MyParams params1 = new MyParams();
        params1.put("username", 200005);
        params1.put("password", 123456);
        params1.put("mobileLogin", true);
        params1.put("app", true);
        params1.put("imei", "863064010142216");


        OkGoHttpUtil.doPost(HttpActivity.this, "http://47.94.90.205/a/login", params1, false, "", new BaseHttpCallbackListener<Element>() {
            @Override
            public void callbackSuccess(String url, Element element) {
                super.callbackSuccess(url, element);

/*                        Log.i("========",element.results);*/
                tv_result.setText(element.rows);
            }
        });
    }

    public void pay() {
/*             http://47.94.90.205
                /a/login

http://47.94.90.205/a/login

{username=200005,password=123456,mobileLogin=true,app=true,imei=863064010142216}

{"code":"2","rows":"app_id=2018060760311802&biz_content=%7B%22body%22%3A%22%E5%A4%A9%E4%BD
%91%E5%85%85%E5%80%BC%22%2C%22out_trade_no%22%3A%2215444947092741316674329%22%2C%22product_code
%22%3A%22QUICK_MSECURITY_PAY%22%2C%22seller_id%22%3A%222088131308488370%22%2C%22subject%22%3A
%22%E5%A4%A9%E4%BD%91%E5%85%85%E5%80%BC%22%2C%22total_amount%22%3A%220.01%22%7D&charset=utf-
8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fty.tianjistar.com%2FAilPayResoult
%2Fresoult&sign=GCmHZfeCfXGfrmqAgVcRAIMUNHy1Gj%2BvJelnsiSHF8GvT290zrC6LV%2FF6ZdIsqTJm8hcSfivP0lq
%2BRVk5Zx%2BkWGow5cDvIZCRkkNa53E61dxSxTinBwbPPokuAVznuxnS06io2vS%2BJLJBN
%2FHm3EvI4O1BSO9RZbrNbS80x6UPUgedMO9G%2Br%2F%2F%2BE1jKNEDCB62T9iuzcT4mxfo9GsK5Cl
%2BRctESfoEzgIQByfWFXY851gkDadMBUfosYGy2hqUEN%2FHHprz%2Brj9Xm3jCj3FSKg7vdaE2zwtMZwf0cHW41GC%2BfLj
%2BMnAx1sVGwQ8uqC6N9zyqp8jvdL48qCT9mCuDiZpA%3D%3D&sign_type=RSA2&timestamp=2018-12-
11+10%3A18%3A29&version=1.0"}

                */
        MyParams params2 = new MyParams();
        params2.put("price", 0.01);

        OkGoHttpUtil.doPost(HttpActivity.this, "http://47.94.90.205/a/pay/app", params2, false, "", new BaseHttpCallbackListener<Element>() {
            @Override
            public void callbackSuccess(String url, Element element) {
                super.callbackSuccess(url, element);

/*                        Log.i("========",element.results);*/
                tv_result.setText(element.rows);

                orderInfo = element.rows;
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(HttpActivity.this);
                        Map<String, String> result = alipay.payV2(orderInfo, true);

                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };

                Thread payThread = new Thread(payRunnable);
                payThread.start();

            }
        });
    }


    private void getAliPayState(String no) {

        Logger.w("支付成功");


        MyParams params = new MyParams();
        params.put("userid", "");
        params.put("out_trade_no", no);

//        VictorHttpUtil.doPost(false, mContext, Define.ALIPAY_STATE_URL, params, true, "加载中...",
//                new BaseHttpCallbackListener<Element>() {
//                    @Override
//                    public void callbackSuccess(String url, Element element) {
//                        super.callbackSuccess(url, element);
//
//                        JSONObject obj = null;//最外层的JSONObject对象
//                        try {
//                            obj = new JSONObject(element.data);
//                            String tjh_status = obj.getString("tjh_status");
//                            if ("1".equals(tjh_status)){
//                                finish();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
    }


    public void getFile() {
        //http://tp.homebank.shop/apk/tiancang2.2.16_legu_signed_zipalign.apk
        //new BaseFileCallbackListener<File>  记得传泛型
        OkGoHttpUtil.download(HttpActivity.this, "http://tp.homebank.shop/apk/tiancang2.2.16_legu_signed_zipalign.apk",false,"",new BaseFileCallbackListener<File>(){
            @Override
            public void callbackSuccess(String url, File file) {
                super.callbackSuccess(url, file);

                Log.i("123456",url+"\n"+file.getPath());

                //安装apk文件
                //ApkUtil.installApk(HttpActivity.this,file);

            }
        });
    }

    /*
    我们为每个请求前都设置了一个参数tag，取消就是通过这个tag来取消的。
    通常我们在Activity中做网络请求，当Activity销毁时要取消请求否则会发生内存泄露，那么就可以在onDestory()里面写如下代码
     */
    protected void onDestroy() {
        super.onDestroy();
        //取消全局默认的OkHttpClient中标识为tag的请求
        OkGo.getInstance().cancelTag(HttpActivity.this);
    }

}
