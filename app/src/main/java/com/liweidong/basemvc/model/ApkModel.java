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
package com.liweidong.basemvc.model;

import java.io.Serializable;
import java.util.Random;

public class ApkModel implements Serializable {

    //文件名
    public String name;
    //文件地址
    public String url;
    //文件图标
    public String iconUrl;
    //下载优先级
    public int priority;

    //随机产生100以内的整数作为优先级
    public ApkModel() {
        Random random = new Random();
        priority = random.nextInt(100);
    }
}
