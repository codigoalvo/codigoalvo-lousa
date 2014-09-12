package br.com.codigoalvo.lousa;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * @author Cassio Reinaldo Amaral
 *         source: https://github.com/josnidhin/Android-Camera-Example
 */
public class CameraPreview
    extends ViewGroup
    implements SurfaceHolder.Callback {

    private final String TAG = "focaand.lousa.CameraPreview";

    SurfaceView          mSurfaceView;
    SurfaceHolder        mHolder;
    Size                 mPreviewSize;
    List<Size>           mSupportedPreviewSizes;
    Camera               mCamera;
    Context              mContext;

    public CameraPreview(Context context, SurfaceView sv) {
	super(context);
	mContext = context;

	mSurfaceView = sv;
	// addView(mSurfaceView);
	
    

	
	mHolder = mSurfaceView.getHolder();
	mHolder.addCallback(this);
	mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setCamera(Camera camera) {
	mCamera = camera;
	if (mCamera != null) {
	    mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
	    requestLayout();

	    // get Camera parameters
	    Camera.Parameters params = mCamera.getParameters();

	    List<String> focusModes = params.getSupportedFocusModes();
	    if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
		// set the focus mode
		params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		// set Camera parameters
		mCamera.setParameters(params);
	    }
	}
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// We purposely disregard child measurements because act as a
	// wrapper to a SurfaceView that centers the camera preview instead
	// of stretching it.
	final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
	final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
	setMeasuredDimension(width, height);

	if (mSupportedPreviewSizes != null) {
	    mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
	}
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
	if (changed && getChildCount() > 0) {
	    final View child = getChildAt(0);

	    final int width = r - l;
	    final int height = b - t;

	    int previewWidth = width;
	    int previewHeight = height;
	    if (mPreviewSize != null) {
		previewWidth = mPreviewSize.width;
		previewHeight = mPreviewSize.height;
	    }

	    // Center the child SurfaceView within the parent.
	    if (width * previewHeight > height * previewWidth) {
		final int scaledChildWidth = previewWidth * height / previewHeight;
		child.layout((width - scaledChildWidth) / 2, 0, (width + scaledChildWidth) / 2, height);
	    } else {
		final int scaledChildHeight = previewHeight * width / previewWidth;
		child.layout(0, (height - scaledChildHeight) / 2, width, (height + scaledChildHeight) / 2);
	    }
	}
    }

    public void surfaceCreated(SurfaceHolder holder) {
	// The Surface has been created, acquire the camera and tell it where
	// to draw.
	try {
	    if (mCamera != null) {
		mCamera.setPreviewDisplay(holder);
	    }
	} catch (IOException exception) {
	    Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
	}
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
	// Surface will be destroyed when we return, so stop the preview.
	if (mCamera != null) {
	    mCamera.stopPreview();
	}
    }

    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
	final double ASPECT_TOLERANCE = 0.1;
	double targetRatio = (double)w / h;
	if (sizes == null)
	    return null;

	Size optimalSize = null;
	double minDiff = Double.MAX_VALUE;

	int targetHeight = h;

	// Try to find an size match aspect ratio and size
	for (Size size : sizes) {
	    double ratio = (double)size.width / size.height;
	    if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
		continue;
	    if (Math.abs(size.height - targetHeight) < minDiff) {
		optimalSize = size;
		minDiff = Math.abs(size.height - targetHeight);
	    }
	}

	// Cannot find the one match the aspect ratio, ignore the requirement
	if (optimalSize == null) {
	    minDiff = Double.MAX_VALUE;
	    for (Size size : sizes) {
		if (Math.abs(size.height - targetHeight) < minDiff) {
		    optimalSize = size;
		    minDiff = Math.abs(size.height - targetHeight);
		}
	    }
	}
	return optimalSize;
    }

    /**
     * @param activity
     * @param cameraId
     * @param camera
     * 
     *            source: https://groups.google.com/forum/#!topic/android-developers/COb7v1_CtmE
     */
    public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
	android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
	android.hardware.Camera.getCameraInfo(cameraId, info);
	int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
	int degrees = 0;
	switch (rotation) {
	    case Surface.ROTATION_0:
		degrees = 0;
		break;
	    case Surface.ROTATION_90:
		degrees = 90;
		break;
	    case Surface.ROTATION_180:
		degrees = 180;
		break;
	    case Surface.ROTATION_270:
		degrees = 270;
		break;
	}

	int result;
	if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	    result = (info.orientation + degrees) % 360;
	    result = (360 - result) % 360; // compensate the mirror
	} else { // back-facing
	    result = (info.orientation - degrees + 360) % 360;
	}
	camera.setDisplayOrientation(result);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	if (mCamera != null) {
	    Camera.Parameters parameters = mCamera.getParameters();
	    parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
	    requestLayout();

	    setCameraDisplayOrientation(((Activity)mContext), 0, mCamera);

	    mCamera.setParameters(parameters);
	    mCamera.startPreview();
	}
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
	if (this.mCamera != null)
	    this.mCamera.autoFocus(new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean arg0, Camera arg1) {
		// Se quiser avisar visualmente que o autofocus terminou, implementar aqui
		//    Toast.makeText(mContext, "autoFocus", Toast.LENGTH_SHORT).show();
		}
	    });
        return super.dispatchTouchEvent(ev);
    }
    
}