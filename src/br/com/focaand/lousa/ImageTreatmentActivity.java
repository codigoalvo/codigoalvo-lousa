package br.com.focaand.lousa;

import java.io.ByteArrayOutputStream;

import br.com.focaand.lousa.util.ImageFileUtil;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageTreatmentActivity extends Activity {

	private static final String TAG = "focaand.lousa.ImageTreatmentActivity";
	private String fileName = "";

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.treatment, menu);
		return true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_treatment);

		try {
			Bundle extras = getIntent().getExtras();
			fileName = extras.getString("photo_path");

			Bitmap bitmap = ImageFileUtil.getBitmap(fileName, this.getResources().getConfiguration().orientation);

			ImageView image = (ImageView)findViewById(R.id.imageViewImageTreatment);
			image.setImageBitmap(bitmap);

//			int bitmapWidth = bitmap.getWidth();
//			int bitmapHeight = bitmap.getHeight();
//			for (int i = 0; i < bitmapWidth; i++) {
//				for (int j = 0; j < bitmapHeight; j++) {
//					Integer p = bitmap.getPixel(i, j);
//					int R = (p >> 16) & 0xff;
//					int G = (p >> 8) & 0xff;
//					int B = p & 0xff;
//				}
//			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}

	}

	public void onDoneTreatment(View view) {
	    Toast.makeText(this, "Saved: "+fileName, Toast.LENGTH_SHORT).show();
	    finish();
	}

	public void onCancelTreatment(View view) {
	    Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
	    finish();
	}

}
