package com.liweidong.basemvc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.liweidong.basemvc.activitydemo.HttpActivity;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

    }

    public void onclick(View v){
        switch (v.getId()){
            case R.id.btn_http:
                startActivity(new Intent(DemoActivity.this, HttpActivity.class));
                break;
        }
    }
}
