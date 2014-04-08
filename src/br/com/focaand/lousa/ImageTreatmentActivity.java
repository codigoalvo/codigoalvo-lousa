package br.com.focaand.lousa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
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
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_image_treatment);

	if (!Preferences.getInstance().getShowButtons())
	    hideButtons();

	try {
	    Bundle extras = getIntent().getExtras();
	    segmentFileName = extras.getString("segment_path");
	    final Bitmap segmentation = ImageFileUtil.getBitmap(segmentFileName);
	    System.out.println("*focaAndLousa* - Loaded imageTreatment segmentFileName: " + segmentFileName);
	    final ImageView image = (ImageView)findViewById(R.id.imageViewImageTreatment);
	    image.setImageBitmap(segmentation);

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
			    image.setImageBitmap(finalPicture);
			}
		    });
		}
	    }).start();

	} catch (Exception exc) {
	    exc.printStackTrace();
	}

    }
    
    public void onDoneTreatment(View view) {
	finalizarTratamento();
    }

    public void onCancelTreatment(View view) {
	Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
	finish();
    }

    private void hideButtons() {
	ImageButton imgBtnDoneTreatment = (ImageButton)findViewById(R.id.imgBtnDoneTreatment);
	imgBtnDoneTreatment.setVisibility(View.GONE);
	ImageButton imgBtnCancelTreatment = (ImageButton)findViewById(R.id.imgBtnCancelTreatment);
	imgBtnCancelTreatment.setVisibility(View.GONE);
    }

    private Bitmap processaFiltrosImagem(Bitmap segmentation) {
	Bitmap bmpGrayscale = toGrayscale(segmentation);

	// TODO: Processamento de filtros da imagem deve ser feito aqui

	Bitmap processBitmap = bmpGrayscale.copy(Bitmap.Config.ARGB_8888, true);
	return processBitmap;
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
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
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

}
