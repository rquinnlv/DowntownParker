package com.zappos.downtown.parker.task;

import com.zappos.downtown.parker.model.GarageData;

/**
 * This interface is used as a callback for {@link com.zappos.downtown.parker.model.GarageData} that has been asynchronously read
 */
public interface GarageDataCallback {
    void onGarageDataAvailable(final GarageData garageData);
    void onException(final Exception e);
}
