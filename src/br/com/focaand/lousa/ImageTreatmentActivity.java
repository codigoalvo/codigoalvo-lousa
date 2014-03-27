package br.com.focaand.lousa;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import tcc.GrayScaleImage;
import tcc.IGrayScaleImage;
import tcc.IRGBImage;
import tcc.operators.MorphlogicalOperators;
import tcc.operators.OperatorsByIFT;
import tcc.utils.AdjacencyRelation;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.focaand.lousa.util.ImageFileUtil;

public class ImageTreatmentActivity extends Activity {

    private static final String TAG = "focaand.lousa.ImageTreatmentActivity";
    private String pictureFileName = "";
    private String segmentFileName = "";
    int imgMarcador[][];
    AdjacencyRelation adj = AdjacencyRelation.getCircular(1.5f);
    Bitmap picture;

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
	    pictureFileName = extras.getString("photo_path");

	    segmentFileName = extras.getString("segment_path");

	    picture = ImageFileUtil.getBitmap(pictureFileName);

	    Bitmap segmentation = ImageFileUtil.getBitmap(segmentFileName);

	    ImageView image = (ImageView)findViewById(R.id.imageViewImageTreatment);

	    int bitmapWidth = picture.getWidth();
	    int bitmapHeight = picture.getHeight();
	    int pixels[][] = new int[bitmapWidth][bitmapHeight];

	    int imgMarcador[][] = new int[segmentation.getWidth()][segmentation.getHeight()];
	    for (int x = 0; x < segmentation.getWidth(); x++)
		for (int y = 0; y < segmentation.getHeight(); y++) {
			int pixelSegmentation = segmentation.getPixel(x, y);

			if(pixelSegmentation == Color.BLUE || pixelSegmentation == Color.RED)
				imgMarcador[x][y] = pixelSegmentation;
			else
				imgMarcador[x][y] = -1;
		}
	    
	    

	    for (int i = 0; i < bitmapWidth; i++) {
		for (int j = 0; j < bitmapHeight; j++) {
		    Integer p = picture.getPixel(i, j);
		    int R = (p >> 16) & 0xff;
		    int G = (p >> 8) & 0xff;
		    int B = p & 0xff;
		    pixels[i][j] = (int)Math.round(.299 * R + .587 * G + .114 * B);
		}
	    }

	    IGrayScaleImage imgGrad = MorphlogicalOperators.gradient(new GrayScaleImage(pixels), adj);

	    HashMap<Integer, Integer> labels = new HashMap<Integer, Integer>();
	    int label = 1;
	    IGrayScaleImage imgM = new GrayScaleImage(bitmapWidth, bitmapHeight);
	    System.out.println("bitmapWidth "+bitmapWidth+"  -  bitmapHeight "+bitmapHeight);
	    System.out.println("imgMarcador "+imgMarcador.length+"  -  imgMarcador[0] "+imgMarcador[0].length);
	    for (int x = 0; x < bitmapWidth; x++) {
		for (int y = 0; y < bitmapHeight; y++) {
		    if (imgMarcador[y][x] != -1) {
			if (labels.containsKey(imgMarcador[y][x])) {
			    imgM.setPixel(x, y, labels.get(imgMarcador[y][x]));
			} else {
			    labels.put(imgMarcador[y][x], label++);
			    imgM.setPixel(x, y, labels.get(imgMarcador[y][x]));
			}
		    } else {
			imgM.setPixel(x, y, -1);
		    }
		}
	    }

	    IGrayScaleImage imgWS = OperatorsByIFT.watershedByMarker(adj, imgGrad, imgM);


	    int[][] pixelsImageLabel = imgWS.getPixels();

	    for (int i = 0; i < bitmapWidth; i++) {
		for (int j = 0; j < bitmapHeight; j++) {
			if(pixelsImageLabel[i][j] == 1){
				int rgb = 255;
			    rgb = (rgb << 8) + 255;
			    rgb = (rgb << 8) + 255;
			    rgb = (rgb << 8) + 255;
			    picture.setPixel(i, j, rgb);
			}

		}
	    }

	    image.setImageBitmap(picture);

	} catch (Exception exc) {
	    exc.printStackTrace();
	}

    }

    public void onDoneTreatment(View view) {
	String finalFileName = ImageFileUtil.getOutputMediaFileUri(ImageFileUtil.MEDIA_TYPE_FINAL).getPath();
	if (picture != null  &&  finalFileName != null  &&  !finalFileName.isEmpty()) {
	    boolean saveOk = ImageFileUtil.saveBitmap(picture, finalFileName);
	    if (saveOk) {
		Toast.makeText(this, "Saved: " + finalFileName, Toast.LENGTH_SHORT).show();
	    	finish();
	    } else {
		Toast.makeText(this, "Erro ao salvar: " + finalFileName, Toast.LENGTH_SHORT).show();
	    }
	} else {
	    Toast.makeText(this, "N�o � poss�vel salvar!", Toast.LENGTH_SHORT).show();
	}
    }

    public void onCancelTreatment(View view) {
	Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
	finish();
    }

}
