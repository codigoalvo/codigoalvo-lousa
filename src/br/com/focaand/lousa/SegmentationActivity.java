package br.com.focaand.lousa;

import java.util.HashMap;

import tcc.GrayScaleImage;
import tcc.IGrayScaleImage;
import tcc.operators.MorphlogicalOperators;
import tcc.operators.OperatorsByIFT;
import tcc.utils.AdjacencyRelation;
import br.com.focaand.lousa.util.ImageFileUtil;
import br.com.focaand.lousa.util.Preferences;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class SegmentationActivity extends Activity  implements OnTouchListener {

    private static final String TAG = "focaand.lousa.SegmentationActivity";
    private Bitmap bitmapDraw;
    private Bitmap bitmapPicture;
    ImageView imageViewDraw;
    Canvas canvas;
    Paint paint;
    float downx = 0, downy = 0, upx = 0, upy = 0;

    enum SegmentType {
	BACKGROUND, FOREGROUND
    };

    private SegmentType currentSegment = SegmentType.FOREGROUND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	Log.d(TAG, "before onCreate");
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_segmentation);
	
	if (!Preferences.getInstance().getShowButtons())
	    hideButtons();

	Bundle extras = getIntent().getExtras();
	String fileName = extras.getString("photo_path");
	bitmapPicture = ImageFileUtil.getBitmap(fileName);
	ImageView imageViewPicture = (ImageView)findViewById(R.id.imageViewPicture);
	imageViewPicture.setImageBitmap(bitmapPicture);

	imageViewDraw = (ImageView)findViewById(R.id.imageViewDraw);

	bitmapDraw = Bitmap.createBitmap(bitmapPicture.getWidth(), bitmapPicture.getHeight(), Bitmap.Config.ARGB_8888);
	canvas = new Canvas(bitmapDraw);
	imageViewDraw.setImageBitmap(bitmapDraw);
	imageViewDraw.setOnTouchListener(this);

	paint = new Paint();
	paint.setColor(Color.BLUE);
	Double fatorBrush = Preferences.getInstance().getMaxResolution()/200d;
	int brush = fatorBrush.intValue();
	if (brush < 2)
	    brush = 2;
	paint.setStrokeWidth(brush);
	paint.setStrokeJoin(Paint.Join.ROUND);
	paint.setStrokeCap(Paint.Cap.ROUND);
	paint.setDither(true);
	paint.setAntiAlias(true);
	Log.d(TAG, "after onCreate");
    }

    @Override
    protected void onDestroy() {
	Log.d(TAG, "before onDestroy");
	if (bitmapDraw != null) {
	    bitmapDraw.recycle();
	    bitmapDraw = null;
	    Log.d(TAG, "onDestroy 1");
	}
	if (bitmapPicture != null) {
	    bitmapPicture.recycle();
	    bitmapPicture = null;
	    Log.d(TAG, "onDestroy 2");
	}
	System.gc();
        super.onDestroy();
	Log.d(TAG, "after onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.segmentation, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_seg_cancelar:
        	finish();
                return true;
            case R.id.action_seg_mudar:
        	trocarSegmentacao();
        	return true;
            case R.id.action_seg_confirmar:
        	finalizarSegmentacao();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onDoneSegmentation(View view) {
	finalizarSegmentacao();
    }

    public void onSwitchSegment(View view) {
	trocarSegmentacao();
    }

    public void onCancelSegmentation(View view) {
	finish();
    }

    private void finalizarSegmentacao() {
	final String segmentFileName = ImageFileUtil.getOutputMediaFileUri(ImageFileUtil.MEDIA_TYPE_SEGMENTATION).getPath();
	if (bitmapDraw != null  &&  segmentFileName != null  &&  !segmentFileName.isEmpty()) {
	    Log.d("focaAndLousa", "Segmentation - before segmentation");
	    mLockScreenRotation();
	    final ProgressDialog dialog = new ProgressDialog(SegmentationActivity.this);
	    dialog.setTitle(R.string.processing_segmentation);
	    dialog.setMessage(getResources().getString(R.string.please_wait));
	    dialog.show();

	    new Thread(new Runnable() {
		@Override
		public void run()
		{
		    final Bitmap segmented = processSegmentation(bitmapPicture, bitmapDraw);
		    runOnUiThread(new Runnable() {
			@Override
			public void run()
			{
			    dialog.dismiss();
			    SegmentationActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			    Log.d("focaAndLousa", "Segmentation - endSegmentation 2");
			    saveBitmap(segmented, segmentFileName);
			}
		    });
		}
	    }).start();

	} else {
	    Toast.makeText(this, R.string.erro_salvar_seg, Toast.LENGTH_SHORT).show();
	}
    }

    private void trocarSegmentacao() {
	if (currentSegment == SegmentType.BACKGROUND) {
	    currentSegment = SegmentType.FOREGROUND;
	    if (this.paint != null)
		this.paint.setColor(Color.BLUE);
	} else {
	    currentSegment = SegmentType.BACKGROUND;
	    if (this.paint != null)
		this.paint.setColor(Color.RED);
	}
    }

    private void hideButtons() {
	ImageButton imgBtnDoneSegmentation = (ImageButton)findViewById(R.id.imgBtnDoneSegmentation);
	imgBtnDoneSegmentation.setVisibility(View.GONE);
	ImageButton imgBtnCancelSegmentation = (ImageButton)findViewById(R.id.imgBtnCancelSegmentation);
	imgBtnCancelSegmentation.setVisibility(View.GONE);
	ImageButton imgBtnSwitchSegment = (ImageButton)findViewById(R.id.imgBtnSwitchSegment);
	imgBtnSwitchSegment.setVisibility(View.GONE);
    }

    public void saveBitmap(Bitmap segmented, String segmentFileName) {
	if (segmented != null) {
	    boolean saveDrawOk = ImageFileUtil.saveBitmap(segmented, segmentFileName);
	    if (saveDrawOk) {
		Toast.makeText(this, R.string.ok_salvar_seg, Toast.LENGTH_SHORT).show();
		Intent i = new Intent(SegmentationActivity.this, ImageTreatmentActivity.class);
		i.putExtra("segment_path", segmentFileName);
		segmented.recycle();
		segmented = null;
		System.gc();
		startActivity(i);
		finish();

	    } else {
		Toast.makeText(this, R.string.erro_salvar_seg, Toast.LENGTH_SHORT).show();
	    }
	} else {
	    Toast.makeText(this, R.string.erro_salvar_seg, Toast.LENGTH_SHORT).show();
	}
    }

    public boolean onTouch(View v, MotionEvent event) {
	int action = event.getAction();
	switch (action) {
	    case MotionEvent.ACTION_DOWN:
		downx = event.getX();
		downy = event.getY();
		break;
	    case MotionEvent.ACTION_MOVE:
		upx = event.getX();
		upy = event.getY();
		draw(downx, downy, upx, upy);
		downx = upx;
		downy = upy;
		imageViewDraw.invalidate();
		break;
	    case MotionEvent.ACTION_UP:
		upx = event.getX();
		upy = event.getY();
		draw(downx, downy, upx, upy);
		imageViewDraw.invalidate();
		break;
	    case MotionEvent.ACTION_CANCEL:
		break;
	    default:
		break;
	}
	return true;
    }

    private void draw(float downX, float downY, float upX, float upY) {
	Point pointDown = getProportionalXY(imageViewDraw.getMeasuredWidth(), imageViewDraw.getMeasuredHeight(), bitmapDraw.getWidth(), bitmapDraw.getHeight(), (int)downX, (int)downY);
	Point pointUp = getProportionalXY(imageViewDraw.getMeasuredWidth(), imageViewDraw.getMeasuredHeight(), bitmapDraw.getWidth(), bitmapDraw.getHeight(), (int)upX, (int)upY);
	canvas.drawLine(pointDown.x, pointDown.y, pointUp.x, pointUp.y, paint);
    }

    public static Point getProportionalXY(int screenWidth, int screenHeight, int bitmapWidth, int bitmapHeight, int inputX, int inputY) {
	int x = (int)(inputX * bitmapWidth) / screenWidth;
	int y = (int)(inputY * bitmapHeight) / screenHeight;
	return new Point(x, y);
    }

    private Bitmap processSegmentation(Bitmap picture, Bitmap segmentation) {
	AdjacencyRelation adj = AdjacencyRelation.getCircular(1.5f);
	int bitmapWidth = picture.getWidth();
	int bitmapHeight = picture.getHeight();
	int pixels[][] = new int[bitmapWidth][bitmapHeight];
	int xBackground = 0;
	int yBackground = 0;
	int colorBackground = 0;
	
	
	System.out.println("*focaAndLousa* - ImageTreatment segmentFileSize: "+segmentation.getWidth()+"x"+segmentation.getHeight());
	System.out.println("*focaAndLousa* - ImageTreatment pictureFileSize: "+picture.getWidth()+"x"+picture.getHeight());

	int imgMarcador[][] = new int[segmentation.getWidth()][segmentation.getHeight()];
	for (int x = 0; x < segmentation.getWidth(); x++)
	    for (int y = 0; y < segmentation.getHeight(); y++) {
		int pixelSegmentation = segmentation.getPixel(x, y);

		if(pixelSegmentation == Color.BLUE || pixelSegmentation == Color.RED){
		    imgMarcador[x][y] = pixelSegmentation;
		    if(pixelSegmentation == Color.RED){
			xBackground = x;
			yBackground = y;
		    }
		}  
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
	Bitmap segmentedPicture = picture.copy(Bitmap.Config.ARGB_8888, true); 
	int[][] pixelsImageLabel = imgWS.getPixels();

	colorBackground = pixelsImageLabel[xBackground][yBackground];
	for (int i = 0; i < bitmapWidth; i++) {
	    for (int j = 0; j < bitmapHeight; j++) {
		if(pixelsImageLabel[i][j] == colorBackground){
		    segmentedPicture.setPixel(i, j, Color.BLACK); //TODO: Aqui o ideal é colocar a cor da "MODA" da parte verde da imagem. 
		} 
	    }
	}
	
	Log.d("focaAndLousa", "Segmentation - endSegmentation 1");

	return segmentedPicture;
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
