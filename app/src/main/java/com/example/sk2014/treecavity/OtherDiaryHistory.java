package com.example.sk2014.treecavity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;

import datastruct.Diary;
import datastruct.DiaryAdapter;
import datastruct.DiaryMessage;

public class OtherDiaryHistory extends AppCompatActivity {
    // layout variables
    private ListView listView;
    public ProgressBar progressView;
    //other variables
    public DiaryAdapter diaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_diary_history);

        initVariables();
        initDiaries();
        initEventListener();
    }

    private void initVariables() {
        listView = (ListView) findViewById(R.id.myListView);
        diaryAdapter = new DiaryAdapter(this);
        progressView = (ProgressBar)findViewById(R.id.register_progress);
    }

    private void initEventListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(OtherDiaryHistory.this, OtherDiaryDetail.class);
                Diary diary = (Diary) adapterView.getAdapter().getItem(i);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object", diary);
                bundle.putInt("tag", 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    private void initDiaries() {
        showProgress(true);
        AVQuery<AVObject> query = new AVQuery<>("otherDiaryHistory");
        query.whereEqualTo("owner", AVUser.getCurrentUser().getUsername());
        query.include("diary");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject obj : list) {
                    AVObject diary = obj.getAVObject("diary");
                    diaryAdapter.diaryArrayList.add(
                            new Diary(diary.getString("author"),
                                    diary.getString("title"),
                                    diary.getString("content"),
                                    diary.getObjectId(),
                                    diary.getCreatedAt()));}
                listView.setAdapter(diaryAdapter);
                showProgress(false);
            }
        });
    }

    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

            listView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
            listView.animate().setDuration(2000).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    listView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            progressView.animate().setDuration(2200).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                }
            });
        }
    }

}
