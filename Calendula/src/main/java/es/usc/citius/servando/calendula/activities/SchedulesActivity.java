package es.usc.citius.servando.calendula.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import es.usc.citius.servando.calendula.R;
import es.usc.citius.servando.calendula.fragments.MedicineCreateOrEditFragment;
import es.usc.citius.servando.calendula.fragments.ScheduleTimetableFragment;
import es.usc.citius.servando.calendula.model.Medicine;
import es.usc.citius.servando.calendula.util.FragmentUtils;

public class SchedulesActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener, MedicineCreateOrEditFragment.OnMedicineEditListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    ActionBar mActionBar;

    Button prevButton;
    Button nextButton;

    int currentPageIndicatorColor;
    int normalPageIndicatorColor;
    int selectedPage = -1;

    // Medicine reference that will be created and returned by the createOrEdit fragment
    Medicine med;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedules);

        normalPageIndicatorColor = getResources().getColor(R.color.android_blue_light);
        currentPageIndicatorColor = getResources().getColor(R.color.android_blue_dark);

        prevButton = (Button) findViewById(R.id.schedules_prev_button);
        nextButton = (Button) findViewById(R.id.schedules_next_button);

        setFormOnClickListeners();

        mActionBar = getSupportActionBar();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
        // set first page indicator
        setCurrentPageIndicator(0);
    }


    void setFormOnClickListeners() {
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show prev page if any
                int currentPage = mViewPager.getCurrentItem();
                if (currentPage > 0) {
                    mViewPager.setCurrentItem(currentPage - 1);
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show next page if any
                int currentPage = mViewPager.getCurrentItem();
                if (currentPage < 2 && validatePage(currentPage)) {
                    mViewPager.setCurrentItem(currentPage + 1);
                }
            }
        });
    }


    boolean validatePage(int page) {
        if (page == 0) {
            MedicineCreateOrEditFragment fragment = ((MedicineCreateOrEditFragment) getViewPagerFragment(0));
            if (fragment.validate()) {
                med = fragment.getMedicineFromView();
                Log.d(SchedulesActivity.class.getName(), "Med created but no saved: " + med.getName() + ", " + med.getPresentation().getName(getResources()));
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.schedules, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    void setCurrentPageIndicator(int page) {

        Log.d(SchedulesActivity.class.getName(), "page: " + page + ", current: " + selectedPage);

        if (page != selectedPage) {
            // uncheck old page indicator
            if (selectedPage == 0) {
                findViewById(R.id.add_sched_page_indicator_up_1).setBackgroundColor(normalPageIndicatorColor);
                findViewById(R.id.add_sched_page_indicator_down_1).setBackgroundColor(normalPageIndicatorColor);
                Log.d(SchedulesActivity.class.getName(), "Set background");
            } else if (selectedPage == 1) {
                findViewById(R.id.add_sched_page_indicator_up_2).setBackgroundColor(normalPageIndicatorColor);
                findViewById(R.id.add_sched_page_indicator_down_2).setBackgroundColor(normalPageIndicatorColor);
            } else if (selectedPage == 2) {
                findViewById(R.id.add_sched_page_indicator_up_3).setBackgroundColor(normalPageIndicatorColor);
                findViewById(R.id.add_sched_page_indicator_down_3).setBackgroundColor(normalPageIndicatorColor);
            }
            // check new page indicator
            if (page == 0) {
                findViewById(R.id.add_sched_page_indicator_up_1).setBackgroundColor(currentPageIndicatorColor);
                findViewById(R.id.add_sched_page_indicator_down_1).setBackgroundColor(currentPageIndicatorColor);
                // update buttons
                prevButton.setBackgroundResource(R.drawable.transparent_button_selector);
                prevButton.setEnabled(false);
                nextButton.setBackgroundResource(R.drawable.next_button_selector);
                nextButton.setText(R.string.next);

            } else if (page == 1) {
                findViewById(R.id.add_sched_page_indicator_up_2).setBackgroundColor(currentPageIndicatorColor);
                findViewById(R.id.add_sched_page_indicator_down_2).setBackgroundColor(currentPageIndicatorColor);
                // update buttons
                prevButton.setEnabled(true);
                prevButton.setBackgroundResource(R.drawable.prev_button_selector);
                nextButton.setBackgroundResource(R.drawable.next_button_selector);
                nextButton.setText(R.string.next);

            } else if (page == 2) {
                findViewById(R.id.add_sched_page_indicator_up_3).setBackgroundColor(currentPageIndicatorColor);
                findViewById(R.id.add_sched_page_indicator_down_3).setBackgroundColor(currentPageIndicatorColor);
                // update buttons
                prevButton.setEnabled(true);
                prevButton.setBackgroundResource(R.drawable.prev_button_selector);
                nextButton.setBackgroundResource(R.drawable.confirm_button_selector);
                nextButton.setText(R.string.confirm);
            }

            selectedPage = page;

            Log.d(SchedulesActivity.class.getName(), "page: " + page + ", current: " + selectedPage);
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int page) {
        setCurrentPageIndicator(page);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onMedicineEdited(Medicine r) {

    }

    @Override
    public void onMedicineCreated(Medicine m) {
        // save med reference
        med = m;
        // go to next step
        mViewPager.setCurrentItem(1);
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
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return new MedicineCreateOrEditFragment();
            } else if (position == 1) {
                return new ScheduleTimetableFragment();
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_schedules, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() > 0) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        } else {
            super.onBackPressed();
        }
    }

    Fragment getViewPagerFragment(int position) {
        return getSupportFragmentManager().findFragmentByTag(FragmentUtils.makeViewPagerFragmentName(R.id.pager, position));
    }

}
