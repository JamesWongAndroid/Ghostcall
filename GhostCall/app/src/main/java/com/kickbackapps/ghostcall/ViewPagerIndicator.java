package com.kickbackapps.ghostcall;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by James on 6/29/15.
 */
public class ViewPagerIndicator extends RadioGroup {

    private int numIndicators;
    private int progress;

    public ViewPagerIndicator(Context context) {
        super(context);
        init();
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        setOrientation(HORIZONTAL);
    }

    public void setNumIndicators(int value) {

        this.removeAllViews();

        for (int i = 0; i < value; i++) {
            final RadioButton radioButton = createRadioButton(i);
            this.addView(radioButton);
        }

        check(progress + 1);

    }

    public void setProgress(int value) {
        int count = getChildCount();
        if (value > 0 && value <= count) {
            progress = value;
            check(value);
        } else {
            throw new IllegalArgumentException("not a valid progress");
        }
    }

    private RadioButton createRadioButton(int position) {


        final RadioButton radioButton = new RadioButton(getContext());
        radioButton.setId(position + 1);
        radioButton.setBackgroundResource(0);
        radioButton.setButtonDrawable(R.drawable.indicator);
        int size = getResources().getDimensionPixelSize(R.dimen.indicator_size);
        final LayoutParams params = new LayoutParams(size, size);
        int padding = getResources().getDimensionPixelSize(R.dimen.indicator_padding);
        params.setMargins(padding,0,padding,0);
        radioButton.setLayoutParams(params);
        return radioButton;

    }

}
