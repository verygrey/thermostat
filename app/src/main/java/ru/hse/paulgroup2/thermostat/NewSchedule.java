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
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

    MyNewTSchedule schedule;
    ArrayList<LinkedList<PairPeriod>> allSchedulePeriods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);

        Intent intent = getIntent();
        schedule = (MyNewTSchedule) intent.getSerializableExtra("SCHEDULE");
        allSchedulePeriods = new ArrayList<>(7);

        for (int day = 0; day < 7; day++) {

            ArrayList<Period> schedulePeriods = schedule.getFullSchedule(day);
            LinkedList<PairPeriod> dayPairPeriods = new LinkedList<>();

            for (int numberPeriod = 0; numberPeriod < schedulePeriods.size(); numberPeriod += 2) {
                Period dayPeriod = schedulePeriods.get(numberPeriod);
                Period nightPeriod = schedulePeriods.get(numberPeriod + 1);
                PairPeriod pairPeriod = new PairPeriod(dayPeriod, nightPeriod);
                dayPairPeriods.add(pairPeriod);
            }

            allSchedulePeriods.add(dayPairPeriods);
        }

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
//        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }

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
            fragment.environment = linkedActivity;
            return fragment;
        }

        public PlaceholderFragment() {
        }

        int day;

        NewSchedule environment;

        LinearLayout periodContainer;

        LinkedList<PairPeriod> allDayPairPeriods;
        LinkedList<View> allViews;

        private void collapsePeriod(int index) {
            Time newNightEnd = allDayPairPeriods.get(index).night.end;
            allDayPairPeriods.remove(index);
            if (index != 0) {
                allDayPairPeriods.get(index - 1).night.end = newNightEnd;
            }
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
            periodContainer.removeView(view);
            collapsePeriod(index);
            if (index != 0) {
                ((TextView) allViews.get(index - 1).findViewById(R.id.nighttime)).setText("NIGHT: " + allDayPairPeriods.get(index - 1).night.toString());
            }
        }

        boolean inside(Time time, Period period) {
            return period.begin.toInt() <= time.toInt() && time.toInt() <= period.end.toInt();
        }

        boolean collapse(Period one, Period two) {
            if (inside(one.begin, two) || inside(one.end, two) || inside(two.begin, one) || inside(two.end, one)){
                return true;
            }
            return false;
        }

        boolean addNewPairPeriod(LayoutInflater inflater, Period dayPeriod) {
            for (int index = 0; index < allDayPairPeriods.size(); index++) {
                if (collapse(allDayPairPeriods.get(index).day, dayPeriod)) {
                    return false;
                }
            }
            if (allDayPairPeriods.isEmpty()) {
                PairPeriod newPP = new PairPeriod(dayPeriod, new Period(dayPeriod.end.minuteAfter(), new Time(23, 59)));
                allDayPairPeriods.add(0, newPP);
                periodContainer.addView(createPairPeriodView(inflater, newPP, 0), 0);
                return true;
            }
            if (inside(dayPeriod.begin, new Period(new Time(0, 0), allDayPairPeriods.getFirst().day.begin))) {
                PairPeriod newPP = new PairPeriod(dayPeriod, new Period(dayPeriod.end.minuteAfter(), allDayPairPeriods.getFirst().day.begin.minuteBefore()));
                allDayPairPeriods.add(0, newPP);
                periodContainer.addView(createPairPeriodView(inflater, newPP, 0), 0);
                return true;
            }
            for (int index = 0; index < allDayPairPeriods.size(); index++) {
                PairPeriod pair = allDayPairPeriods.get(index);
                if (inside(dayPeriod.begin, pair.night)) {
                    PairPeriod newPP = new PairPeriod(dayPeriod, new Period(dayPeriod.end.minuteAfter(), pair.night.end));
                    pair.night.end = dayPeriod.begin.minuteBefore();
                    ((TextView) allViews.get(index).findViewById(R.id.nighttime)).setText("NIGHT: " + pair.night.toString());
                    allDayPairPeriods.add(index + 1, newPP);
                    periodContainer.addView(createPairPeriodView(inflater, newPP, index + 1), index + 1);
                    return true;
                }
            }
            throw new RuntimeException("FAILFAILFAIL");
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_new_schedule, container, false);
            periodContainer = (LinearLayout) rootView.findViewById(R.id.periodsContainer);

            allDayPairPeriods = environment.allSchedulePeriods.get(day - 1);

            allViews = new LinkedList<>();
            for (int pair = 0; pair < allDayPairPeriods.size(); pair++) {
                ViewGroup pairPeriodView = createPairPeriodView(inflater, allDayPairPeriods.get(pair), pair);
                periodContainer.addView(pairPeriodView, pair);
            }

            rootView.findViewById(R.id.addperiod).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(environment, "ADD TIME", Toast.LENGTH_SHORT).show();
                    TimePickerDialog tpdBegin = new TimePickerDialog(environment, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            final Time begin = new Time(hourOfDay, minute);
                            TimePickerDialog tpdEnd = new TimePickerDialog(environment, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    final Time end = new Time(hourOfDay, minute);
                                    Period newPeriod = new Period(begin, end);
                                    addNewPairPeriod(inflater, newPeriod);
                                }
                            }, hourOfDay, minute, true);
                            tpdEnd.show();
                        }
                    }, 12, 0, true);
                    tpdBegin.show();
                }
            });
            return rootView;
        }
    }

    public void exportSchedule(View view) {
        Intent answerIntent = new Intent();
        MyNewTSchedule nts = new MyNewTSchedule();
        for (int day = 0; day < allSchedulePeriods.size(); day++) {
            LinkedList<PairPeriod> dayPeriods = allSchedulePeriods.get(day);
            for (PairPeriod pair: dayPeriods) {
                nts.addPeriodToEnd(day, pair.day.begin, pair.day.end);
            }
        }
        schedule = nts;
        answerIntent.putExtra("SCHEDULE", nts);
        setResult(RESULT_OK, answerIntent);
        finish();
    }
}
