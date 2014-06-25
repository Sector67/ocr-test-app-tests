package org.sector67.ocr_test_app.test;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

public class SimpleOCRTest extends AndroidTestCase {

	private static final String TAG = "ocr-test-app-tests";

	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/ocr-test-app/";
	public static final String lang = "eng";

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/*
	 * Make a dynamic test image
	 */
    private static Bitmap getTextImage(String text, int width, int height) {
        final Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Paint paint = new Paint();
        final Canvas canvas = new Canvas(bmp);

        canvas.drawColor(Color.WHITE);

        paint.setColor(Color.BLACK);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize(24.0f);
        canvas.drawText(text, width / 2, height / 2, paint);

        return bmp;
    }
    
	public void testHelloWorld() {
		Log.e(TAG, "Data path is: " + DATA_PATH + "");

		Bitmap bitmap = getTextImage("Hello World", 640, 480);
		// Convert to ARGB_8888, required by tess

		// _image.setImageBitmap( bitmap );
		
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);
		
		String recognizedText = baseApi.getUTF8Text();
		
		baseApi.end();

		// You now have the text in recognizedText var, you can do anything with it.
		// We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
		// so that garbage doesn't make it to the display.

		if ( lang.equalsIgnoreCase("eng") ) {
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
		}
		
		recognizedText = recognizedText.trim();
		
		assertEquals("The OCR'd text did not match the image", "Hello World", recognizedText);


	}

	
	public void testHex() {
		Log.e(TAG, "Data path is: " + DATA_PATH + "");

		Bitmap bitmap = getTextImage("ABCD123", 640, 480);
		// Convert to ARGB_8888, required by tess

		// _image.setImageBitmap( bitmap );
		
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);
		
		String recognizedText = baseApi.getUTF8Text();
		
		baseApi.end();

		// You now have the text in recognizedText var, you can do anything with it.
		// We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
		// so that garbage doesn't make it to the display.

		if ( lang.equalsIgnoreCase("eng") ) {
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
		}
		
		recognizedText = recognizedText.trim();
		
		assertEquals("The OCR'd text did not match the image", "ABCD123", recognizedText);


	}

}
