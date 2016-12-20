package datastruct;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by sk2014 on 2016/12/20.
 */

public class DiaryAdapter extends BaseAdapter {
    public ArrayList<Diary> diaryArrayList;
    public Context context;


    public DiaryAdapter(Context context) {
        this.context = context;
        diaryArrayList = new ArrayList<Diary>();
    }

    @Override
    public int getCount() {
        return diaryArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return diaryArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // to be continue
        return null;
    }
}
