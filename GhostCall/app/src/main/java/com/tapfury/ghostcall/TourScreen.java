package com.tapfury.ghostcall;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

public class TourScreen extends FragmentActivity {

    private ViewPager viewPager;
    private TutorialAdapter adapter;
    private SoundPool soundPool;
    private MediaPlayer mediaPlayer;
    private int soundId;

    Button skipButton;

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


        skipButton = (Button) findViewById(R.id.skipButton);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TourScreen.this, VerificationScreen.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mediaPlayer = MediaPlayer.create(this, R.raw.ghost_call_loop);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ensureMediaPlayerDeath();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backtoStart = new Intent(TourScreen.this, StartScreen.class);
        startActivity(backtoStart);
        finish();
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageSelected(int position) {

            ratingBar.setProgress(position + 1);
            if(position == 4) {
                skipButton.setVisibility(View.VISIBLE);
            } else {
                skipButton.setVisibility(View.INVISIBLE);
            }

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
                    return TutorialImageFragment.newInstance(R.drawable.tutorial_page_six);
            }
            throw new IllegalArgumentException("unknown fragment position");
        }

        @Override
        public int getCount() {
            return 5;
        }

    }

    private void ensureMediaPlayerDeath() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
