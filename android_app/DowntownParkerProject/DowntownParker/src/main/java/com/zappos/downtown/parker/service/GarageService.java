package com.zappos.downtown.parker.service;

import com.zappos.downtown.parker.model.GarageData;
import com.zappos.downtown.parker.task.GarageDataCallback;

/**
 * This service handles the {@link GarageData} for the application
 */
public interface GarageService {

    public void getGarageData(final GarageDataCallback callback);

    public boolean isGarageDataAvailable();
}
