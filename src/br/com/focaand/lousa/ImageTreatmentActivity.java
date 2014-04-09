package br.com.focaand.lousa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.focaand.lousa.util.ImageFileUtil;
import br.com.focaand.lousa.util.Preferences;

public class ImageTreatmentActivity
    extends Activity {

    private static final String TAG = "focaand.lousa.ImageTreatmentActivity";
    private String segmentFileName = "";
    int imgMarcador[][];

    Bitmap finalPicture;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.treatment, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_trt_cancelar:
        	finish();
                return true;
            case R.id.action_trt_confirmar:
        	finalizarTratamento();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	Log.d(TAG, "before onCreate");
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_image_treatment);

	if (!Preferences.getInstance().getShowButtons())
	    hideButtons();

	try {
	    Bundle extras = getIntent().getExtras();
	    segmentFileName = extras.getString("segment_path");
	    final Bitmap segmentation = ImageFileUtil.getBitmap(segmentFileName);
	    System.out.println("*focaAndLousa* - Loaded imageTreatment segmentFileName: " + segmentFileName);

	    final ProgressDialog dialog = new ProgressDialog(ImageTreatmentActivity.this);
	    dialog.setTitle(R.string.processing_image);
	    dialog.setMessage(getResources().getString(R.string.please_wait));
	    dialog.show();

	    new Thread(new Runnable() {

		@Override
		public void run() {
		    finalPicture = processaFiltrosImagem(segmentation);
		    runOnUiThread(new Runnable() {
			@Override
			public void run() {
			    dialog.dismiss();
			    ImageView imageView = (ImageView)findViewById(R.id.imageViewImageTreatment);
			    imageView.setImageBitmap(finalPicture);
			}
		    });
		}
	    }).start();

	} catch (Exception exc) {
	    exc.printStackTrace();
	}
	Log.d(TAG, "after onCreate");
    }

    @Override
    protected void onDestroy() {
	Log.d(TAG, "before onDestroy");
	if (finalPicture != null) {
	    finalPicture.recycle();
	    finalPicture = null;
	    Log.d(TAG, "onDestroy");
	}
	System.gc();
        super.onDestroy();
        Log.d(TAG, "after onDestroy");
    }

    public void onDoneTreatment(View view) {
	finalizarTratamento();
    }

    public void onCancelTreatment(View view) {
	Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
	finish();
    }

    private void finalizarTratamento() {
	String finalFileName = ImageFileUtil.getOutputMediaFileUri(ImageFileUtil.MEDIA_TYPE_FINAL).getPath();
	if (finalPicture != null && finalFileName != null && !finalFileName.isEmpty()) {
	    boolean saveOk = ImageFileUtil.saveBitmap(finalPicture, finalFileName);
	    if (saveOk) {
		Toast.makeText(this, "Salvo: " + finalFileName, Toast.LENGTH_SHORT).show();
		finish();
	    } else {
		Toast.makeText(this, "Erro ao salvar: " + finalFileName, Toast.LENGTH_SHORT).show();
	    }
	} else {
	    Toast.makeText(this, "Nao eh possivel salvar!", Toast.LENGTH_SHORT).show();
	}
    }

    private void hideButtons() {
	ImageButton imgBtnDoneTreatment = (ImageButton)findViewById(R.id.imgBtnDoneTreatment);
	imgBtnDoneTreatment.setVisibility(View.GONE);
	ImageButton imgBtnCancelTreatment = (ImageButton)findViewById(R.id.imgBtnCancelTreatment);
	imgBtnCancelTreatment.setVisibility(View.GONE);
    }

    private Bitmap processaFiltrosImagem(Bitmap segmentation) {

	Bitmap contrastBitmap = adjustedContrast(segmentation, Preferences.getInstance().getContraste());

	Bitmap colorInverted = colorInvert(contrastBitmap);

	Bitmap bmpGrayscale = toGrayscale(colorInverted);

	segmentation.recycle();
	segmentation = null;

	contrastBitmap.recycle();
	contrastBitmap = null;

	colorInverted.recycle();
	colorInverted = null;
	System.gc();

	return bmpGrayscale;
    }

    /**
     * A simple toGrayscale test
     * @param bmpOriginal
     * @return
     */
    public Bitmap toGrayscale(Bitmap bmpOriginal) {        
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();    

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        canvas.drawBitmap(bmpOriginal, 0, 0, paint);
        bmpOriginal.recycle();
        bmpOriginal = null;
        System.gc();
        return bmpGrayscale;
    }

    public static Bitmap colorInvert(Bitmap bitmapOriginal) {
	Bitmap output = Bitmap.createBitmap(bitmapOriginal.getWidth(), bitmapOriginal.getHeight(), bitmapOriginal.getConfig());
	int A, R, G, B;
	int pixelColor;
	int height = bitmapOriginal.getHeight();
	int width = bitmapOriginal.getWidth();

	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		pixelColor = bitmapOriginal.getPixel(x, y);
		A = Color.alpha(pixelColor);

		R = 255 - Color.red(pixelColor);
		G = 255 - Color.green(pixelColor);
		B = 255 - Color.blue(pixelColor);

		output.setPixel(x, y, Color.argb(A, R, G, B));
	    }
	}
	bitmapOriginal.recycle();
	bitmapOriginal = null;
	System.gc();
	return output;
    }

    private Bitmap adjustedContrast(Bitmap bitmapOriginal, double value)
    {
        // image size
        int width = bitmapOriginal.getWidth();
        int height = bitmapOriginal.getHeight();
        // create output bitmap

        // create a mutable empty bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, bitmapOriginal.getConfig());

        // create a canvas so that we can draw the bmOut Bitmap from source bitmap
        Canvas canvas = new Canvas();
        canvas.setBitmap(bmOut);

        // draw bitmap to bmOut from src bitmap so we can modify it
        canvas.drawBitmap(bitmapOriginal, 0, 0, new Paint(Color.BLACK));


        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = bitmapOriginal.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                G = Color.green(pixel);
                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                B = Color.blue(pixel);
                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        bitmapOriginal.recycle();
        bitmapOriginal = null;
        System.gc();
        return bmOut;
    }

}
