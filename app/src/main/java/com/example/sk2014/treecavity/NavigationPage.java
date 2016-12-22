package com.example.sk2014.treecavity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import java.util.Timer;
import java.util.TimerTask;

import datastruct.Diary;
import datastruct.DiaryMessage;
import pushservice.MyService;

import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.okhttp.internal.framed.FrameReader;

public class NavigationPage extends AppCompatActivity {
    private boolean flag = true;
    private MyService ms;
    private Context context;
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
        context = NavigationPage.this;
        ms = new MyService(context);
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, sc, BIND_AUTO_CREATE);
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

                AVQuery<AVObject> historyDiary = new AVQuery<AVObject>("otherDiaryHistory");
                historyDiary.whereEqualTo("owner", AVUser.getCurrentUser().getUsername());
                AVQuery<AVObject> allDiary = new AVQuery<>("theDiary");
                allDiary.whereNotEqualTo("author", AVUser.getCurrentUser().getUsername());
                allDiary.whereDoesNotMatchKeyInQuery("objectId", "diaryId", historyDiary);
                allDiary.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null ) {
                            if (list.size() != 0) {
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
                            } else {
                                Toast.makeText(NavigationPage.this, "无更多日记", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(NavigationPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        PackageManager pm = getPackageManager();
        ResolveInfo homeInfo = pm.resolveActivity(
                new Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_HOME), 0);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            ActivityInfo ai = homeInfo.activityInfo;
//            Intent startIntent = new Intent(Intent.ACTION_MAIN);
//            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//            startIntent
//                    .setComponent(new ComponentName(ai.packageName, ai.name));
//            startActivity(startIntent);
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    NavigationPage.this);
            builder.setTitle("提示");
            builder.setMessage("确定注销账号吗");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    unbindService(sc);
                    ms.setflag(false);
                    AVUser.getCurrentUser().logOut();
                    Intent intent = new Intent(NavigationPage.this, MainActivity.class);
                    NavigationPage.this.finish();
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }
}
