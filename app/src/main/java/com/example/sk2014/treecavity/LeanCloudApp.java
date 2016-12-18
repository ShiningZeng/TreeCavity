package com.example.sk2014.treecavity;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/*
    数据库初始化信息
 */

public class LeanCloudApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"U6spYazJjuVB8Ggzg2e8imHR-gzGzoHsz","MmVnIkt1EON3Xcvt76lIb9F9");
    }
}
