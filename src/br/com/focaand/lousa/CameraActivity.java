package br.com.focaand.lousa;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
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

		preview = new CameraPreview(this,
				(SurfaceView) findViewById(R.id.surfaceView));
		preview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		((FrameLayout) findViewById(R.id.preview)).addView(preview);
		preview.setKeepScreenOn(true);

		buttonClick = (Button) findViewById(R.id.buttonClick);

		buttonClick.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				camera.takePicture(shutterCallback, rawCallback, jpegCallback);
			}
		});

		buttonClick.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				camera.autoFocus(new AutoFocusCallback() {

					@Override
					public void onAutoFocus(boolean arg0, Camera arg1) {
						camera.takePicture(shutterCallback, rawCallback,
								jpegCallback);
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
			Intent i = new Intent(CameraActivity.this, ImageTreatmentActivity.class);
            i.putExtra("photo" ,data);
            startActivity(i);
		}
	};

}
