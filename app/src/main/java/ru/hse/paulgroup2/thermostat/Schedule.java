package ru.hse.paulgroup2.thermostat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


public class Schedule extends Activity implements ActionBar.TabListener {

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

    SeekBar daytempbar, nighttempbar;
    TextView daytemptext, nighttemptext;

    static ThermostatModel tm;
    static ThermostatController tc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Intent intent = getIntent();
        tm = new ThermostatModel();
        tm.setUserSchedule((ThermostatSchedule)intent.getSerializableExtra("SCHEDULE"));
        tc = new ThermostatController(tm);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

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

        daytempbar = (SeekBar) findViewById(R.id.daytempbar);
        nighttempbar = (SeekBar) findViewById(R.id.nighttempbar);
        daytemptext = (TextView) findViewById(R.id.scheduledaytemp);
        nighttemptext = (TextView) findViewById(R.id.schedulenighttemp);
        daytempbar.setMax(25);
        nighttempbar.setMax(25);
        daytempbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                daytemptext.setText("DAY "+(seekBar.getProgress() + 5));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        nighttempbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                nighttemptext.setText("NIGHT " + (seekBar.getProgress() + 5));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            for (int i = 0; i < 7; i++) {
                fragments.add(PlaceholderFragment.newInstance(i + 1));
//                fragments.get(i).daytimes = thermostatModel.getUserSchedule().getBegins(i + 1, ThermostatSchedule.DAY);
//                fragments.get(i).nighttimes = thermostatModel.getUserSchedule().getBegins(i + 1, ThermostatSchedule.NIGHT);
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
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_DAY_NUMBER, sectionNumber);
            fragment.setArguments(args);
            fragment.day = sectionNumber;
            return fragment;
        }

        public PlaceholderFragment() {
        }

        ArrayList<Pair<Integer, Integer>> daytimes, nighttimes;
        int day;
        LinearLayout daycontainer, nightcontainer;

        private String itos(Integer time) {
            return time / 60 + ":" + time % 60;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
            daytimes = tm.getUserSchedule().getPeriods(day, ThermostatSchedule.DAY);
            nighttimes = tm.getUserSchedule().getPeriods(day, ThermostatSchedule.NIGHT);
            daycontainer = (LinearLayout)rootView.findViewById(R.id.daybegins);
            nightcontainer = (LinearLayout)rootView.findViewById(R.id.nightbegins);
            int num = 1;
            for (final Pair<Integer, Integer> daytime: daytimes) {
                final ViewGroup newView = (ViewGroup) inflater.inflate(
                        R.layout.list_item_example, daycontainer, false);

                ((TextView) newView.findViewById(R.id.time)).setText(itos(daytime.first));

                if (daytime.second == ThermostatSchedule.DAY) {
                    newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            daytimes.remove(daytime);
                            daycontainer.removeView(newView);
                            tc.removePeriod(daytime.first, day);
                        }
                    });

                    daycontainer.addView(newView, num++);
                }
            }
            for (final Pair<Integer, Integer> nighttime: nighttimes) {
                final ViewGroup newView = (ViewGroup) inflater.inflate(
                        R.layout.list_item_example, daycontainer, false);

                ((TextView) newView.findViewById(R.id.time)).setText(itos(nighttime.first));

                newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nighttimes.remove(nighttime);
                        nightcontainer.removeView(newView);
                        tc.removePeriod(nighttime.first, day);
                    }
                });

                nightcontainer.addView(newView, num++);
            }

            rootView.findViewById(R.id.adddayperiod).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final int time = 1;
                    if (tc.addDayNightPeriod(time, day)) {
                        daytimes = tm.getUserSchedule().getPeriods(day, ThermostatSchedule.DAY);
                        Context c = v.getContext();

                        final ViewGroup newView = (ViewGroup) LayoutInflater.from(c).inflate(
                                R.layout.list_item_example, daycontainer, false);

                        ((TextView) newView.findViewById(R.id.time)).setText(itos(time));
                        newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for (Pair<Integer, Integer> p: daytimes) {
                                    if (p.first == time) {
                                        daytimes.remove(p);
                                        break;
                                    }
                                }
                                daycontainer.removeView(newView);
                                tc.removePeriod(time, day);
                            }
                        });
                        int pos = daytimes.size() + 1;
                        for (int i = 0; i < daytimes.size(); i++) {
                            if (daytimes.get(i).first * 60 + daytimes.get(i).second > time) {
                                pos = i + 1;
                                break;
                            }
                        }
                        daycontainer.addView(newView, pos);
                    }
                }
            });
            rootView.findViewById(R.id.addnightperiod).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int time = 2;
                    if (tc.addNightDayPeriod(time, day)) {
                        nighttimes = tm.getUserSchedule().getPeriods(day, ThermostatSchedule.NIGHT);
                        Context c = v.getContext();
                        final ViewGroup newView = (ViewGroup) LayoutInflater.from(c).inflate(
                                R.layout.list_item_example, nightcontainer, false);

                        ((TextView) newView.findViewById(R.id.time)).setText(itos(time));

                        newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for (Pair<Integer, Integer> p: nighttimes) {
                                    if (p.first == time) {
                                        nighttimes.remove(time);
                                        break;
                                    }
                                }
                                nightcontainer.removeView(newView);
                                tc.removePeriod(time, day);
                            }
                        });
                        int pos = nighttimes.size() + 1;
                        for (int i = 0; i < nighttimes.size(); i++) {
                            if (nighttimes.get(i).first * 60 + nighttimes.get(i).second > time) {
                                pos = i + 1;
                                break;
                            }
                        }
                        nightcontainer.addView(newView, pos);
                    }
                }
            });
            return rootView;
        }
    }

}
