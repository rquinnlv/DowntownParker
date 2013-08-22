package com.zappos.downtown.parker.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.zappos.downtown.parker.ParkinglotDetailActivity;
import com.zappos.downtown.parker.dao.ParkerDAO;
import com.zappos.downtown.parker.dao.impl.ParkerDAOImpl;
import com.zappos.downtown.parker.model.GarageInfo;
import com.zappos.downtown.parker.model.exception.DaoException;

/**
 * A remotely executing task to retrieve garage information
 */
public class RetrieveDataTask extends AsyncTask<String, Void, GarageInfo> {

    private static ParkerDAO parkerDAO;
    private Exception exception;

    private ProgressDialog progressDialog;
    private Activity activity;

    private String errorTitle;
    private String errorMessage;

    public RetrieveDataTask(Activity activity, String loadingTitle, String loadingMessage,
                            String errorTitle, String errorMessage) {
        this.activity = activity;

        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;

        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(loadingTitle);
        progressDialog.setMessage(loadingMessage);
        progressDialog.show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected GarageInfo doInBackground(String... strings) {
        if (strings.length != 1) {
            exception = new IllegalArgumentException("Must provide exactly one URL");
            return null;
        }

        String url = strings[0];

        if (parkerDAO == null) {
            parkerDAO = new ParkerDAOImpl(url);
        }

        try {
            return parkerDAO.getGarageInfo();
        } catch (DaoException e) {
            exception = e;
            return null;
        }

    }

    @Override
    protected void onPostExecute(GarageInfo garageInfo) {
        progressDialog.dismiss();

        if (exception != null) {
            Log.e(getClass().getSimpleName(), exception.getMessage(), exception);
            showErrorMessage();
            return;
        }

        ParkinglotDetailActivity.setGarageInfo(garageInfo);
    }

    private void showErrorMessage() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(errorMessage)
                .setTitle(errorTitle)
                .setCancelable(false)
                .setNeutralButton("Well shucks", null);

    }

}
