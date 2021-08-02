package com.clockbyte.admobadapter.sampleapp;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FILM on 01.02.2016.
 */
public class RecyclerExampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> items = new ArrayList<>();

    private final Context mContext;

    public RecyclerExampleAdapter(Context context){
        mContext = context;
    }

    @Override
    public ViewWrapper<RecyclerViewExampleItem> onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        return new ViewWrapper<>(new RecyclerViewExampleItem(mContext));
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
