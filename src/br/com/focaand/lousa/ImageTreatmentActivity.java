package br.com.focaand.lousa;

import java.io.ByteArrayOutputStream;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

public class ImageTreatmentActivity extends Activity {

	private static final String TAG = "focaand.lousa.ImageTreatmentActivity";
	
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
//			byte[] photo = extras.getByteArray("photo");
//			Bitmap bitmap  = BitmapFactory.decodeByteArray(photo, 0, photo.length);
			String fileName = extras.getString("photo_path");
			
			
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);    	    
    	   
            
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 0, byteArrayOutputStream);
//            byte[] imageBytes = byteArrayOutputStream.toByteArray();

			
			
//			Bitmap bitmap = BitmapFactory.decodeFile(fileName);
			
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

}
