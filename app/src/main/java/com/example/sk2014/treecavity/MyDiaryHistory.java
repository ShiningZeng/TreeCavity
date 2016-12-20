package com.example.sk2014.treecavity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.List;

import datastruct.Diary;
import datastruct.DiaryAdapter;

public class MyDiaryHistory extends AppCompatActivity {
    public ListView listView;
    public DiaryAdapter diaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_diary_history);

        listView = (ListView)findViewById(R.id.diary_list);
        diaryAdapter = new DiaryAdapter(this);

        getData();


    }

    public void getData() {
        AVQuery<AVObject> query = new AVQuery<>("theDiary");
        query.whereEqualTo("author", AVUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject obj : list) {
                    diaryAdapter.diaryArrayList.add(new Diary(obj.getString("author"), obj.getString("title"), obj.getString("content"), obj.getObjectId(), obj.getCreatedAt()));
                }
                listView.setAdapter(diaryAdapter);
            }
        });

    }
}
