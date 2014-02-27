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

public class SegmentationActivity
    extends Activity implements OnTouchListener {

    private int displayWidth;
    private int displayHeight;
    private String fileName = "";
    private Bitmap bitmapDraw;
    ImageView imageViewDraw;
    Canvas canvas;
    Paint paint;
    float downx = 0, downy = 0, upx = 0, upy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_segmentation);

	Bundle extras = getIntent().getExtras();
	fileName = extras.getString("photo_path");
	Bitmap bitmapPicture = ImageFileUtil.getBitmap(fileName, this.getResources().getConfiguration().orientation);
	ImageView imageViewPicture = (ImageView)findViewById(R.id.imageViewPicture);
	imageViewPicture.setImageBitmap(bitmapPicture);

	imageViewDraw = (ImageView)findViewById(R.id.imageViewDraw);
	Display currentDisplay = getWindowManager().getDefaultDisplay();
	displayWidth = currentDisplay.getWidth();
	displayHeight = currentDisplay.getHeight();

	bitmapDraw = Bitmap.createBitmap(bitmapPicture.getWidth(), bitmapPicture.getHeight(), Bitmap.Config.ARGB_8888);
	canvas = new Canvas(bitmapDraw);
	imageViewDraw.setImageBitmap(bitmapDraw);

	paint = new Paint();
	paint.setColor(Color.BLUE);
	paint.setStrokeWidth(5);
	paint.setStrokeJoin(Paint.Join.ROUND);
	paint.setStrokeCap(Paint.Cap.ROUND);
	paint.setDither(true);
	paint.setAntiAlias(true);
	imageViewDraw.setOnTouchListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.segmentation, menu);
	return true;
    }

    public void onDoneSegmentation(View view) {
	Toast.makeText(this, "onDoneSegmentation", Toast.LENGTH_SHORT).show();
	//TODO: Neste ponto, salvar o arquivo novo já segmentado para enviar para a próxima activity
	Intent i = new Intent(SegmentationActivity.this, ImageTreatmentActivity.class);
        i.putExtra("photo_path" ,fileName);
        startActivity(i);
        finish();
    }

    public void onCancelSegmentation(View view) {
	Toast.makeText(this, "onCancelSegmentation", Toast.LENGTH_SHORT).show();
	finish();
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
		canvas.drawLine(downx, downy, upx, upy, paint);
//		draw(downx, downy, upx, upy);
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
	Point pointDown = ImageFileUtil.getProportionalXY(displayWidth, displayHeight, bitmapDraw.getWidth(), bitmapDraw.getHeight(), (int)downX, (int)downY);
	Point pointUp = ImageFileUtil.getProportionalXY(displayWidth, displayHeight, bitmapDraw.getWidth(), bitmapDraw.getHeight(), (int)downX, (int)downY);
	canvas.drawLine(pointDown.x, pointDown.y, pointUp.x, pointUp.y, paint);
    }

}
