package br.com.focaand.lousa;

import java.util.HashMap;

import tcc.GrayScaleImage;
import tcc.IGrayScaleImage;
import tcc.operators.MorphlogicalOperators;
import tcc.operators.OperatorsByIFT;
import tcc.utils.AdjacencyRelation;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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
    Bitmap finalPicture;

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

	    System.out.println("*focaAndLousa* - ImageTreatment pictureFileName: "+pictureFileName);
	    System.out.println("*focaAndLousa* - ImageTreatment segmentFileName: "+segmentFileName);

	    Bitmap aPicture = ImageFileUtil.getBitmap(pictureFileName);
	    System.out.println("*focaAndLousa* - ImageTreatment pictureFileSize: "+aPicture.getWidth()+"x"+aPicture.getHeight());

	    Bitmap segmentation = ImageFileUtil.getBitmap(segmentFileName);
	    System.out.println("*focaAndLousa* - ImageTreatment segmentFileSize: "+segmentation.getWidth()+"x"+segmentation.getHeight());

	    ImageView image = (ImageView)findViewById(R.id.imageViewImageTreatment);

	    // Set picture image on imageview to prevent black screen
	    image.setImageBitmap(aPicture);

	    int bitmapWidth = aPicture.getWidth();
	    int bitmapHeight = aPicture.getHeight();
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
		    Integer p = aPicture.getPixel(i, j);
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
	    System.out.println("*focaAndLousa* - bitmapWidth "+bitmapWidth+"  -  bitmapHeight "+bitmapHeight);
	    System.out.println("*focaAndLousa* - imgMarcador "+imgMarcador.length+"  -  imgMarcador[0] "+imgMarcador[0].length);
	    for (int x = 0; x < bitmapWidth; x++) {
		for (int y = 0; y < bitmapHeight; y++) {
		    if (imgMarcador[x][y] != -1) {
			if (labels.containsKey(imgMarcador[x][y])) {
			    imgM.setPixel(x, y, labels.get(imgMarcador[x][y]));
			} else {
			    labels.put(imgMarcador[x][y], label++);
			    imgM.setPixel(x, y, labels.get(imgMarcador[x][y]));
			}
		    } else {
			imgM.setPixel(x, y, -1);
		    }
		}
	    }

	    IGrayScaleImage imgWS = OperatorsByIFT.watershedByMarker(adj, imgGrad, imgM);
	    finalPicture = aPicture.copy(Bitmap.Config.ARGB_8888, true); 
	    int[][] pixelsImageLabel = imgWS.getPixels();

	    for (int i = 0; i < bitmapWidth; i++) {
		for (int j = 0; j < bitmapHeight; j++) {
			if(pixelsImageLabel[i][j] == 1){
				int rgb = 255;
			    rgb = (rgb << 8) + 255;
			    rgb = (rgb << 8) + 255;
			    rgb = (rgb << 8) + 255;
			    finalPicture.setPixel(i, j, rgb);
			}

		}
	    }

	    image.setImageBitmap(finalPicture);

	} catch (Exception exc) {
	    exc.printStackTrace();
	}

    }

    public void onDoneTreatment(View view) {
	String finalFileName = ImageFileUtil.getOutputMediaFileUri(ImageFileUtil.MEDIA_TYPE_FINAL).getPath();
	if (finalPicture != null  &&  finalFileName != null  &&  !finalFileName.isEmpty()) {
	    boolean saveOk = ImageFileUtil.saveBitmap(finalPicture, finalFileName);
	    if (saveOk) {
		Toast.makeText(this, "Saved: " + finalFileName, Toast.LENGTH_SHORT).show();
	    	finish();
	    } else {
		Toast.makeText(this, "Erro ao salvar: " + finalFileName, Toast.LENGTH_SHORT).show();
	    }
	} else {
	    Toast.makeText(this, "Nao eh possivel salvar!", Toast.LENGTH_SHORT).show();
	}
    }

    public void onCancelTreatment(View view) {
	Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
	finish();
    }

}
