package tcc;

import tcc.utils.Color;
import tcc.utils.Point;

import tcc.utils.ImageUtils;

 
public class GrayScaleImage implements IGrayScaleImage{
    private int width; //largura da imagem
    private int height; //altura da imagem
    private int pixels[][]; //matriz de pixel da imagem
    private Point origin; //origem da imagem
    private int max = Integer.MIN_VALUE;
    private int min = Integer.MAX_VALUE;
    private int mean=0;
    private boolean flagMaxMin = false;
    

    public GrayScaleImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width][height];
        this.origin = new Point(0,0);
    }

    public GrayScaleImage(int matrix[][]){
    	this.width = matrix.length;
    	this.height = matrix[0].length;
    	this.pixels = matrix;
    	this.origin = new Point(0,0);
    }
    
    public IGrayScaleImage invert(){
        IGrayScaleImage imgOut = new GrayScaleImage(this.getWidth(), this.getHeight());
        for(int w=0;w<this.getWidth();w++){
            for(int h=0;h<this.getHeight();h++){
                imgOut.setPixel(w, h, 255 - this.getPixel(w, h));
            }
        }
        return imgOut;
    }

    
    public void paintBoundBox(int x1, int y1, int x2, int y2, int color){
        int w = x2 - x1;
        int h = y2 - y1;
        for(int i=0; i < w; i++){
            for(int j=0; j < h; j++){
                if(i == 0 || j == 0 || i == w-1 || j == h-1)
                    setPixel(x1 + i, y1 + j, color);        
            }
        }
    }
    
    public int countPixels(int color) {
        int area =0;
        for(int w=0; w < getWidth(); w++){
            for(int h=0; h < getHeight(); h++){
                if(getPixel(w, h) == color){
                    area++;
                }
            }
        }
        return area;
    }
    
 
    /**
     * Inicializar todos os pixel da imagem para um dado nivel de cinza
     * @param color
     */
    public void initImage(int color){
        this.flagMaxMin = false;
        for (int i = 0; i < getWidth(); i++){
            for (int j = 0; j < getHeight(); j++){
                setPixel(i,j, color);
            }
        }
    }
    
    /**
     * modifica todos os niveis de cinza de uma dada cor para uma outra cor
     * @param colorOld
     * @param colorNew
     */
    public void replaceColor(int colorOld, int colorNew){
    	for(int i=0; i < this.getSize(); i++){
    		if(getPixel(i) == colorOld)
    			setPixel(i, colorNew);
    	}
    }
    
    /**
     * Cria uma copia da imagem original
     * @return BinaryImage - nova imagem
     */
    public IGrayScaleImage duplicate(){
        GrayScaleImage clone = new GrayScaleImage(getWidth(), getHeight());
        clone.origin = origin;
        for (int i = 0; i < getWidth(); i++){
            for (int j = 0; j < getHeight(); j++){
                clone.setPixel(i,j,getPixel(i, j));
            }
        }
        return clone;
    }
    
    /**
     * Pega o valor do pixel (x, y)
     * @param x - largura
     * @param y - altura
     * @return float - valor do pixel
     */
    public int getPixel(int x, int y){
        if(x >= getWidth() || y >= getHeight()) throw new RuntimeException("(x="+x+", y="+y+") Nao existe essa posicao na matriz de pixels");
        return pixels[x][y];
    }
    
    
    /**
     * Modifica o valor do pixel (x, y) = value
     * @param x - largura
     * @param y - altura
     * @param value - valor do pixel
     */
    public void setPixel(int x, int y, int value){
        if(x >= getWidth() || y >= getHeight()) throw new RuntimeException("(x="+x+", y="+y+") Nao existe essa posicao na matriz de pixels");
        pixels[x][y] = value;
    }
    
    /**
     * Pega uma matriz bidimensional de pixel da imagem
     * @return int[][]
     */
    public int[][] getPixels(){
        return pixels;
    }
    
    
    /**
     * Modifica a matriz de pixel da imagem para os valores da matriz dada
     * @param matrix 
     */
    public void setPixels(int matrix[][]){
        this.width = matrix.length;
        this.height = matrix[0].length;
        this.pixels = new int[width][height];
        this.flagMaxMin = false;
        for (int i = 0; i < getWidth(); i++){
            for (int j = 0; j < getHeight(); j++){
                setPixel(i, j, matrix[i][j]);
            }
        }
    }
    
    /**
     * Modificao tamanho da matriz
     */
    public void resize(int width, int height){
        this.width = width;
        this.height = height;
        int matrix[][] = getPixels();
        this.pixels = new int[width][height];
        this.flagMaxMin = false;
        initImage(255);
        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[i].length; j++){
                setPixel(i, j, matrix[i][j]);
            }
        }
    }
    
    /**
     * Modificao tamanho da matriz
     */
    public void resizeCenter(int width, int height){
        this.width = width;
        this.height = height;
        int matrix[][] = getPixels();
        this.pixels = new int[width][height];
        this.flagMaxMin = false;
        initImage(255);
        
        for (int i = 0, x = Math.abs(matrix.length - width)/2; i < matrix.length; i++, x++){
            for (int j = 0, y = Math.abs(matrix.length - height)/2; j < matrix[i].length; j++, y++){
                setPixel(x, y, matrix[i][j]);
            }
        }
    }
    
    /**
     * Retorna a altura da imagem
     * @return int - altura
     */
    public int getHeight(){
        return height;
    }
    
    /**
     * Retorna a largura da imagem
     * @return int - largura
     */
    public int getWidth(){
        return width;
    }
    
    /**
     * Retorna o tamanho da imagem
     * @return
     */
    public int getSize(){
        return getHeight() * getWidth();
    }
    
    
    /**
     * Pega a posicao de x do ponto de origem da imagem
     * @return Point 
     */
    public int getOriginX(){
        return (int) origin.getX();
    }
    
    /**
     * Pega a posicao de y do ponto de origem da imagem
     * @return Point 
     */
    public int getOriginY(){
        return (int) origin.getY();
    }
    
    /**
     * Modifica o valor da origem do elemento estruturante
     * @param x
     * @param y
     */
    public void setOrigin(int x, int y){
        origin = new Point(x, y);
    }
    
    /**
     * Converte uma imagem de gray scale para binaria
     * @param limiar - limiar do threshold
     * @param background - background da imagem binaria
     */
    public IBinaryImage convertBinaryImage(int limiar, int backgound) {
        IBinaryImage imgB = new BinaryImage(getWidth(), getHeight());
        imgB.setBackground(backgound);
        imgB.setOrigin(this.getOriginX(), getOriginY());
        for (int i = 0; i < this.getWidth(); i++) {
            for (int j = 0; j < this.getHeight(); j++) {
                if (this.getPixel(i, j) > limiar)
                    imgB.setPixel(i, j, imgB.getBackground());
                else
                    imgB.setPixel(i, j,   imgB.getForeground());
            }
        }
        return imgB;
    }

    /**
     * Pega o maior pixel da imagem
     */
    public int getPixelMax() {
        if(!flagMaxMin)
            processMaxMin();
        return max;
    }
    

    /**
     * Pega o valor da media dos pixels da imagem
     */
    public int getPixelMean() {
        if(!flagMaxMin)
            processMaxMin();
        return mean;
    }
    
    
    
    private void processMaxMin(){
        max = Integer.MIN_VALUE;
        min = Integer.MAX_VALUE;
        int sum=0;
        for (int i = 0; i < getWidth(); i++){
            for (int j = 0; j < getHeight(); j++){
                sum += getPixel(i, j);
                if(getPixel(i, j) < min)
                    min = getPixel(i, j);
                if(getPixel(i, j) > max)
                    max = getPixel(i, j);
            }
        }
        mean = sum / (getWidth() * getHeight());
        flagMaxMin = true;
    }

    /**
     * Pega o menor pixel da imagem
     */
    public int getPixelMin() {
        if(!flagMaxMin)
            processMaxMin();
        return min;
    }

    /**
     * Pega um histograma da imagem
     * @return int[]
     */
    public int[] getHistogram() {
       int result[] = new int[256];
       for(int w=0; w < getWidth(); w++){
           for(int h=0; h < getHeight(); h++){
               result[getPixel(w, h)]++;
           }
       }
       return result;
    }

    /**
     * Pega o numero de pixel diferente na imagem
     */
    public int numPixels() {
        int h[] = getHistogram();
        int numPixel = 0;
        for(int i=0; i < h.length; i++) 
            if(h[i] > 0) numPixel++;
        return numPixel;
        
    }
    
    /**
     * Verifica se duas imagens sao iguais
     * @param img - IGrayScaleImage
     * @return true se forem iguais false caso contrario
     */
    public boolean equals(Object o){
        IGrayScaleImage img = (IGrayScaleImage) o;
        for(int x = 0 ; x < getWidth() ; x++)
            for(int y = 0 ; y < getHeight(); y++)
                if(getPixel(x, y) != img.getPixel(x, y)) 
                    return false;
                
        return true;
    }
    
    public void add(int a){
        for(int x = 0 ; x < getWidth() ; x++){
            for(int y = 0 ; y < getHeight(); y++){
                setPixel(x, y, getPixel(x, y) + a);
            }
        }
        
    }
    public void multiply(int a){
        for(int x = 0 ; x < getWidth() ; x++){
            for(int y = 0 ; y < getHeight(); y++){
                setPixel(x, y, getPixel(x, y) * a);
            }
        }
        
    }
    

    public void setPixel(int i, int level){
        int w = i % getWidth();
        int h = i / getWidth();
        pixels[w][h] = level;
    }
    
    public int getPixel(int i){
    	int w = i % getWidth();
        int h = i / getWidth();
        return pixels[w][h];
    }
 
    
    public IRGBImage randomColor(){
    	int max = this.getPixelMax();
    	int r[] = new int[max+1];
    	int g[] = new int[max+1];
    	int b[] = new int[max+1];
    	for(int i=0; i <= max; i++){
    		r[i] = ImageUtils.randomInteger(0, 255);
    		g[i] = ImageUtils.randomInteger(0, 255);
    		b[i] = ImageUtils.randomInteger(0, 255);
    	}
    	IRGBImage imgOut = new RGBImage(this.getWidth(), this.getHeight());
        for(int i=0; i < imgOut.getSize(); i++){
        	imgOut.setPixel(i, new Color(r[this.getPixel(i)], g[this.getPixel(i)], b[this.getPixel(i)]));
        }
        return imgOut;
    }
}
