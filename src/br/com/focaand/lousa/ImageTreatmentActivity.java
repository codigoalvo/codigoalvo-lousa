package br.com.focaand.lousa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.focaand.lousa.util.ImageFileUtil;
import br.com.focaand.lousa.util.ImageFiltersUtil;
import br.com.focaand.lousa.util.Preferences;

public class ImageTreatmentActivity
    extends Activity {

    private static final String TAG             = "focaand.lousa.ImageTreatmentActivity";
    private String              segmentFileName = "";
    Bitmap                      finalPicture;

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
	int[][] pixels = ImageFiltersUtil.bitmapToGrayscale(segmentation);

	segmentation.recycle();
	segmentation = null;
	System.gc();

	pixels = ImageFiltersUtil.exponentialHistogram(pixels);

	// double thersholder = ImageFiltersUtil.thresholder(pixels);
	double thersholder = Preferences.getInstance().getContraste();

	pixels = ImageFiltersUtil.convertToBlackWhite(pixels, thersholder);
	Bitmap output = ImageFiltersUtil.grayscale2Bitmap(pixels);

	return output;
    }

}
