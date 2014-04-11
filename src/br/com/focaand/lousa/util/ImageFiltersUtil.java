package br.com.focaand.lousa.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class ImageFiltersUtil {

    /**
     * Converte um objeto android.graphics.Bitmap ARGB_8888 para um array de int[][] grayscale (0-255)
     * @param Bitmap bitmapOriginal
     * @return int[width][height] (Array de duas dimenssões com valores de 0 a 255 * escala de cinza)
     * @author Cassio
     * @since 04/2014
     */
    public static int[][] bitmapToGrayscale(Bitmap bitmap) {
	int A, R, G, B;
	int pixelColor;
	int height = bitmap.getHeight();
	int width = bitmap.getWidth();
	int[][] grayscale = new int[width][height];
	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		pixelColor = bitmap.getPixel(x, y);
		//A = Color.alpha(pixelColor);
		R = Color.red(pixelColor);
		G = Color.green(pixelColor);
		B = Color.blue(pixelColor);
		int media = (R + B + G) / 3;
		grayscale[x][y] = media;
	    }
	}
	return grayscale;
    }

    /**
     * Converte um array de int[][] grayscale (0-255) para um objeto android.graphics.Bitmap ARGB_8888
     * @param int[width][height] (Array de duas dimenssões com valores de 0 a 255 * escala de cinza)
     * @return android.graphics.Bitmap bitmap
     * @author Cassio
     * @since 04/2014
     */
    public static Bitmap grayscale2Bitmap(int[][] grayscale) {
	int width = grayscale.length;
	int height = grayscale[0].length;
	final int ALPHA = 255;
	Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	for (int x = 0; x < width; x++) {
	    for (int y = 0; y < height; y++) {
		output.setPixel(x, y, Color.argb(ALPHA, grayscale[x][y], grayscale[x][y], grayscale[x][y]));
	    }
	}
	return output;
    }

    /**
     * Transforma um array de pixeis grayscale em um array de pixeis preto e branco de acordo com um parametro de corte
     * @param int[width][height] (Array de duas dimenssões com valores de 0 a 255 * escala de cinza)
     * @param double parametro de corte
     * @return int[width][height] (Array de duas dimenssões com valores 0 ou 255 preto e branco)
     * @author Eduardo
     * @since 04/2014
     */
    public static int[][] convertToBlackWhite(int grayscale[][], double value) {
	// pegando as dimensoes da imagem
	int width = grayscale.length;
	int height = grayscale[0].length;
	int[][] blackWhite = new int[width][height];

	for (int x = 0; x < width; x++) {
	    for (int y = 0; y < height; y++) {
		if (grayscale[x][y] <= value)
		    blackWhite[x][y] = 255;
		else if (Preferences.getInstance().getResultadoPretoBranco())
		    blackWhite[x][y] = 0;
		else
		    blackWhite[x][y] = 255-grayscale[x][y];
	    }
	}
	return blackWhite;
    }

    /**
     * Processamento de histogram exponencial em array de int grayscale (0-255)
     * @param int[width][height] grayscale (Array de duas dimenssões com valores de 0 a 255 * escala de cinza)
     * @return int[width][height] grayscale processado com histogram exponencial
     * @author Eduardo
     * @since 04/2014
     */
    public static int[][] exponentialHistogram(int grayscale[][]) {
	int width = grayscale.length;
	int height = grayscale[0].length;
	int result[][] = new int[width][height];

	int histogram[] = new int[256];
	double lambda = 255 / Math.log(256);
	for (int i = 0; i < 256; i++) {
	    histogram[i] = (int)Math.exp(i / lambda);
	}

	for (int x = 0; x < width; x++) {
	    for (int y = 0; y < height; y++) {
		result[x][y] = histogram[grayscale[x][y]];
	    }
	}

	return result;
    }
    
    /**
     * TODO: Eduardo por favor colocar uma descrição aqui!
     * @param int[width][height] (Array de duas dimenssões com valores de 0 a 255 * escala de cinza)
     * @return double parametro de corte de thresholder
     * @author Eduardo
     * @since 04/2014
     */
    public static double thresholder(int grayscale[][]) {
	// pegando as dimensoes da imagem
	int largura = grayscale.length;
	int altura = grayscale[0].length;

	// vetor do histograma
	int h[] = new int[256];
	// calculando o histograma
	for (int x = 0; x < largura; x++) {
	    for (int y = 0; y < altura; y++) {
		h[grayscale[x][y]] += 1;
	    }
	}
	double wb = 0.0, ub = 0.0, ob = 0.0;
	double wf = 0.0, uf = 0.0, of = 0.0;
	double half = 0.0;
	// background
	for (int i = 0; i < 128; i++) {
	    half += h[i];
	    ub += i * h[i];
	}
	wb = half / (altura * largura);
	ub = ub / half;

	for (int i = 0; i < 128; i++) {
	    ob += Math.pow(i - ub, 2) * h[i];
	}
	ob = ob / half;

	// Foreground
	for (int i = 128; i <= 255; i++) {
	    half += h[i];
	    uf += i * h[i];
	}
	wf = half / (altura * largura);
	uf = uf / half;
	for (int i = 0; i <= 128; i++) {
	    of += Math.pow(i - ub, 2) * h[i];
	}
	of = ob / half;

	// Within Class Variance
	double ow = 0.0, otsu = 0.0;
	ow = wb * ob + wf * of;
	otsu = wb * (1 - wb) * Math.pow(ub - uf, 2);

	double threshold = ow;
	System.out.print(ob);

	return threshold;
    }

    public static Bitmap adjustedContrast(Bitmap bitmapOriginal, double value) {
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
	for (int x = 0; x < width; ++x) {
	    for (int y = 0; y < height; ++y) {
		// get pixel color
		pixel = bitmapOriginal.getPixel(x, y);
		A = Color.alpha(pixel);
		// apply filter contrast for every channel R, G, B
		R = Color.red(pixel);
		R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
		if (R < 0) {
		    R = 0;
		} else if (R > 255) {
		    R = 255;
		}

		G = Color.green(pixel);
		G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
		if (G < 0) {
		    G = 0;
		} else if (G > 255) {
		    G = 255;
		}

		B = Color.blue(pixel);
		B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
		if (B < 0) {
		    B = 0;
		} else if (B > 255) {
		    B = 255;
		}

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
