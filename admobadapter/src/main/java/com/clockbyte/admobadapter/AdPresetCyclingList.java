package com.clockbyte.admobadapter;

import com.clockbyte.admobadapter.expressads.ExpressAdPreset;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by FILM on 02.10.2016.
 */

public class AdPresetCyclingList extends ArrayList<ExpressAdPreset> {

    private int currentIdx = -1;

    public AdPresetCyclingList(){
        super();
    }

    public int getCurrentIdx() {
        return currentIdx;
    }

    /**
     * Gets next ad preset for Admob banners from FIFO.
     * It works like cycling FIFO (first in = first out, cycling from end to start).
     * Each ad block will get one from the queue.
     * If the desired count of ad blocks is greater than this collection size
     * then it will go again to the first item and iterate as much as it required.
     * ID should be active, please check it in your Admob's account.
     */
    public ExpressAdPreset get() {
        if(size() == 0) return null;
        if (size() == 1) return get(0);
        currentIdx = ++currentIdx % size();
        return get(currentIdx);
    }

    /**
     * Tries to add an item to collection if it is valid {@link ExpressAdPreset#isValid()}
     * @return true if item was added, false - otherwise
     */
    @Override
    public boolean add(ExpressAdPreset expressAdPreset) {
        return !(expressAdPreset == null || !expressAdPreset.isValid())
                && super.add(expressAdPreset);
    }

    /**
     * Tries to add items to collection if valid {@link ExpressAdPreset#isValid()}
     * @return true if items were added, false - otherwise
     */
    @Override
    public boolean addAll(Collection<? extends ExpressAdPreset> c) {
        ArrayList<ExpressAdPreset> lst = new ArrayList<ExpressAdPreset>();
        for (ExpressAdPreset eap : c) {
            if(eap!=null && eap.isValid())
                lst.add(eap);
        }
        return super.addAll(lst);
    }
}
