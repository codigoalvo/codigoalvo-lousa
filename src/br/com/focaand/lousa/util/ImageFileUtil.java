package br.com.focaand.lousa.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class ImageFileUtil {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_SEGMENTATION = 2;
    public static final int MEDIA_TYPE_FINAL = 3;

    /** Create a file Uri for saving an image or video */
    public static Uri getOutputMediaFileUri(int type) {
	return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(int type) {
	// To be safe, you should check that the SDCard is mounted
	// using Environment.getExternalStorageState() before doing this.

	File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "focaAndLousa");
	// This location works best if you want the created images to be shared
	// between applications and persist after your app has been uninstalled.

	// Create the storage directory if it does not exist
	if (!mediaStorageDir.exists()) {
	    if (!mediaStorageDir.mkdirs()) {
		Log.d("focaAndLousa", "failed to create directory");
		return null;
	    }
	}

	// Create a media file name
	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	File mediaFile;
	if (type == MEDIA_TYPE_IMAGE) {
	    mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
	} else if (type == MEDIA_TYPE_SEGMENTATION) {
	    mediaFile = new File(mediaStorageDir.getPath() + File.separator + "SEG_" + "FOCAAND_TEMP" + ".jpg");
	} else if (type == MEDIA_TYPE_FINAL) {
	    mediaFile = new File(mediaStorageDir.getPath() + File.separator + "FOCA_" + timeStamp + ".jpg");
	} else {
	    return null;
	}

	return mediaFile;
    }

    public static boolean saveBitmap(Bitmap bitmap, String fileName) {

	FileOutputStream out = null;
	try {
	    out = new FileOutputStream(fileName);
	    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (out != null)
		    out.close();
	    } catch (Throwable ignore) {
	    }
	}

	return true;
    }

    public static Bitmap getBitmap(String fileName, int deviceOrientation) {
	BitmapFactory.Options options = new BitmapFactory.Options();
	options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);

	if (deviceOrientation == Configuration.ORIENTATION_PORTRAIT) {
	    // Rotaciona o bitmap para exibir quando o celular estiver em portrait
	    Matrix matrix = new Matrix();
	    matrix.postRotate(90);
	    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
	    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
	    bitmap = rotatedBitmap;
	}

	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	bitmap.compress(CompressFormat.PNG, 0, byteArrayOutputStream);

	return bitmap;
    }

    public static Point getProportionalXY(int screenWidth, int screenHeight, int bitmapWidth, int bitmapHeight, int inputX, int inputY) {
	int x = (int)(inputX * bitmapWidth) / screenWidth;
	int y = (int)(inputY * bitmapHeight) / screenHeight;
	return new Point(x, y);
    }
}
