package com.clockbyte.admobadapter.sampleapp;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by FILM on 01.02.2016.
 */
public class ViewWrapper<V extends View> extends RecyclerView.ViewHolder{

    public ViewWrapper(V itemView) {
        super(itemView);
    }
}