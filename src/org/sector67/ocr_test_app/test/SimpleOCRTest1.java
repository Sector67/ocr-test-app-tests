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

public class SimpleOCRTest1 extends AndroidTestCase {

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
	

	public void testHelloWorld() {
		Log.e(TAG, "Data path is: " + DATA_PATH + "");

		Bitmap bitmap = OCRTestUtils.getTextImage("Hello World", 640, 480);
		
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);
		
		String recognizedText = baseApi.getUTF8Text();
		
		baseApi.end();

		// You now have the text in recognizedText var

		if ( lang.equalsIgnoreCase("eng") ) {
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
		}
		
		recognizedText = recognizedText.trim();
		
		assertEquals("The OCR'd text did not match the image", "Hello World", recognizedText);


	}

	
	public void testHex() {
		Log.e(TAG, "Data path is: " + DATA_PATH + "");

		Bitmap bitmap = OCRTestUtils.getTextImage("ABCD123", 640, 480);
		
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);
		
		String recognizedText = baseApi.getUTF8Text();
		
		baseApi.end();

		// You now have the text in recognizedText var

		if ( lang.equalsIgnoreCase("eng") ) {
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
		}
		
		recognizedText = recognizedText.trim();
		
		assertEquals("The OCR'd text did not match the image", "ABCD123", recognizedText);


	}

	public void testMoreHex() {
		Log.e(TAG, "Data path is: " + DATA_PATH + "");

		Bitmap bitmap = OCRTestUtils.getTextImage("AB CD 12 34\nEE FF 77 88", 640, 480);
		
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);
		
		String recognizedText = baseApi.getUTF8Text();
		
		baseApi.end();

		// You now have the text in recognizedText var

		if ( lang.equalsIgnoreCase("eng") ) {
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
			recognizedText = recognizedText.replaceAll("[ ]+", "");
		}
		
		recognizedText = recognizedText.trim();
		
		assertEquals("The OCR'd text did not match the image", "ABCD1234EEFF7788", recognizedText);
	}
	
}
