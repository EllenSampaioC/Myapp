package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class CustomCircleView extends View {
    private Paint paint;
    private Paint linePaint;
    private int circleColor;
    private static final String PREFERENCES_KEY = "circle_color_pref";
    private static final String COLOR_KEY = "circle_color";

    public CustomCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        circleColor = retrieveCircleColor(context);

        linePaint = new Paint();
        linePaint.setStrokeWidth(8);
        linePaint.setColor(Color.BLACK); 
    }

    private int retrieveCircleColor(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        if (preferences.contains(COLOR_KEY)) {
            return preferences.getInt(COLOR_KEY, Color.GREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(R.color.default_circle_color, null);
        } else {
            return getResources().getColor(R.color.default_circle_color);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(circleColor);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;

        canvas.drawCircle(width / 2, height / 2, radius, paint);

        int[] radii = {radius / 4, radius / 2, (3 * radius) / 4};

        for (int r : radii) {
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeWidth(5);
            canvas.drawCircle(width / 2, height / 2, r, linePaint);
        }

        int verticalLineLength = 1080;
        linePaint.setStrokeWidth(12);
        canvas.drawLine(width / 2, height / 2 - verticalLineLength / 2, width / 2, height / 2 + verticalLineLength / 2, linePaint);

        linePaint.setStrokeWidth(6);
        int horizontalLineLength = width;
        canvas.drawLine((width - horizontalLineLength) / 2, height / 2, (width + horizontalLineLength) / 2, height / 2, linePaint);
    }

    public void setCircleColor(int color) {
        this.circleColor = color;
        invalidate();

        SharedPreferences preferences = getContext().getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(COLOR_KEY, color);
        editor.apply();
    }
}
