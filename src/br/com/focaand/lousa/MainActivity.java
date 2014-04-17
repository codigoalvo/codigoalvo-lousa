package br.com.focaand.lousa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import br.com.focaand.lousa.util.ImageFileUtil;
import br.com.focaand.lousa.util.Preferences;

public class MainActivity
    extends Activity {

    private static final String TAG             = "focaand.lousa.MainActivity";
    private static final int SELECT_PICTURE      = 110;
    private static final int CAPTURE_FROM_CAMERA = 120;
    private static final int GET_FROM_CAMERA     = 130;
    private static Uri preCameraExtraUri         = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	Log.d(TAG, "before onCreate");
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	Typeface face = Typeface.createFromAsset(getAssets(), "fonts/OpenDyslexicAlta-Regular.otf");
	TextView lblTitle = (TextView)findViewById(R.id.lblTitle);
	lblTitle.setTypeface(face);
	loadPreferences();
	Log.d(TAG, "after onCreate");
    }
    
    private void loadPreferences() {
	SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);

        int maxResolution = app_preferences.getInt("max_resolution", 800);
        int contraste = app_preferences.getInt("contraste", 50);
        boolean resultadoPretoBranco = app_preferences.getBoolean("resultado_preto_branco", true);
        boolean showButtons = app_preferences.getBoolean("show_buttons", true);

        Preferences.getInstance().setMaxResolution(maxResolution);
        Preferences.getInstance().setContraste(contraste);
        Preferences.getInstance().setResultadoPretoBranco(resultadoPretoBranco);
        Preferences.getInstance().setShowButtons(showButtons);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	    case R.id.action_main_about:
		Toast.makeText(this, "APP build version date: "+ImageFileUtil.getApkBuildTimeStamp(this), Toast.LENGTH_SHORT).show();
		return true;
	    default:
		return super.onOptionsItemSelected(item);
	}
    }

    public void onAbout(View view) {
	Toast.makeText(this, "APP build version date: "+ImageFileUtil.getApkBuildTimeStamp(this), Toast.LENGTH_SHORT).show();
    }
    
    public void onGetFromCamera(View view) {
	// Intent intent = new Intent(this, CameraActivity.class);
	// startActivityForResult(intent, CAPTURE_FROM_CAMERA);
	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	preCameraExtraUri = ImageFileUtil.getOutputMediaFileUri(ImageFileUtil.MEDIA_TYPE_CAMERA);
	intent.putExtra(MediaStore.EXTRA_OUTPUT, preCameraExtraUri);
	startActivityForResult(intent, GET_FROM_CAMERA);
    }

    public void onGetFromGalery(View view) {
	Intent intent = new Intent();
	intent.setType("image/*");
	intent.setAction(Intent.ACTION_GET_CONTENT);
	startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_picture)), SELECT_PICTURE);
    }
    
    public void onSettings(View view) {
	Intent intent = new Intent(this, SettingsActivity.class);
	startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	Log.d(TAG, "before onActivityResult");
	if (resultCode == RESULT_OK) {
	    String selectedImagePath = null;
	    if (requestCode == SELECT_PICTURE) {
		Uri selectedImageUri = data.getData();
		if (selectedImageUri != null)
		    selectedImagePath = getPath(selectedImageUri);
	    } else if (requestCode == CAPTURE_FROM_CAMERA) {
		selectedImagePath = data.getStringExtra("photo_path");
	    } else if (requestCode == GET_FROM_CAMERA) {
		Uri selectedImageUri = null;
		if (preCameraExtraUri != null) {
		    selectedImageUri = preCameraExtraUri;
		    if (selectedImageUri.getPath() == null  ||  selectedImageUri.getPath().isEmpty())
			selectedImagePath = getPath(selectedImageUri);
		    else
			selectedImagePath = selectedImageUri.getPath();
		}
	    }

	    if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
		final String finalSelectedPath = selectedImagePath;
		mLockScreenRotation();
		final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
		dialog.setTitle(R.string.preparing_image);
		dialog.setMessage(getResources().getString(R.string.please_wait));
		dialog.show();

		new Thread(new Runnable() {

		    @Override
		    public void run() {
			final String preparedImagePath = ImageFileUtil.prepareFile(finalSelectedPath);
			runOnUiThread(new Runnable() {
			    @Override
			    public void run() {
				dialog.dismiss();
				MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
				Intent i = new Intent(MainActivity.this, SegmentationActivity.class);
				i.putExtra("photo_path", preparedImagePath);
				startActivity(i);
			    }
			});
		    }
		}).start();

	    } else {
		Log.d(TAG, "error1 onActivityResult");
		Toast.makeText(this, R.string.erro_imagem_camera, Toast.LENGTH_SHORT).show();
	    }
	} else if (resultCode == RESULT_CANCELED) {
	    Log.d(TAG, "canceled onActivityResult");
	    Toast.makeText(this, R.string.acao_cancelada_usuario, Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "error2 onActivityResult");
            Toast.makeText(this, R.string.erro_imagem_camera, Toast.LENGTH_SHORT).show();
        }
	Log.d(TAG, "after onActivityResult");
    }

    public String getPath(Uri uri) {
	Log.d(TAG, "before getPath");
	String[] projection = {MediaStore.Images.Media.DATA};
	Cursor cursor = managedQuery(uri, projection, null, null, null);
	int column_index = -1;
	column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	cursor.moveToFirst();
	Log.d(TAG, "afetr getPath");
	return cursor.getString(column_index);
    }

    /**
     * @autor: http://eigo.co.uk/labs/lock-screen-orientation-in-android/
     * // See more at: http://eigo.co.uk/labs/lock-screen-orientation-in-android/#sthash.pbM5Pkf3.dpuf
     */
    private void mLockScreenRotation() {   // Stop the screen orientation changing during an event
	switch (this.getResources().getConfiguration().orientation)     {
	    case Configuration.ORIENTATION_PORTRAIT:
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		break;
	    case Configuration.ORIENTATION_LANDSCAPE:
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		break;
	}
    } 

}
