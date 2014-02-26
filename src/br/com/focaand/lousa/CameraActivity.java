package br.com.focaand.lousa;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import br.com.focaand.lousa.util.CameraUtil;
import br.com.focaand.lousa.util.ImageFileUtil;

/**
 * @author Cassio Reinaldo Amaral source:
 *         https://github.com/josnidhin/Android-Camera-Example
 */
public class CameraActivity extends Activity {

	private static final String TAG = "focaand.lousa.CamTestActivity";
	CameraPreview preview;
	ImageButton buttonClick;
	Camera camera;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_camera);

		preview = new CameraPreview(this, (SurfaceView)findViewById(R.id.surfaceView));
		preview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		((FrameLayout)findViewById(R.id.preview)).addView(preview);
		preview.setKeepScreenOn(true);

		buttonClick = (ImageButton)findViewById(R.id.buttonClick);

		buttonClick.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				System.out.println(camera.getParameters().getJpegQuality());
				System.out.println(camera.getParameters().getPictureSize().width + " / " + camera.getParameters().getPictureSize().height);

				camera.takePicture(shutterCallback, rawCallback, jpegCallback);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// preview.camera = Camera.open();
		if (CameraUtil.checkCameraHardware(this)) {
			camera = CameraUtil.getCameraInstance();
			Size preferedSize = CameraUtil.getPreferredSupportedResolution(900, 1500, camera.getParameters().getSupportedPictureSizes());
			Parameters parameters = camera.getParameters();
			parameters.set("jpeg-quality", 70);
			parameters.setJpegQuality(70);
			parameters.setPictureFormat(PixelFormat.JPEG);
			parameters.setPictureSize(preferedSize.width, preferedSize.height);
			camera.setParameters(parameters);
			camera.startPreview();
			preview.setCamera(camera);
		}
	}

	@Override
	protected void onPause() {
		if (CameraUtil.checkCameraHardware(this)) {
		    try {
			if (camera != null) {
			    camera.stopPreview();
			    preview.setCamera(null);
			    camera.release();
			    camera = null;
			}
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
		super.onPause();
	}

	ShutterCallback shutterCallback = new ShutterCallback() {

		public void onShutter() {

		}
	};

	PictureCallback rawCallback = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {

		}
	};

	PictureCallback jpegCallback = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null;

			try {
				// Write to SD Card
				String fileName =  ImageFileUtil.getOutputMediaFileUri(ImageFileUtil.MEDIA_TYPE_IMAGE).getPath();
				outStream = new FileOutputStream(fileName);
				outStream.write(data);
				outStream.close();
				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);

				Intent returnIntent = new Intent();
				returnIntent.putExtra("photo_path", fileName);
				setResult(RESULT_OK,returnIntent);
				camera.stopPreview();
				preview.setCamera(null);
				camera.release();
				if (camera == CameraActivity.this.camera) 
				    CameraActivity.this.camera = null;
				finish();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
			Log.d(TAG, "onPictureTaken - jpeg");
		}
	};

}
