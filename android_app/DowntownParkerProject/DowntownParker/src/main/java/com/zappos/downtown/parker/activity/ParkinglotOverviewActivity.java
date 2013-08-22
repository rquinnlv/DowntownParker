package com.zappos.downtown.parker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zappos.downtown.parker.ParkingApp;
import com.zappos.downtown.parker.R;
import com.zappos.downtown.parker.model.GarageData;
import com.zappos.downtown.parker.service.impl.GarageServiceImpl;
import com.zappos.downtown.parker.task.GarageDataCallback;

public class ParkinglotOverviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parkinglot_overview_activity);

        RelativeLayout northLayout = (RelativeLayout) findViewById(R.id.northGarageLayout);
        northLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParkinglotDetailActivity.setInitiallySelectedTab(ParkinglotDetailActivity.NORTH_GARAGE_TAB_POSITION);
                Intent intent = new Intent(view.getContext(), ParkinglotDetailActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        RelativeLayout southLayout = (RelativeLayout) findViewById(R.id.southGarageLayout);
        southLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParkinglotDetailActivity.setInitiallySelectedTab(ParkinglotDetailActivity.SOUTH_GARAGE_TAB_POSITION);
                Intent intent = new Intent(view.getContext(), ParkinglotDetailActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        RelativeLayout parkingLotLayout = (RelativeLayout) findViewById(R.id.parkingLotLayout);
        parkingLotLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParkinglotDetailActivity.setInitiallySelectedTab(ParkinglotDetailActivity.PARKING_LOT_TAB_POSITION);
                Intent intent = new Intent(view.getContext(), ParkinglotDetailActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        final Activity activity = this;
        ParkingApp.showProgressDialog(activity);

        GarageServiceImpl.getInstance().getGarageData(new GarageDataCallback() {
            @Override
            public void onGarageDataAvailable(GarageData garageData) {
                ParkingApp.hideProgressDialog();

                TextView northCount = (TextView) activity.findViewById(R.id.northGarageCount);
                TextView southCount = (TextView) activity.findViewById(R.id.southGarageCount);
                TextView parkingLotCount = (TextView) activity.findViewById(R.id.parkingLotCount);

                northCount.setText(Integer.toString(garageData.getNorthGarage().getOpenSpots()));
                southCount.setText(Integer.toString(garageData.getSouthGarage().getOpenSpots()));
                parkingLotCount.setText(Integer.toString(garageData.getParkingLot().getOpenSpots()));
            }

            @Override
            public void onException(Exception e) {
                ParkingApp.showErrorDialog(activity);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.parkinglot_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                ParkingApp.refreshCache(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}
