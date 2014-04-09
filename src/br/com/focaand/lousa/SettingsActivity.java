package br.com.focaand.lousa;

import br.com.focaand.lousa.util.Preferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_settings);
        EditText edtMaxResolution = (EditText)findViewById(R.id.edtResolucaoMax);
        EditText edtContraste = (EditText)findViewById(R.id.edtContraste);
        CheckBox ckbShowButtons = (CheckBox)findViewById(R.id.ckbEscondeBotoes);
        CheckBox ckbPreCameraExtraUri = (CheckBox)findViewById(R.id.ckbPreCameraExtraUri);

        edtMaxResolution.setText(Integer.toString(Preferences.getInstance().getMaxResolution()));
        edtContraste.setText(Integer.toString(Preferences.getInstance().getContraste()));
        ckbShowButtons.setChecked(Preferences.getInstance().getShowButtons());
        ckbPreCameraExtraUri.setChecked(Preferences.getInstance().getPreCameraExtraUri());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.settings, menu);
	return true;
    }

    public void onSave(View view) {
	SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = app_preferences.edit();

        int maxResolution = 800;
        int contraste = 50;
        boolean showButtons = true;
        boolean preCameraExtraUri = false;

        EditText edtMaxResolution = (EditText)findViewById(R.id.edtResolucaoMax);
        EditText edtContraste = (EditText)findViewById(R.id.edtContraste);
        CheckBox ckbShowButtons = (CheckBox)findViewById(R.id.ckbEscondeBotoes);
        CheckBox ckbPreCameraExtraUri = (CheckBox)findViewById(R.id.ckbPreCameraExtraUri);

        maxResolution = Integer.valueOf(edtMaxResolution.getText().toString());
        contraste = Integer.valueOf(edtContraste.getText().toString());
        showButtons = ckbShowButtons.isChecked();
        preCameraExtraUri = ckbPreCameraExtraUri.isChecked();

        editor.putInt("max_resolution", maxResolution);
        editor.putInt("contraste", contraste);
        editor.putBoolean("show_buttons", showButtons);
        editor.putBoolean("pre_cam_extra_uri", preCameraExtraUri);

        editor.commit();

        Preferences.getInstance().setMaxResolution(maxResolution);
        Preferences.getInstance().setContraste(contraste);
        Preferences.getInstance().setShowButtons(showButtons);
        Preferences.getInstance().setPreCameraExtraUri(preCameraExtraUri);

        finish();
    }

    public void onCancel(View view) {
	finish();
    }

}
