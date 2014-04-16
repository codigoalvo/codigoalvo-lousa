package br.com.focaand.lousa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import br.com.focaand.lousa.util.ImageFileUtil;
import br.com.focaand.lousa.util.ImageFiltersUtil;
import br.com.focaand.lousa.util.Preferences;

public class ImageTreatmentActivity
    extends Activity {

    private static final String TAG             = "focaand.lousa.ImageTreatmentActivity";
    private String              segmentFileName = "";
    private Bitmap              finalPicture    = null;
    private int[][]             pixels          = null;

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

	SeekBar skbThreshold = (SeekBar)findViewById(R.id.skbThreshold);
	skbThreshold.setProgress(Preferences.getInstance().getContraste());
	((TextView)findViewById(R.id.lblThreshold)).setText(Integer.toString(skbThreshold.getProgress()));

	skbThreshold.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
	    @Override
	    public void onStopTrackingTouch(SeekBar seekBar) {
	    }
	    @Override
	    public void onStartTrackingTouch(SeekBar seekBar) {
	    }

	    @Override
	    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (fromUser) {
		    TextView lblThreshold = (TextView)findViewById(R.id.lblThreshold);
		    lblThreshold.setText(Integer.toString(progress));
		    pixels2ImegaView(progress);
		}
	    }
	});

	try {
	    Bundle extras = getIntent().getExtras();
	    segmentFileName = extras.getString("segment_path");
	    mLockScreenRotation();
	    final ProgressDialog dialog = new ProgressDialog(ImageTreatmentActivity.this);
	    dialog.setTitle(R.string.processing_image);
	    dialog.setMessage(getResources().getString(R.string.please_wait));
	    dialog.show();

	    new Thread(new Runnable() {

		@Override
		public void run() {
		    final Bitmap segmentation = ImageFileUtil.getBitmap(segmentFileName);
		    System.out.println("*focaAndLousa* - Loaded imageTreatment segmentFileName: " + segmentFileName);
		    finalPicture = Bitmap.createBitmap(segmentation.getWidth(), segmentation.getHeight(), Bitmap.Config.ARGB_8888);
		    processaFiltrosIniciaisImagem(segmentation);
		    runOnUiThread(new Runnable() {

			@Override
			public void run() {
			    dialog.dismiss();
			    ImageTreatmentActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			    pixels2ImegaView(Preferences.getInstance().getContraste());
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

    private void processaFiltrosIniciaisImagem(Bitmap segmentation) {
	pixels = ImageFiltersUtil.bitmapToGrayscale(segmentation);

	segmentation.recycle();
	segmentation = null;
	System.gc();

	pixels = ImageFiltersUtil.exponentialHistogram(pixels);
    }

    private void pixels2ImegaView(double thersholder) {
	int[][] imagePixels = ImageFiltersUtil.convertToBlackWhite(pixels, thersholder);
	ImageFiltersUtil.grayscale2Bitmap(imagePixels, finalPicture);
	ImageView imageView = (ImageView)findViewById(R.id.imageViewImageTreatment);
	imageView.setImageBitmap(finalPicture);
    }
    
    /**
     * @autor: http://eigo.co.uk/labs/lock-screen-orientation-in-android/
     * // See more at: http://eigo.co.uk/labs/lock-screen-orientation-in-android/#sthash.pbM5Pkf3.dpuf
     */
    private void mLockScreenRotation() {   // Stop the screen orientation changing during an event
	switch (this.getResources().getConfiguration().orientation)     {
	    case Configuration.ORIENTATION_PORTRAIT:
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		break;
	    case Configuration.ORIENTATION_LANDSCAPE:
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		break;
	}
    } 

}
