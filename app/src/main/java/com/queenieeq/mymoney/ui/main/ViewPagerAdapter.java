package com.queenieeq.mymoney.ui.main;




import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.queenieeq.mymoney.ui.main.statistics.StatisticsFragment;
import com.queenieeq.mymoney.ui.main.history.HistoryFragment;
import com.queenieeq.mymoney.ui.main.home.HomeFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private static int TAB_COUNT = 3;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return HomeFragment.newInstance();
            case 1:
                return HistoryFragment.newInstance();
            case 2:
                return StatisticsFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return HomeFragment.TITLE;

            case 1:
                return HistoryFragment.TITLE;

            case 2:
                return StatisticsFragment.TITLE;
        }
        return super.getPageTitle(position);
    }
}