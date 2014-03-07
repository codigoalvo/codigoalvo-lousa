package tcc;





public interface IGrayScaleImage extends IImage{
    
    public void initImage(int color);
    
    public IGrayScaleImage duplicate();
    
    public int getPixel(int x, int y);
    
    public void setPixel(int x, int y, int value);
    
    public int[][] getPixels();
    
    public void setPixels(int matrix[][]);
    
    public IBinaryImage convertBinaryImage(int limiar, int background);
    
    public int getPixelMax();
    
    public int getPixelMin();
    
    public int getPixelMean();
    
    public int numPixels();
    
    public IGrayScaleImage invert();

    public void add(int a);
    
    public void multiply(int a);
    
    public int[] getHistogram();
    
    public void resize(int x, int y);
    
    public void resizeCenter(int x, int y);
    
    public void paintBoundBox(int x1, int y1, int x2, int y2, int color);
    
    public void setPixel(int i, int level); 
    
    public int getPixel(int i);
    
    public int getSize();
    
    public int countPixels(int color);
    
    public void replaceColor(int colorOld, int colorNew);
    
    public IRGBImage randomColor();
}
    