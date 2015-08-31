package ru.hse.paulgroup2.thermostat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class NewSchedule extends Activity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    NewThermostatSchedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Intent intent = getIntent();
        schedule = (NewThermostatSchedule) intent.getSerializableExtra("SCHEDULE");

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), this);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        ArrayList<PlaceholderFragment> fragments = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm, NewSchedule ns) {
            super(fm);
            for (int i = 0; i < 7; i++) {
                fragments.add(PlaceholderFragment.newInstance(i + 1, ns));
            }
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.saturday).toUpperCase(l);
                case 1:
                    return getString(R.string.monday).toUpperCase(l);
                case 2:
                    return getString(R.string.tuesday).toUpperCase(l);
                case 3:
                    return getString(R.string.wednesday).toUpperCase(l);
                case 4:
                    return getString(R.string.thursday).toUpperCase(l);
                case 5:
                    return getString(R.string.friday).toUpperCase(l);
                case 6:
                    return getString(R.string.saturday).toUpperCase(l);
            }
            return null;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_DAY_NUMBER = "day_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, NewSchedule linkedActivity) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_DAY_NUMBER, sectionNumber);
            fragment.setArguments(args);
            fragment.day = sectionNumber;
            fragment.clonedSchedule = linkedActivity.schedule;
            fragment.environment = linkedActivity;
            return fragment;
        }

        public PlaceholderFragment() {
        }

        int day;

        Activity environment;
        NewThermostatSchedule clonedSchedule;

        LinearLayout periodContainer;

        LinkedList<PairPeriod> allPairPeriods;
        LinkedList<View> allViews;

//        private void addPeriodFromList(LayoutInflater inflater, ViewGroup container, final int number) {
//            final ViewGroup newPeriod =
//                    (ViewGroup) inflater.inflate(R.layout.list_item_new_example, container, false);
//
//            ((TextView) newPeriod.findViewById(R.id.daytime)).setText("DAY " + allPairPeriods.get(number).first.toString());
//            ((TextView) newPeriod.findViewById(R.id.nighttime)).setText("NIGHT " + allPairPeriods.get(number).second.toString());
//
//            newPeriod.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    allPairPeriods.remove(numberPeriod);
//                    periodContainer.removeView(newPeriod);
//                }
//            });
//
//            periodContainer.addView(newPeriod, numberOfPeriods++);
//        }

        private void collapsePeriod(int index) {
            Time newNightEnd = allPairPeriods.get(index).night.end;
            allPairPeriods.remove(index);
            allPairPeriods.get(index - 1).night.end = newNightEnd;
        }

        public ViewGroup createPairPeriodView(LayoutInflater inflater, PairPeriod pair, final int index) {
            final ViewGroup newView =
                    (ViewGroup) inflater.inflate(R.layout.list_item_new_example, periodContainer, false);

            ((TextView) newView.findViewById(R.id.daytime)).setText("DAY: " + pair.day.toString());
            ((TextView) newView.findViewById(R.id.nighttime)).setText("NIGHT: " + pair.night.toString());

            newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deletePairPeriodView(newView);
                }
            });

            addNewIndex(newView, index);
            return newView;
        }

        void addNewIndex(View view, int index) {
            allViews.add(index, view);
        }

        void deletePairPeriodView(ViewGroup view) {
            int index = allViews.indexOf(view);
            allViews.remove(index);
            collapsePeriod(index);
            periodContainer.removeView(view);
        }

        PairPeriod getNewPairPeriod(Period dayPeriod) {
            for (int index = 0; index < allViews.size(); index++) {
//                if (allViews.get(index))
            }
            return null;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_new_schedule, container, false);
            periodContainer = (LinearLayout) rootView.findViewById(R.id.periodsContainer);

            ArrayList<Period> schedulePeriods = clonedSchedule.getFullSchedule(day);
            allPairPeriods = new LinkedList<>();

            for (int dayPeriod = 0; dayPeriod < schedulePeriods.size(); dayPeriod += 2) {
                Period day = schedulePeriods.get(dayPeriod);
                Period night = schedulePeriods.get(dayPeriod + 1);
                PairPeriod pairPeriod = new PairPeriod(day, night);
                allPairPeriods.add(pairPeriod);
            }

            ////

            allViews = new LinkedList<>();
            for (int pair = 0; pair < allPairPeriods.size(); pair++) {
                ViewGroup pairPeriodView = createPairPeriodView(inflater, allPairPeriods.get(pair), pair);
                periodContainer.addView(pairPeriodView, pair);
            }

            ////

            rootView.findViewById(R.id.adddayperiod).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog tpdBegin = new TimePickerDialog(environment, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            final Time begin = new Time(hourOfDay, minute);
                            TimePickerDialog tpdEnd = new TimePickerDialog(environment, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    final Time end = new Time(hourOfDay, minute);
                                    Period newPeriod = new Period(begin, end);
                                    PairPeriod newPair = getNewPairPeriod(newPeriod);
//                                    createPairPeriodView();
                                }
                            }, hourOfDay, minute, true);
                        }
                    }, 12, 0, true);
                }
            });
            return rootView;
        }
    }
}