package br.com.focaand.lousa;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
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
import android.widget.Button;
import android.widget.FrameLayout;
import br.com.focaand.lousa.util.CameraUtil;

/**
 * @author Cassio Reinaldo Amaral source:
 *         https://github.com/josnidhin/Android-Camera-Example
 */
public class CameraActivity extends Activity {

	private static final String TAG = "focaand.lousa.CamTestActivity";
	CameraPreview preview;
	Button buttonClick;
	Camera camera;
	String fileName;

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

		buttonClick = (Button)findViewById(R.id.buttonClick);

		buttonClick.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				System.out.println(camera.getParameters().getJpegQuality());
				System.out.println(camera.getParameters().getPictureSize().width + " / " + camera.getParameters().getPictureSize().height);

				camera.takePicture(shutterCallback, rawCallback, jpegCallback);
			}
		});

		buttonClick.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				camera.autoFocus(new AutoFocusCallback() {

					@Override
					public void onAutoFocus(boolean arg0, Camera arg1) {
						camera.takePicture(shutterCallback, rawCallback, jpegCallback);
					}
				});
				return true;
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// preview.camera = Camera.open();
		if (CameraUtil.checkCameraHardware(this)) {
			camera = CameraUtil.getCameraInstance();
			camera.getParameters().setPictureSize(1280, 720);
			camera.getParameters().setJpegQuality(40);
			// System.out.println(camera.getParameters().getJpegQuality());
			// System.out.println(camera.getParameters().getSupportedPictureSizes()
			// getPictureSize().width+" / "+camera.getParameters().getPictureSize().height);
			camera.startPreview();
			preview.setCamera(camera);
		}
	}

	@Override
	protected void onPause() {
		if (CameraUtil.checkCameraHardware(this)) {
			if (camera != null) {
				camera.stopPreview();
				preview.setCamera(null);
				camera.release();
				camera = null;
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

				fileName = String.format("/sdcard/focaand/%d.jpg", System.currentTimeMillis());
				outStream = new FileOutputStream(fileName);
				outStream.write(data);
				outStream.close();
				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
				Intent i = new Intent(CameraActivity.this, ImageTreatmentActivity.class);
				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
				i.putExtra("photo_path", fileName);
				startActivity(i);
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
