package com.liweidong.basemvc.http;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/12.
 */

public class MyParams{

    private final List<KeyValue> params = new ArrayList<>();

    /**
     * 向集合添加参数
     */
    public void put(String key, Object value) {
        this.params.add(new KeyValue(key, value));
    }

    /**
     * 返回参数集合
     */
    public List<KeyValue> getParamsList() {
        return this.params;
    }

    /**
     * 判断是否为空
     */
    public boolean isEmpty() {
        return params.size() <= 0;
    }

    /**
     * 重写toString方法
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        if (params != null && !params.isEmpty()) {
            for (KeyValue item : params) {
                builder.append(item.key).append("=").append(item.value).append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("}");
        return builder.toString();
    }
}
