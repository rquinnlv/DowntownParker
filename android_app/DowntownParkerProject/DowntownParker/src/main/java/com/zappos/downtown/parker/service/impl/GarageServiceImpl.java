package com.zappos.downtown.parker.service.impl;

import com.zappos.downtown.parker.model.GarageData;
import com.zappos.downtown.parker.service.GarageService;
import com.zappos.downtown.parker.task.GarageDataCallback;
import com.zappos.downtown.parker.task.RetrieveGarageDataTask;

import java.util.HashSet;
import java.util.Set;

/**
 * An implementation of the {@link GarageService} data access object.
 */
public class GarageServiceImpl implements GarageService {

    private GarageData cachedGarageData;
    private Set<GarageDataCallback> callbacks = new HashSet<GarageDataCallback>();

    private static GarageService instance;

    /**
     * Retrieve the singleton instance of this class.
     * @return the singleton instance of {@link GarageService}
     */
    public static GarageService getInstance() {
        if (instance == null) {
            instance = new GarageServiceImpl();
        }
        return instance;
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private GarageServiceImpl() { }

    @Override
    public void refreshCache(final GarageDataCallback callback) {
        cachedGarageData = null;
        getGarageData(callback);
    }

    /**
     * Retrieve the cached {@link GarageData} once it's ready.
     * @param callback the {@link GarageDataCallback} to call once the data is available
     */
    @Override
    public synchronized void getGarageData(final GarageDataCallback callback) {
        this.callbacks.add(callback);

        if (!isGarageDataAvailable()) {
            cacheGarage();
        } else {
            executeCallbacks();
        }
    }

    /**
     * Determines if the cached {@link GarageData} is available yet.
     * @return true if it is available, false otherwise
     */
    @Override
    public synchronized boolean isGarageDataAvailable() {
        return cachedGarageData != null;
    }

    /**
     * Retrieve the {@link GarageData} from a web service.
     */
    private void cacheGarage() {
        new RetrieveGarageDataTask().execute(new GarageDataCallback() {
            @Override
            public void onGarageDataAvailable(final GarageData garageData) {
                cachedGarageData = garageData;
                executeCallbacks();
            }

            @Override
            public void onException(final Exception e) {
                for (final GarageDataCallback callback : callbacks) {
                    callback.onException(e);
                }
            }
        });
    }

    /**
     * If the {@link GarageDataCallback} and {@link GarageData} are both available, execute the
     * {@link GarageDataCallback} with the {@link GarageData}. Clear all current callbacks.
     */
    private synchronized void executeCallbacks() {
        if (cachedGarageData != null) {
            for (final GarageDataCallback callback : callbacks) {
                callback.onGarageDataAvailable(cachedGarageData);
            }

            callbacks = new HashSet<GarageDataCallback>();
        }
    }
}
