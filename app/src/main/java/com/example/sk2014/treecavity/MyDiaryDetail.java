package com.example.sk2014.treecavity;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;

import datastruct.Diary;

public class MyDiaryDetail extends AppCompatActivity {
    public Diary diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_diary_detail);

        String objectId = getIntent().getStringExtra("objectId");
        if (!objectId.equals("")) {
            AVQuery<AVObject> avQuery = new AVQuery<>("theDiary");
            avQuery.getInBackground(objectId, new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    diary = new Diary(avObject.getString("author"), avObject.getString("title"), avObject.getString("content"), avObject.getObjectId(), avObject.getCreatedAt());
                    updateUI();
                }
            });
        }


    }

    public void updateUI() {
        TextView title = (TextView)findViewById(R.id.title);
        TextView date = (TextView)findViewById(R.id.date);
        TextView content = (TextView)findViewById(R.id.content);

        if (diary != null) {
            title.setText(diary.title);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((diary.date.getYear() + 1900) + "-" + (diary.date.getMonth() + 1) + "-" + diary.date.getDate());
            date.setText(stringBuilder.toString());
            content.setText(diary.content);
        }
    }
}
