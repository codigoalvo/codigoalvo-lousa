package br.com.focaand.lousa;

import br.com.focaand.lousa.util.ImageFileUtil;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class SegmentationActivity
    extends Activity {

    private String fileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_segmentation);

	Bundle extras = getIntent().getExtras();
	fileName = extras.getString("photo_path");

	Bitmap bitmap = ImageFileUtil.getBitmap(fileName, this.getResources().getConfiguration().orientation);

	ImageView image = (ImageView)findViewById(R.id.imageViewSegmentation);
	image.setImageBitmap(bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.segmentation, menu);
	return true;
    }

    public void onDoneSegmentation(View view) {
	Toast.makeText(this, "onDoneSegmentation", Toast.LENGTH_SHORT).show();
	//TODO: Neste ponto, salvar o arquivo novo já segmentado para enviar para a próxima activity
	Intent i = new Intent(SegmentationActivity.this, ImageTreatmentActivity.class);
        i.putExtra("photo_path" ,fileName);
        startActivity(i);
        finish();
    }

    public void onCancelSegmentation(View view) {
	Toast.makeText(this, "onCancelSegmentation", Toast.LENGTH_SHORT).show();
	finish();
    }

}
