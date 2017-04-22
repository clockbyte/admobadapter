package com.clockbyte.admobadapter.expressads;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by FILM on 28.10.2016.
 */

public class NativeHolder extends RecyclerView.ViewHolder {

    public NativeHolder(ViewGroup adViewWrapper){
        super(adViewWrapper);
    }

    public ViewGroup getAdViewWrapper() {
        return (ViewGroup)itemView;
    }

}
