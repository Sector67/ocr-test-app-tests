package org.sector67.ocr_test_app.test;

import java.util.Random;

import org.sector67.otp.EncryptionException;
import org.sector67.otp.cipher.OneTimePadCipher;
import org.sector67.otp.key.InMemoryKeyStore;
import org.sector67.otp.key.KeyException;
import org.sector67.otp.utils.BaseUtils;

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

public class EncryptingOCRTest extends AndroidTestCase {

	InMemoryKeyStore store;

	private static final String TAG = "ocr-test-app-tests";

	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/ocr-test-app/";
	public static final String lang = "eng";

	protected void setUp() throws Exception {
		InMemoryKeyStore store = new InMemoryKeyStore();
		byte[] pseudoRandom = new byte[1000];
		//use a fixed seed for repeatable unit tests
		Random r = new Random(1);
		r.nextBytes(pseudoRandom);
		store.addKey("encrypt-key", pseudoRandom, 0);
		store.copyKey("encrypt-key", "decrypt-key");
		this.store = store;
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	

    
	public void testSmallishMessage() throws Exception {
		Log.e(TAG, "Data path is: " + DATA_PATH + "");

		// encrypt some data
		String original = "Smallish message";
		OneTimePadCipher cipher = new OneTimePadCipher(store);
		byte[] encrypted = cipher.encrypt("encrypt-key", original);
		String chunked = BaseUtils.getChunkedBase16(encrypted);
		
		Log.i(TAG, "chunked output: " + chunked);

		//make an image out of it
		
		Bitmap bitmap = OCRTestUtils.getTextImage(chunked, 640, 480);
		
		//OCR the image
		
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "0123456789ABCDEF");
		baseApi.setImage(bitmap);
		
		String recognizedText = baseApi.getUTF8Text();
		
		baseApi.end();

		Log.i(TAG, "chunked input: " + recognizedText);

		if ( lang.equalsIgnoreCase("eng") ) {
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
		}
		
		//decrypt the text
		byte[] decoded = BaseUtils.base16ToBytes(recognizedText);
		String decrypted = cipher.decrypt("decrypt-key", decoded);
		
		assertEquals("The OCR'd text did not match the image", original, decrypted);


	}

	public void testMediumMessage() throws Exception {
		Log.e(TAG, "Data path is: " + DATA_PATH + "");

		// encrypt some data
		String original = "A somewhat larger message";
		OneTimePadCipher cipher = new OneTimePadCipher(store);
		byte[] encrypted = cipher.encrypt("encrypt-key", original);
		String chunked = BaseUtils.getChunkedBase16(encrypted);
		
		Log.i(TAG, "chunked output: " + chunked);

		//make an image out of it
		
		Bitmap bitmap = OCRTestUtils.getTextImage(chunked, 1024, 480);
		
		//OCR the image
		
		TessBaseAPI baseApi = new TessBaseAPI();
		//attempt to limit the characters
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "0123456789ABCDEF");
		baseApi.setImage(bitmap);
		
		String recognizedText = baseApi.getUTF8Text();
		
		baseApi.end();

		Log.i(TAG, "chunked input: " + recognizedText);

		if ( lang.equalsIgnoreCase("eng") ) {
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
		}
		
		//decrypt the text
		byte[] decoded = BaseUtils.base16ToBytes(recognizedText);
		String decrypted = cipher.decrypt("decrypt-key", decoded);
		
		assertEquals("The OCR'd text did not match the image", original, decrypted);


	}

}
