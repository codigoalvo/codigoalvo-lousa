package br.com.focaand.lousa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.focaand.lousa.util.ImageFileUtil;

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
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_image_treatment);

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

    private Bitmap processaFiltrosImagem(Bitmap segmentation) {
	Bitmap processBitmap = segmentation.copy(Bitmap.Config.ARGB_8888, true);

	// TODO: Processamento de filtros da imagem deve ser feito aqui
	return processBitmap;
    }

    public void onDoneTreatment(View view) {
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

    public void onCancelTreatment(View view) {
	Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
	finish();
    }

}
