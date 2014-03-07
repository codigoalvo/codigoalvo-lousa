package tcc.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import tcc.GrayScaleImage;
import tcc.IGrayScaleImage;
import tcc.IImage;



/**
 * Project: Computer Vision Framework
 * 
 * @author Wonder Alexandre Luz Alves
 * @advisor Ronaldo Fumio Hashimoto
 * 
 * @date 13/09/2007
 * 
 * @description
 * Esta classe define alguns metodos utilitarios para operacoes com imagens
 */
public class ImageUtils {

    /**
     * valida os indices da imagem dada
     * @param img
     * @param x
     * @param y
     * @return
     */
    public static boolean isIndexInImage(IImage img, int x, int y){
        return (x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight());
    }

    public static boolean isIndexInImage(IImage img, int i){
    	return isIndexInImage(img, i % img.getWidth(), i / img.getWidth());
    }
    
    
    public static double log2(double d) {
        return Math.log(d)/Math.log(2.0);
     }

    public static double log10(double d) {
        return Math.log(d)/Math.log(10.0);
     }
    
    public static String numberToString(double x){
        String s = new BigDecimal(x).toPlainString();
        return s;
    }

    /**
     * Calcula a entropia da imagem
     * @param img
     * @return
     */
    public static double entropy(IGrayScaleImage img){
        int result = 0;
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                result += - log2(img.getPixel(i, j)) * img.getPixel(i, j);
            }
        }
        
        return result;
    }
    
    /**
     * Reescala a imagem de entrada para valores no intervalode 0 a 255
     * @param img - imagem de entrada
     * @return IGrayScaleImage
     */
    public static IGrayScaleImage normalizedPixels(IGrayScaleImage img){
        IGrayScaleImage imgOut = new GrayScaleImage(img.getWidth(), img.getHeight());
        int tmp = 0;
        for(int x = 0 ; x < img.getWidth() ; x++){
            for(int y = 0 ; y < img.getHeight(); y++){
                tmp = (int) (( 255.0 / (img.getPixelMax() - img.getPixelMin())) * (img.getPixel(x, y) - img.getPixelMin())); 
                imgOut.setPixel(x, y, tmp);
            }
        }
        return imgOut;
    }

    
    
    /**
     * Normaliza os pixel diferentes de zero para proximos de 255
     * @param img
     * @return
     */
    public static IGrayScaleImage normalizedPixelsMax(IGrayScaleImage img){
        IGrayScaleImage imgOut = new GrayScaleImage(img.getWidth(), img.getHeight());
        int max = img.getPixelMax();
        for (int w = 0; w < img.getWidth(); w++)
            for (int h = 0; h < img.getHeight(); h++)
                if(img.getPixel(w,h) > 0)
                    imgOut.setPixel(w, h, 255 / max * img.getPixel(w,h));
        
        return imgOut;
    }
    

    
    
    /**
     * Determina a distancia entre dois pixels
     * @param w1 - coordenada da largura do primeiro pixel
     * @param h1 - coordenada da altura do primeiro pixel
     * @param w2 - coordenada da largura do segundo pixel
     * @param h2 - coordenada da altura do segundo pixel
     * @param type - tipo da distancia. 
     *               Sendo que type = 1 para euclidiana; 
     *                         type = 2 para city block; 
     *                         type = 3 para clessbord
     * @return double
     */
    public static double distance(double w1, double h1, double w2, double h2, int type){
        if(type == 1){
            return Math.sqrt(Math.pow(w1 - w2, 2) + Math.pow(h1 - h2, 2));
        }else if(type == 2){
            return Math.abs(w1 - w2) + Math.abs(h1 - h2);
        }else{
            return Math.max(Math.abs(w1 - w2), Math.abs(h1 - h2));
        }
        
    }
    
  
    
    
    public static int randomInteger (int low, int high){
    	int k;
    	k = (int) (Math.random() * (high - low + 1));
    	return low + k;
    }
    
    
    

    /**
     * Conjunto de pixels vizinhos de 8
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public static List<Integer> getNeighbors(int i, int width, int height) {
    	int x = i % width;
    	int y =i / width;
        ArrayList<Integer> neighbors = new ArrayList<Integer>();
        
        if (x < width-1) {
            neighbors.add( (y) * width + (x+1) );
        }
        if (x > 0) {
            neighbors.add( (y) * width + (x-1) );
        }
        if (y > 0) {
            neighbors.add( (y-1) * width + (x) );
        }
        if (y < height-1) {
            neighbors.add( (y+1) * width + (x) );
        }

        if (x > 0 && y > 0) {
            neighbors.add( (y-1) * width + (x-1) );
        }

        if (x < width - 1 && y > 0) {
            neighbors.add( (y-1) * width + (x+1) );
        }

        if (x > 0 && y < height - 1) {
            neighbors.add( (y+1) * width + (x-1) );
        }

        if (x < width - 1 && y < height - 1) {
            neighbors.add( (y+1) * width + (x+1) );
        }
        return neighbors;
    }
    
   
}
