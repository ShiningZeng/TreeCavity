package com.example.sk2014.treecavity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.Arrays;
import java.util.List;

import datastruct.Diary;
import datastruct.DiaryMessage;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;

public class NavigationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_page);

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

//                AVQuery<AVObject> historyDiary = new AVQuery<AVObject>("otherDiaryHistory");
//                historyDiary.whereEqualTo("owner", AVUser.getCurrentUser().getUsername());

                AVQuery<AVObject> allDiary = new AVQuery<>("theDiary");
                allDiary.whereNotEqualTo("author", AVUser.getCurrentUser().getUsername());

                allDiary.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        int random = (int)(Math.random()*list.size());
                        AVObject obj = list.get(random);
                        Intent intent = new Intent(NavigationPage.this, OtherDiaryDetail.class);
                        Diary diary = new Diary(
                                obj.getString("author"),
                                obj.getString("title"),
                                obj.getString("content"),
                                obj.getObjectId(),
                                obj.getCreatedAt());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("object", diary);
                        bundle.putInt("tag", 2);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        });
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
            }
        });
        PushService.setDefaultPushCallback(this, NavigationPage.class);
    }
}
