package com.clockbyte.admobadapter;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by FILM on 02.10.2016.
 */

public class UnitIdQueue extends ArrayBlockingQueue<String> {

    public UnitIdQueue(Collection<String> unitIds) {
        super(unitIds.size(), false, unitIds);
    }

    //peek the last unit id in FIFO without removing it
    @Override
    public String take() throws InterruptedException {
        return size() == 1
                ? super.peek()
                : super.take();
    }
}
