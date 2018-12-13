package com.liweidong.basemvc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.liweidong.basemvc.R;
import com.liweidong.basemvc.http.BaseHttpCallbackListener;
import com.liweidong.basemvc.http.Element;
import com.liweidong.basemvc.http.MyParams;
import com.liweidong.basemvc.http.OkGoHttpUtil;

/*
http
 */
public class HttpActivity extends AppCompatActivity {

    TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        tv_result = (TextView)findViewById(R.id.tv_result);
    }

    public void get(View v){
        switch (v.getId()){
            case R.id.btn_get:
                //https://gank.io/api/data/%E7%A6%8F%E5%88%A9/1/1
                //https://gank.io/api/data/福利/1/1
                MyParams params = new MyParams();
/*                params.put("username", phone);
                params.put("password",pwd);*/
/*
https://gank.io/api/data/福利/1/1
 */
                OkGoHttpUtil.doGet(HttpActivity.this,"https://gank.io/api/data/福利/1/1",params,false,"",new BaseHttpCallbackListener<Element>(){
                    @Override
                    public void callbackSuccess(String url, Element element) {
                        super.callbackSuccess(url, element);

/*                        Log.i("========",element.results);*/
                        tv_result.setText(element.results);
                    }
                });
                break;

            case R.id.btn_post:
/*             http://47.94.90.205
                /a/login

http://47.94.90.205/a/login

{username=200005,password=123456,mobileLogin=true,app=true,imei=863064010142216}

                */
                MyParams params1 = new MyParams();
                params1.put("username", 200005);
                params1.put("password",123456);
                params1.put("mobileLogin",true);
                params1.put("app",true);
                params1.put("imei","863064010142216");


                OkGoHttpUtil.doPost(HttpActivity.this,"http://47.94.90.205/a/login",params1,false,"",new BaseHttpCallbackListener<Element>(){
                    @Override
                    public void callbackSuccess(String url, Element element) {
                        super.callbackSuccess(url, element);

/*                        Log.i("========",element.results);*/
                        tv_result.setText(element.rows);
                    }
                });
                break;

            case R.id.btn_pay:
/*             http://47.94.90.205
                /a/login

http://47.94.90.205/a/login

{username=200005,password=123456,mobileLogin=true,app=true,imei=863064010142216}

                */
                MyParams params2 = new MyParams();
                params2.put("price", 0.01);

                OkGoHttpUtil.doPost(HttpActivity.this,"http://47.94.90.205/a/pay/app",params2,false,"",new BaseHttpCallbackListener<Element>(){
                    @Override
                    public void callbackSuccess(String url, Element element) {
                        super.callbackSuccess(url, element);

/*                        Log.i("========",element.results);*/
                        tv_result.setText(element.rows);
                    }
                });
                break;


        }
    }
}
