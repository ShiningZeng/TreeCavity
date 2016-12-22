package pushservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.sk2014.treecavity.NavigationPage;
import com.example.sk2014.treecavity.R;

import java.sql.Time;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private NotificationManager mNotificationManager;
    private  Context mContext;
    private Timer timer;
    private boolean flag = true;
    private TimerTask task;
    public final  IBinder binder = new MyBinder();
    private int itemcount;

    public class MyBinder extends Binder{
        public  MyService getService() {
            return MyService.this;
        }
    }
    public MyService() {

    }


    public MyService(Context context) {
        mContext = context;
        sharedPreferences = context.getSharedPreferences("diary1",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        itemcount = sharedPreferences.getInt(AVUser.getCurrentUser().getUsername(), 0);
        if (itemcount == 0) {
            editor.putInt(AVUser.getCurrentUser().getUsername(), 0);
            editor.commit();
        }
    }

    public  void request() {
        AVQuery<AVObject> avQuery = new AVQuery<>("theDiaryMessage");
        avQuery.whereEqualTo("author", AVUser.getCurrentUser().get("username")
                .toString());
        avQuery.orderByDescending("createdAt");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Log.d("test", Integer.toString(itemcount) + " " + Integer.toString(list.size()));
                    if (itemcount < list.size()) {
                        editor.putInt(AVUser.getCurrentUser().getUsername(), list.size());
                        editor.commit();
                        itemcount = list.size();
                        Notification.Builder builder = new Notification.Builder(mContext);
                        builder.setContentTitle("一条新消息")
                                .setTicker("一条新消息")
                                .setContentText("有人回复了你")
                                .setSmallIcon(R.mipmap.app_logo)
                                .setAutoCancel(true);
                        PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, NavigationPage.class), PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(pIntent);
                        Notification notification = builder.build();
                        // Log.d("ss","ss");
                        mNotificationManager = (NotificationManager) mContext.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
                        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(0, notification);
                    }

                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    public void stopservice(Context c){
        Intent iService=new Intent(c,MyService.class);
        iService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.stopService(iService);
        flag = false;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return binder;
    }
}
