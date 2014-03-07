package tcc;

import tcc.utils.Color;



public interface IRGBImage extends IImage{
    
    public void initImage(int color);
    
    public IRGBImage duplicate();
    
    public Color getPixel(int x, int y);
    
    public int getPixel(int x, int y, int band); 
    
    public Color getPixel(int i);
    
    public int getSize();
    
    public void setPixel(int x, int y, int value[]);
    
    public void setPixel(int x, int y, Color c);
    
    public void setPixel(int x, int y, int band, int value);
    
    public void setPixel(int i, Color c);

    public void setPixel(int i, int level);
    
    public int[][][] getPixels();
    
    public int[][] getPixels(int band);
    
    public void setPixels(int matrix[][][]);
    
    public void setPixels(int matrix[][]);
    
    public void setPixels(int band, int matrix[][]);
    
    public IGrayScaleImage convertGrayScaleImage();

    public void paintBoundBox(int x1, int y1, int x2, int y2, Color c);
    
    public void addSubImage(IRGBImage img, int x, int y);
    
    public void setPixel(int x, int y, int level);
    
    public void addSubImage(IGrayScaleImage img, int x, int y);
    
}
    