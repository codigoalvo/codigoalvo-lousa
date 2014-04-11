package br.com.focaand.lousa;

import br.com.focaand.lousa.util.ImageFileUtil;
import br.com.focaand.lousa.util.Preferences;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity
    extends Activity {

    private static final int SELECT_PICTURE      = 1;
    private static final int CAPTURE_FROM_CAMERA = 2;
    private static final int GET_FROM_CAMERA     = 3;
    private static Uri preCameraExtraUri         = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	Typeface face = Typeface.createFromAsset(getAssets(), "fonts/OpenDyslexicAlta-Regular.otf");
	TextView lblTitle = (TextView)findViewById(R.id.lblTitle);
	lblTitle.setTypeface(face);
	loadPreferences();
    }
    
    private void loadPreferences() {
	SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);

        int maxResolution = app_preferences.getInt("max_resolution", 800);
        int contraste = app_preferences.getInt("contraste", 50);
        boolean resultadoPretoBranco = app_preferences.getBoolean("resultado_preto_branco", true);
        boolean showButtons = app_preferences.getBoolean("show_buttons", true);
        boolean preCameraExtraUri = app_preferences.getBoolean("pre_cam_extra_uri", true);

        Preferences.getInstance().setMaxResolution(maxResolution);
        Preferences.getInstance().setContraste(contraste);
        Preferences.getInstance().setResultadoPretoBranco(resultadoPretoBranco);
        Preferences.getInstance().setShowButtons(showButtons);
        Preferences.getInstance().setPreCameraExtraUri(preCameraExtraUri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }

    public void onGetFromCamera(View view) {
	// Intent intent = new Intent(this, CameraActivity.class);
	// startActivityForResult(intent, CAPTURE_FROM_CAMERA);
	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	if (Preferences.getInstance().getPreCameraExtraUri()) {
	    preCameraExtraUri = ImageFileUtil.getOutputMediaFileUri(ImageFileUtil.MEDIA_TYPE_CAMERA);
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, preCameraExtraUri);
	}
	startActivityForResult(intent, GET_FROM_CAMERA);
    }

    public void onGetFromGalery(View view) {
	Intent intent = new Intent();
	intent.setType("image/*");
	intent.setAction(Intent.ACTION_GET_CONTENT);
	startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }
    
    public void onSettings(View view) {
	Intent intent = new Intent(this, SettingsActivity.class);
	startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (resultCode == RESULT_OK) {
	    String selectedImagePath = null;
	    if (requestCode == SELECT_PICTURE) {
		Uri selectedImageUri = data.getData();
		selectedImagePath = getPath(selectedImageUri);
	    } else if (requestCode == CAPTURE_FROM_CAMERA) {
		selectedImagePath = data.getStringExtra("photo_path");
	    } else if (requestCode == GET_FROM_CAMERA) {
		Uri selectedImageUri = null;
		if (data != null &&  data.getData() != null) {
		    selectedImageUri = data.getData();
		    if (selectedImageUri != null)
			selectedImagePath = getPath(selectedImageUri);
		} else if (preCameraExtraUri != null) {
		    selectedImageUri = preCameraExtraUri;
		    selectedImagePath = selectedImageUri.getPath();
		}
	    }

	    if (selectedImagePath != null && !selectedImagePath.isEmpty()) {

		selectedImagePath = ImageFileUtil.prepareFile(selectedImagePath);
		Intent i = new Intent(MainActivity.this, SegmentationActivity.class);
		i.putExtra("photo_path", selectedImagePath);
		startActivity(i);
	    } else {
		Toast.makeText(this, R.string.erro_salvar_seg, Toast.LENGTH_SHORT).show();
	    }
	}
    }

    public String getPath(Uri uri) {
	String[] projection = {MediaStore.Images.Media.DATA};
	Cursor cursor = managedQuery(uri, projection, null, null, null);
	int column_index = -1;
	column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	cursor.moveToFirst();
	return cursor.getString(column_index);
    }

}
