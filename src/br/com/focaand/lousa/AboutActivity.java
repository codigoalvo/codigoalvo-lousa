package br.com.focaand.lousa;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_about);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	return false;
    }

    public void onFechar(View view) {
	finish();
    }

}
