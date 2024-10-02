package com.example.splash1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomPieChartView extends View {

    private Paint paint;
    private float[] percentages = {25f, 35f, 40f}; // Example data
    private String[] labels = {"Category A", "Category B", "Category C"};
    private int[] colors = {Color.RED, Color.GREEN, Color.BLUE};

    public CustomPieChartView(Context context) {
        super(context);
        init();
    }

    public CustomPieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float startAngle = 0f;
        float sweepAngle;

        int width = getWidth();
        int height = getHeight();
        int diameter = Math.min(width, height);
        int radius = diameter / 2;
        int cx = width / 2;
        int cy = height / 2;

        for (int i = 0; i < percentages.length; i++) {
            paint.setColor(colors[i]);
            sweepAngle = 360 * (percentages[i] / 100);
            canvas.drawArc(cx - radius, cy - radius, cx + radius, cy + radius, startAngle, sweepAngle, true, paint);
            startAngle += sweepAngle;
        }
    }
}
