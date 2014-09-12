package br.com.codigoalvo.lousa.util;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Size;

/**
 * @author Cassio Reinaldo Amaral - cassio.amaral@gmail.com
 * @since 2013/12/06
 *        source: http://developer.android.com/guide/topics/media/camera.html
 */
public class CameraUtil {

    /** Check if this device has a camera */
    public static boolean checkCameraHardware(Context context) {
	if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
	    // this device has a camera
	    return true;
	} else {
	    // no camera on this device
	    return false;
	}
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance() {
	Camera camera = null;
	try {
	    camera = Camera.open(); // attempt to get a Camera instance
	} catch (Exception e) {
	    // Camera is not available (in use or does not exist)
	}
	return camera; // returns null if camera is unavailable
    }

    public static Size getPreferredSupportedResolution(int widthMin, int widthMax, List<Size> supportedPictureSizes) {
	for (int cii = supportedPictureSizes.size()-1; cii >=0; cii--) {
	    Size size = supportedPictureSizes.get(cii);
	    int reference = (size.width > size.height)?size.width:size.height;
	    if (reference > widthMin  &&  reference < widthMax) {
		return size;
	    }
	}
	return supportedPictureSizes.get(supportedPictureSizes.size()-1);
    }

}
