package com.zappos.downtown.parker.task;

import android.os.AsyncTask;
import android.util.Log;

import com.zappos.downtown.parker.dao.impl.GarageFetchDAOImpl;
import com.zappos.downtown.parker.model.GarageData;
import com.zappos.downtown.parker.model.exception.DaoException;

/**
 * A remotely executing task to retrieve garage information
 */
public class RetrieveGarageDataTask extends AsyncTask<GarageDataCallback, Void, GarageData> {

    private Exception exception;

    private GarageDataCallback[] callbacks;

    @Override
    protected GarageData doInBackground(GarageDataCallback... callbacks) {
        this.callbacks = callbacks;

        try {
            return new GarageFetchDAOImpl().getGarageInfo();
        } catch (DaoException e) {
            exception = e;
            return null;
        }

    }

    @Override
    protected void onPostExecute(GarageData garageData) {
        if (exception != null) {
            Log.e(getClass().getSimpleName(), exception.getMessage(), exception);

            if (callbacks != null) {
                for (final GarageDataCallback callback : callbacks) {
                    callback.onException(exception);
                }
            }

            return;
        }

        if (callbacks != null) {
            for (final GarageDataCallback callback : callbacks) {
                callback.onGarageDataAvailable(garageData);
            }
        }
    }

}
