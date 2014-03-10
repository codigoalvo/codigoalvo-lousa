package br.com.focaand.lousa;

import java.util.HashMap;

import tcc.GrayScaleImage;
import tcc.IGrayScaleImage;
import tcc.IRGBImage;
import tcc.operators.MorphlogicalOperators;
import tcc.operators.OperatorsByIFT;
import tcc.utils.AdjacencyRelation;
import br.com.focaand.lousa.util.ImageFileUtil;
import android.R.integer;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageTreatmentActivity extends Activity {

	private static final String TAG = "focaand.lousa.ImageTreatmentActivity";
	private String fileName = "";
	int imgMarcador[][];
	AdjacencyRelation adj = AdjacencyRelation.getCircular(1.5f);
	
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
			

			
			
			int bitmapWidth = bitmap.getWidth();
			int bitmapHeight = bitmap.getHeight();
			int pixels[][] = new int[bitmapWidth][bitmapHeight];
			
			int imgMarcador[][] = new int[bitmapWidth][bitmapHeight];
			for(int x=0; x < bitmapWidth; x++)
				for(int y=0; y < bitmapHeight; y++){
						imgMarcador[x][y] = -1;
				}
					
			
			
			for (int i = 0; i < bitmapWidth; i++) {
				for (int j = 0; j < bitmapHeight; j++) {
					Integer p = bitmap.getPixel(i, j);
					int R = (p >> 16) & 0xff;
					int G = (p >> 8) & 0xff;
					int B = p & 0xff;
					pixels[i][j] = (int) Math.round(.299*R + .587*G + .114*B); 
				}
			}
			
			
			IGrayScaleImage imgGrad = MorphlogicalOperators.gradient(new GrayScaleImage(pixels),adj);
			
			HashMap<Integer, Integer> labels = new HashMap<Integer, Integer>();
			int label = 1;
			IGrayScaleImage imgM = new GrayScaleImage(bitmapWidth, bitmapHeight);
			for(int x=0; x < bitmapWidth; x++){
				for(int y=0; y < bitmapHeight; y++){
					if(imgMarcador[x][y] != -1){
						if(labels.containsKey(imgMarcador[x][y])){
							imgM.setPixel(x, y, labels.get(imgMarcador[x][y]));
						}else{
							labels.put(imgMarcador[x][y], label++);
							imgM.setPixel(x, y, labels.get(imgMarcador[x][y]));
						}
					}else{
						imgM.setPixel(x,  y, -1);
					}
				}
			}
			
			IGrayScaleImage imgWS = OperatorsByIFT.watershedByMarker(adj, imgGrad, imgM);
			
			IRGBImage imgLabel = imgWS.randomColor();
			
			int[][][] pixelsImageLabel = imgLabel.getPixels();
			
			for (int i = 0; i < bitmapWidth; i++) {
				for (int j = 0; j < bitmapHeight; j++) {
					int rgb = 255;
					rgb = (rgb << 8) + pixelsImageLabel[0][i][j];
					rgb = (rgb << 8) + pixelsImageLabel[1][i][j];
					rgb = (rgb << 8) + pixelsImageLabel[2][i][j];
					bitmap.setPixel(i, j, rgb);
					
				}
			}
			
			image.setImageBitmap(bitmap);
			
			
			
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
