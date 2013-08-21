package com.zappos.downtown.parker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ParkinglotView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parkinglot_view_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.parkinglot_view, menu);
        return true;
    }
    
}
