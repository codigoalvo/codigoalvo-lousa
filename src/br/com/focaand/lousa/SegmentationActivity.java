package br.com.focaand.lousa;

import java.util.HashMap;

import tcc.GrayScaleImage;
import tcc.IGrayScaleImage;
import tcc.operators.MorphlogicalOperators;
import tcc.operators.OperatorsByIFT;
import tcc.utils.AdjacencyRelation;
import br.com.focaand.lousa.util.ImageFileUtil;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

public class SegmentationActivity extends Activity  implements OnTouchListener {

    private String fileName = "";
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
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_segmentation);

	Bundle extras = getIntent().getExtras();
	fileName = extras.getString("photo_path");
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
	paint.setStrokeWidth(5);
	paint.setStrokeJoin(Paint.Join.ROUND);
	paint.setStrokeCap(Paint.Cap.ROUND);
	paint.setDither(true);
	paint.setAntiAlias(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.segmentation, menu);
	return true;
    }

    public void onDoneSegmentation(View view) {
	final String segmentFileName = ImageFileUtil.getOutputMediaFileUri(ImageFileUtil.MEDIA_TYPE_SEGMENTATION).getPath();
	if (bitmapDraw != null  &&  segmentFileName != null  &&  !segmentFileName.isEmpty()) {

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
			    saveBitmap(segmented, segmentFileName);
			}
		    });
		}
	    }).start();

	} else {
	    Toast.makeText(this, R.string.erro_salvar_seg, Toast.LENGTH_SHORT).show();
	}
    }

    public void saveBitmap(Bitmap segmented, String segmentFileName) {
	if (segmented != null) {
	    boolean saveDrawOk = ImageFileUtil.saveBitmap(segmented, segmentFileName);
	    if (saveDrawOk) {
		Toast.makeText(this, R.string.ok_salvar_seg, Toast.LENGTH_SHORT).show();
		Intent i = new Intent(SegmentationActivity.this, ImageTreatmentActivity.class);
		i.putExtra("segment_path", segmentFileName);
		startActivity(i);
		finish();

	    } else {
		Toast.makeText(this, R.string.erro_salvar_seg, Toast.LENGTH_SHORT).show();
	    }
	} else {
	    Toast.makeText(this, R.string.erro_salvar_seg, Toast.LENGTH_SHORT).show();
	}
    }

    public void onCancelSegmentation(View view) {
	finish();
    }

    public void onSwitchSegment(View view) {
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

    public boolean onTouch(View v, MotionEvent event) {
	int action = event.getAction();
	switch (action) {
	    case MotionEvent.ACTION_DOWN:
		downx = event.getX();
		downy = event.getY();
		break;
	    case MotionEvent.ACTION_MOVE:
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

	System.out.println("*focaAndLousa* - ImageTreatment segmentFileSize: "+segmentation.getWidth()+"x"+segmentation.getHeight());
	System.out.println("*focaAndLousa* - ImageTreatment pictureFileSize: "+picture.getWidth()+"x"+picture.getHeight());

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

	for (int i = 0; i < bitmapWidth; i++) {
	    for (int j = 0; j < bitmapHeight; j++) {
		if(pixelsImageLabel[i][j] == 1){
		    int rgb = 255;
		    rgb = (rgb << 8) + 255;
		    rgb = (rgb << 8) + 255;
		    rgb = (rgb << 8) + 255;
		    segmentedPicture.setPixel(i, j, rgb);
		}
	    }
	}

	return segmentedPicture;
    }

}
