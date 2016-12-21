package com.example.sk2014.treecavity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;

import pushservice.MyService;

public class NavigationPage extends AppCompatActivity {
    private MyService ms;
    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ms = ((MyService.MyBinder) iBinder).getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_page);
        ms = new MyService(NavigationPage.this);
        ms.request();
        LinearLayout edit_button = (LinearLayout) findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NavigationPage.this, EditDiary.class));
            }
        });

        LinearLayout my_diary_button = (LinearLayout) findViewById(R.id.my_diary_button);
        my_diary_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NavigationPage.this, MyDiaryHistory.class));
            }
        });

        LinearLayout other_diary_button = (LinearLayout) findViewById(R.id.other_diary_button);
        other_diary_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NavigationPage.this, OtherDiaryHistory.class));
            }
        });

        LinearLayout request_diary_button = (LinearLayout) findViewById(R.id.request_diary_button);
        request_diary_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NavigationPage.this, OtherDiaryDetail.class));
            }
        });
    }
}
