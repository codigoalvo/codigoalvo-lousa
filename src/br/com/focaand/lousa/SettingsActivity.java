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
        CheckBox ckbResultadoPretoBranco = (CheckBox)findViewById(R.id.ckbResultadoPretoBranco);
        CheckBox ckbShowButtons = (CheckBox)findViewById(R.id.ckbEscondeBotoes);

        edtMaxResolution.setText(Integer.toString(Preferences.getInstance().getMaxResolution()));
        edtContraste.setText(Integer.toString(Preferences.getInstance().getContraste()));
        ckbResultadoPretoBranco.setChecked(Preferences.getInstance().getResultadoPretoBranco());
        ckbShowButtons.setChecked(Preferences.getInstance().getShowButtons());
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
        boolean resultadoPretoBranco = true;
        boolean showButtons = true;

        EditText edtMaxResolution = (EditText)findViewById(R.id.edtResolucaoMax);
        EditText edtContraste = (EditText)findViewById(R.id.edtContraste);
        CheckBox ckbResultadoPretoBranco = (CheckBox)findViewById(R.id.ckbResultadoPretoBranco);
        CheckBox ckbShowButtons = (CheckBox)findViewById(R.id.ckbEscondeBotoes);

        maxResolution = Integer.valueOf(edtMaxResolution.getText().toString());
        contraste = Integer.valueOf(edtContraste.getText().toString());
        resultadoPretoBranco = ckbResultadoPretoBranco.isChecked();
        showButtons = ckbShowButtons.isChecked();

        editor.putInt("max_resolution", maxResolution);
        editor.putInt("contraste", contraste);
        editor.putBoolean("resultado_preto_branco", resultadoPretoBranco);
        editor.putBoolean("show_buttons", showButtons);

        editor.commit();

        Preferences.getInstance().setMaxResolution(maxResolution);
        Preferences.getInstance().setContraste(contraste);
        Preferences.getInstance().setResultadoPretoBranco(resultadoPretoBranco);
        Preferences.getInstance().setShowButtons(showButtons);

        finish();
    }

    public void onCancel(View view) {
	finish();
    }

}
