package com.clockbyte.admobadapter.sampleapp;

import android.content.Context;
import android.view.ViewGroup;

import com.clockbyte.admobadapter.RecyclerViewAdapterBase;
import com.clockbyte.admobadapter.ViewWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FILM on 01.02.2016.
 */
public class RecyclerExampleAdapter extends RecyclerViewAdapterBase<String, RecyclerViewExampleItem> {

    private List<String> items = new ArrayList<String>();

    private Context mContext;

    public RecyclerExampleAdapter(Context context){
        mContext = context;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<RecyclerViewExampleItem> viewHolder, int position) {
        RecyclerViewExampleItem rvei = viewHolder.getView();
        String str = getItem(position);
        rvei.bind(str);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected RecyclerViewExampleItem onCreateItemView(ViewGroup parent, int viewType) {
        RecyclerViewExampleItem rvei = new RecyclerViewExampleItem(mContext);
        return rvei;
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    public void addAll(List<String> lst){
        items.addAll(lst);
    }
}
