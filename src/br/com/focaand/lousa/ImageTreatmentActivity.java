package br.com.focaand.lousa;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

public class ImageTreatmentActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_treatment);
		
		Bundle extras = getIntent().getExtras();
		byte[] photo = extras.getByteArray("photo");
		Bitmap bitmap  = BitmapFactory.decodeByteArray(photo, 0, photo.length);
		
		
		ImageView image = (ImageView)findViewById(R.id.imageViewImageTreatment);
		image.setImageBitmap(bitmap);
		
		
		
		
		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();
		for (int i = 0; i < bitmapWidth; i++) {
			for (int j = 0; j < bitmapHeight; j++) {
				Integer p = bitmap.getPixel(i, j);
				int R = (p >> 16) & 0xff;
				int G = (p >> 8) & 0xff;
				int B = p & 0xff;
			}
		}
		
		
	}

}
