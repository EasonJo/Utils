package com.eason.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;
import com.eason.util.DisplayUtils;
import com.eason.util.R;

/**
 * CustomSeekBar for Radio
 * Created by Eason on 2015/4/15.
 */
public class CustomSeekBar extends SeekBar {
    private String[] numbers = new String[]{"80", "85", "90", "95", "100", "105", "110"};
    private Paint paint = new Paint();
    private Context mContext;

    public CustomSeekBar(Context context) {
        super(context);
        this.mContext = context;
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float bg_y = DisplayUtils.dipToPx(mContext, 25);
        Drawable drawable = getResources().getDrawable(R.drawable.icon_boduanzhong_720_nor);
        drawable.setBounds(0, 0, getWidth(), drawable.getIntrinsicHeight());
        canvas.translate(0, bg_y);
        drawable.draw(canvas);
        paint.setColor(Color.WHITE);
        paint.setAlpha(153);
        paint.setTextSize(DisplayUtils.sp2px(mContext, 15));


        float w = getWidth() / 36;
        //Text y-coordinate
        float y = bg_y + drawable.getIntrinsicHeight() + DisplayUtils.dipToPx(mContext, 7);

        for (int i = 0; i < numbers.length; i++) {
            Rect r = new Rect();
            paint.getTextBounds(numbers[i], 0, numbers[i].length(), r);
            //String width this method wil be more accurate than R.getTextBounds
            float str_w = paint.measureText(numbers[i]);
//            Log.v("Eason", "W:H  " + numbers[i] + " " + str_w + " : " + str_h);

            float x;
            if (i == 0) {
                x = w * 3;
            } else if (i == 6) {
                x = getWidth() - w * 3;
            } else {
                x = w * 3 + w * 5 * i;
            }
            //Draw Text
            canvas.drawText(numbers[i], x - str_w / 2, y, paint);
        }

    }
}
