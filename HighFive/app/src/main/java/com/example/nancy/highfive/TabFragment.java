package com.example.nancy.highfive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

/**
 * Created by Nancy on 7/24/2015.
 */
public class TabFragment extends Fragment implements MaterialTabListener {
    private ViewPager pager;
    MaterialTabHost tabHost;
    ViewPagerAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main3,container,false);

        tabHost = (MaterialTabHost) view.findViewById(R.id.materialTabHost);
        pager = (ViewPager) view.findViewById(R.id.pager );

        // init view pager
        pagerAdapter = new ViewPagerAdapter(this.getActivity().getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);

            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(pagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );

        }

        return view;
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        // when the tab is clicked the pager swipe content to the tab position
        pager.setCurrentItem(tab.getPosition());

    }
    @Override
    public void onTabReselected(MaterialTab tab) {}

    @Override
    public void onTabUnselected(MaterialTab tab) {}
}

class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    public android.support.v4.app.Fragment getItem(int num) {
        switch (num) {
            case 0:
                return new MainViewFragment();
            case 1:
                return new MatchFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "High Five";
            case 1:
                return "Match List";
        }
        return null;
    }
    }

