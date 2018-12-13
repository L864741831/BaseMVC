package com.liweidong.basemvc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.liweidong.basemvc.http.BaseHttpCallbackListener;
import com.liweidong.basemvc.http.Element;
import com.liweidong.basemvc.http.MyParams;
import com.liweidong.basemvc.http.OkGoHttpUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void get(View v){
        switch (v.getId()){
            case R.id.tv_get:
                //https://gank.io/api/data/%E7%A6%8F%E5%88%A9/1/1
                //https://gank.io/api/data/福利/1/1
                MyParams params = new MyParams();
/*                params.put("username", phone);
                params.put("password",pwd);*/
/*
https://gank.io/api/data/福利/1/1
 */
                OkGoHttpUtil.doGet(MainActivity.this,"https://gank.io/api/data/福利/1/1",params,false,"",new BaseHttpCallbackListener<Element>(){
                    @Override
                    public void callbackSuccess(String url, Element element) {
                        super.callbackSuccess(url, element);

/*                        Log.i("========",element.results);*/
                    }
                });
                break;
        }
    }
}
