/*
 * Copyright (c) 2017 Clockbyte LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clockbyte.admobadapter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by FILM on 02.10.2016.
 */

public class AdPresetCyclingList extends ArrayList<AdPreset> {

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
    public AdPreset get() {
        if(size() == 0) return null;
        if (size() == 1) return get(0);
        currentIdx = ++currentIdx % size();
        return get(currentIdx);
    }

    /**
     * Tries to add an item to collection if it is valid {@link AdPreset#isValid()}
     * @return true if item was added, false - otherwise
     */
    @Override
    public boolean add(AdPreset adPreset) {
        return !(adPreset == null || !adPreset.isValid())
                && super.add(adPreset);
    }

    /**
     * Tries to add items to collection if valid {@link AdPreset#isValid()}
     * @return true if items were added, false - otherwise
     */
    @Override
    public boolean addAll(Collection<? extends AdPreset> c) {
        ArrayList<AdPreset> lst = new ArrayList<AdPreset>();
        for (AdPreset eap : c) {
            if(eap!=null && eap.isValid())
                lst.add(eap);
        }
        return super.addAll(lst);
    }
}
