package br.com.focaand.lousa;

import br.com.focaand.lousa.util.ImageFileUtil;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

public class SegmentationActivity extends Activity  implements OnTouchListener {

    private int displayWidth;
    private int displayHeight;
    private String fileName = "";
    private Bitmap bitmapDraw;
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
	Bitmap bitmapPicture = ImageFileUtil.getBitmap(fileName);
	ImageView imageViewPicture = (ImageView)findViewById(R.id.imageViewPicture);
	imageViewPicture.setImageBitmap(bitmapPicture);

	imageViewDraw = (ImageView)findViewById(R.id.imageViewDraw);
	Display currentDisplay = getWindowManager().getDefaultDisplay();
	displayWidth = currentDisplay.getWidth();
	displayHeight = currentDisplay.getHeight();

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
	String segmentFileName = ImageFileUtil.getOutputMediaFileUri(ImageFileUtil.MEDIA_TYPE_SEGMENTATION).getPath();
	if (bitmapDraw != null  &&  segmentFileName != null  &&  !segmentFileName.isEmpty()) {
	    boolean saveDrawOk = ImageFileUtil.saveBitmap(bitmapDraw, segmentFileName);
	    if (saveDrawOk) {
		Toast.makeText(this, "Arquivo temporario de segmentacao salvo com sucesso", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(SegmentationActivity.this, ImageTreatmentActivity.class);
		i.putExtra("photo_path", fileName);
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
	Toast.makeText(this, "onCancelSegmentation", Toast.LENGTH_SHORT).show();
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

}
