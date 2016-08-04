package com.clockbyte.admobadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by FILM on 01.02.2016.
 */
public class ViewWrapper<V extends View> extends RecyclerView.ViewHolder{

    public ViewWrapper(V itemView) {
        super(itemView);
    }
}