package com.tapfury.ghostcall;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;

/**
 * Created by Ynott on 8/24/15.
 */
public class AvatarView extends ImageView {

    private String mInitial = "J";
    private Paint mPaint;
    private Context context;

    public AvatarView(Context context) {
        super(context);
        this.context = context;
        mPaint = new Paint();
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mPaint = new Paint();
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundResource(R.drawable.circle);
        getBackground().setColorFilter(context.getResources().getColor(R.color.titleblue), PorterDuff.Mode.MULTIPLY);

        mPaint.setTextSize(dpToPx(context, 32));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
        mPaint.setColor(context.getResources().getColor(R.color.white));
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_light.ttf");
        mPaint.setTypeface(typeface);
        int xPos = (getWidth() / 2);
        int yPos = (int) ((getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
        canvas.drawText("" + mInitial, xPos, yPos, mPaint);

    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
