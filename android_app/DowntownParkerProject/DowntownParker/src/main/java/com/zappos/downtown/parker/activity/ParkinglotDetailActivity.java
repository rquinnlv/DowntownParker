package com.zappos.downtown.parker.activity;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zappos.downtown.parker.ParkingApp;
import com.zappos.downtown.parker.R;
import com.zappos.downtown.parker.layout.FlowLayout;
import com.zappos.downtown.parker.model.Floor;
import com.zappos.downtown.parker.model.Garage;
import com.zappos.downtown.parker.model.GarageData;
import com.zappos.downtown.parker.service.impl.GarageServiceImpl;
import com.zappos.downtown.parker.task.GarageDataCallback;

public class ParkinglotDetailActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private static GarageData garageData;

    private static final Set<ParkinglotDetailFragment> fragments =
            new HashSet<ParkinglotDetailFragment>(3);

    public static final int NORTH_GARAGE_TAB_POSITION = 0;
    public static final int SOUTH_GARAGE_TAB_POSITION = 1;
    public static final int PARKING_LOT_TAB_POSITION = 2;
    private static int initiallySelectedTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parkinglot_detail_activity);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        final Activity activity = this;
        ParkingApp.showProgressDialog(activity);

        //retrieve the garage information
        GarageServiceImpl.getInstance().getGarageData(new GarageDataCallback() {
            @Override
            public void onGarageDataAvailable(final GarageData garageData) {
                ParkingApp.hideProgressDialog();
                setGarageData(garageData);
            }

            @Override
            public void onException(final Exception e) {
                ParkingApp.showErrorDialog(activity);
            }
        });

        //set the currently selected tab
        mViewPager.setCurrentItem(initiallySelectedTab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.parkinglot_detail, menu);
        return true;
    }
    
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) { }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) { }

    /**
     * Set the tab that will be selected to start when the activity loads
     * @param initiallySelectedTab the index of the tab.
     */
    public static void setInitiallySelectedTab(int initiallySelectedTab) {
        ParkinglotDetailActivity.initiallySelectedTab = initiallySelectedTab;
    }

    /**
     * Set the parking garage info
     * @param garageData
     */
    public static void setGarageData(final GarageData garageData) {
        ParkinglotDetailActivity.garageData = garageData;

        for (ParkinglotDetailActivity.ParkinglotDetailFragment fragment : fragments) {
            int garageNumber = fragment.getGarageNumber();
            Garage garage = ParkingApp.getGarage(garageData, garageNumber);
            fragment.setGarage(garage);
        }
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a ParkinglotDetailFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new ParkinglotDetailFragment();
            Bundle args = new Bundle();
            args.putInt(ParkinglotDetailFragment.ARG_GARAGE_NUMBER, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return Integer.parseInt(getString(R.string.garage_count));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.garage_north).toUpperCase(l);
                case 1:
                    return getString(R.string.garage_south).toUpperCase(l);
                case 2:
                    return getString(R.string.garage_parking_lot).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A Parkinglot Detail fragment representing a detail view of a parking lot.
     */
    public static class ParkinglotDetailFragment extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_GARAGE_NUMBER = "garage_number";
        private int garageNumber;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            garageNumber = getArguments().getInt(ARG_GARAGE_NUMBER);
            return inflater.inflate(R.layout.fragment_parkinglot_detail, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            if (garageData != null) {
                setGarage(ParkingApp.getGarage(garageData, garageNumber));
            }
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            fragments.add(this);
        }

        @Override
        public void onStop() {
            fragments.remove(this);
            super.onStop();
        }

        public int getGarageNumber() {
            return this.garageNumber;
        }

        public void setGarage(Garage garage) {
            TextView totalCount = (TextView) getView().findViewById(R.id.totalSpotsCount);

            if (garage == null) {
                totalCount.setText(getString(R.string.total_spaces_load_error));
            } else {
                totalCount.setText(Integer.toString(garage.getOpenSpots()));
            }

            LinearLayout tableLayout = (LinearLayout) getView().findViewById(R.id.individualFloorsTable);
            FlowLayout flowLayout = new FlowLayout(getActivity());

            if (garage != null && garage.getFloors() != null) {
                for (Floor floor : garage.getFloors()) {
                    flowLayout.addView(buildFloorTableCell(floor));
                }
            }

            tableLayout.addView(flowLayout);
        }

        private LinearLayout buildFloorTableCell(Floor floor) {
            LinearLayout linearLayout = new LinearLayout(getActivity());
            linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            linearLayout.setLayoutParams(new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setPadding(
                    getResources().getDimensionPixelOffset(R.dimen.floor_left_margin),
                    getResources().getDimensionPixelOffset(R.dimen.floor_top_margin),
                    getResources().getDimensionPixelOffset(R.dimen.floor_right_margin),
                    getResources().getDimensionPixelOffset(R.dimen.floor_bottom_margin));

            TextView name = new TextView(getActivity());
            TextView count = new TextView(getActivity());
            TextView descrip = new TextView(getActivity());

            name.setText(floor.getName());
            count.setText(Integer.toString(floor.getSpotsAvailable()));
            descrip.setText(getString(R.string.ui_floor_spaces_descrip));

            name.setTextColor(getResources().getColor(R.color.floor_spaces_name_color));
            count.setTextColor(getResources().getColor(R.color.floor_spaces_count_color));
            descrip.setTextColor(getResources().getColor(R.color.floor_spaces_descrip_color));

            name.setTextSize(getResources().getDimension(R.dimen.floor_spots_name_text_size));
            count.setTextSize(getResources().getDimension(R.dimen.floor_spots_count_text_size));
            descrip.setTextSize(getResources().getDimension(R.dimen.floor_spots_descrip_text_size));

            name.setGravity(Gravity.CENTER_HORIZONTAL);
            count.setGravity(Gravity.CENTER_HORIZONTAL);
            descrip.setGravity(Gravity.CENTER_HORIZONTAL);

            linearLayout.addView(name);
            linearLayout.addView(count);
            linearLayout.addView(descrip);

            return linearLayout;
        }
    }

}
