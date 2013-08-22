package com.zappos.downtown.parker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;

import com.zappos.downtown.parker.model.Garage;
import com.zappos.downtown.parker.model.GarageData;
import com.zappos.downtown.parker.service.impl.GarageServiceImpl;
import com.zappos.downtown.parker.task.GarageDataCallback;

/**
 * Master application layer
 */
public class ParkingApp extends Application {

    private static Context mContext;

    private static ProgressDialog progressDialog;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }

    public static void showProgressDialog(final Context context) {
        hideProgressDialog();

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getString(R.string.loading_title));
        progressDialog.setMessage(context.getString(R.string.loading_message));
        progressDialog.show();
    }

    public static void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static void showErrorDialog(final Context context) {
        hideProgressDialog();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(context.getString(R.string.loading_message_error))
                .setTitle(context.getString(R.string.loading_title_error))
                .setCancelable(false)
                .setNeutralButton("Well shucks", null);
        alertDialogBuilder.create().show();
    }

    /**
     * Get a {@link com.zappos.downtown.parker.model.Garage} based on a garage number
     * @param garageData the {@link com.zappos.downtown.parker.model.GarageData} containing all the garages
     * @param garageNumber the number of the {@link com.zappos.downtown.parker.model.Garage} to get
     * @return the {@link com.zappos.downtown.parker.model.Garage} corresponding to the garage number
     */
    public static Garage getGarage(final GarageData garageData, final int garageNumber) {
        switch (garageNumber){
            case 0:
                return garageData.getNorthGarage();
            case 1:
                return garageData.getSouthGarage();
            case 2:
                return garageData.getParkingLot();
        }

        return null;
    }

    /**
     * Force a refresh of the {@link GarageData} cached in the {@link GarageServiceImpl}
     * @param activity the activity calling the refresh
     */
    public static void refreshCache(final Activity activity) {

        ParkingApp.showProgressDialog(activity);

        GarageServiceImpl.getInstance().refreshCache(new GarageDataCallback() {
            @Override
            public void onGarageDataAvailable(GarageData garageData) {
                ParkingApp.hideProgressDialog();
            }

            @Override
            public void onException(Exception e) {
                ParkingApp.showErrorDialog(activity);
            }
        });
    }
}
