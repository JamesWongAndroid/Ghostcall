package com.tapfury.ghostcall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class TourScreen extends FragmentActivity {

    private ViewPager viewPager;
    private TutorialAdapter adapter;

    protected ViewPagerIndicator ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_screen);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(2);
        adapter = new TutorialAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pageChangeListener);

        ratingBar = (ViewPagerIndicator) findViewById(R.id.ratingBar);
        ratingBar.setNumIndicators(adapter.getCount());
        ratingBar.setFocusable(false);


        findViewById(R.id.skipButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TourScreen.this, VerificationScreen.class));
            }
        });
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageSelected(int position) {

            ratingBar.setProgress(position + 1);

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
    };

    private class TutorialAdapter extends FragmentPagerAdapter {

        public TutorialAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TutorialImageFragment.newInstance(R.drawable.tutorial_page_one);
                case 1:
                    return TutorialImageFragment.newInstance(R.drawable.tutorial_page_two);
                case 2:
                    return TutorialImageFragment.newInstance(R.drawable.tutorial_page_three);
                case 3:
                    return TutorialImageFragment.newInstance(R.drawable.tutorial_page_four);
                case 4:
                    return TutorialImageFragment.newInstance(R.drawable.tutorial_page_five);
            }
            throw new IllegalArgumentException("unknown fragment position");
        }

        @Override
        public int getCount() {
            return 5;
        }

    }
}
