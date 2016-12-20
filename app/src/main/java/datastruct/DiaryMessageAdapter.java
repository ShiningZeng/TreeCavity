package datastruct;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by sk2014 on 2016/12/20.
 */

public class DiaryMessageAdapter extends BaseAdapter {
    public Context context;
    public ArrayList<DiaryMessage> diaryMessageArrayList;

    public DiaryMessageAdapter(Context context) {
        this.context = context;
        diaryMessageArrayList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return diaryMessageArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return diaryMessageArrayList.get(i);
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
