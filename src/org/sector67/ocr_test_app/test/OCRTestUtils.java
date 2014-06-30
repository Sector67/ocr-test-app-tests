package org.sector67.ocr_test_app.test;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

public class OCRTestUtils {
	
    public static Bitmap getTextImage(String text, int width, int height) {
    	return getTextImage(text, 24.0f, width, height);
    }
	/*
	 * Make a dynamic test image
	 */
    public static Bitmap getTextImage(String text, float size, int width, int height) {
        final Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Paint paint = new Paint();
        final Canvas canvas = new Canvas(bmp);

        canvas.drawColor(Color.WHITE);

        paint.setColor(Color.BLACK);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize(size);
        paint.setTypeface(Typeface.DEFAULT);
        canvas.drawText(text, width / 2, height / 2, paint);

        return bmp;
    }
    
}
