package com.clockbyte.admobadapter.sampleapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.clockbyte.admobadapter.sampleapp.express.ViewWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FILM on 01.02.2016.
 */
public class RecyclerExampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> items = new ArrayList<String>();

    private Context mContext;

    public RecyclerExampleAdapter(Context context){
        mContext = context;
    }

    @Override
    public ViewWrapper<RecyclerViewExampleItem> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<RecyclerViewExampleItem>(new RecyclerViewExampleItem(mContext));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RecyclerViewExampleItem rvei = (RecyclerViewExampleItem) viewHolder.itemView;
        String str = getItem(position);
        rvei.bind(str);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public String getItem(int position) {
        return items.get(position);
    }

    public void addAll(List<String> lst){
        items.addAll(lst);
    }

    public void setItem(int index, String item){
        items.set(index, item);
        notifyItemChanged(index);
    }

    public void setItems(int startPosition, int count, String item){
        int last = startPosition+count;
        for (int i = startPosition; i < last; i++)
            items.set(i, item);
        notifyItemRangeChanged(startPosition, count);
    }
}
