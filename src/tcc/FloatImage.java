package tcc;

import tcc.utils.Point;

public class FloatImage implements IFloatImage {

    private int width; //largura da imagem
    private int height; //altura da imagem
    private float pixels[][]; //matriz de pixel da imagem
    private Point origin; //origem da imagem
    

    /**
     * Construtor para criar uma nova imagem
     * @param width - largura
     * @param height - altura
     */
    public FloatImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new float[width][height];
        this.origin = new Point(0,0);
    }
    
    public FloatImage(float matrix[][]){
    	this.width = matrix.length;
    	this.height = matrix[0].length;
    	this.pixels = matrix;
    	this.origin = new Point(0,0);
    }
     
    /**
     * Construtor para criar uma nova imagem
     * @param width - largura
     * @param height - altura
     */
    public FloatImage(int width, int height, float pixels[]) {
        setPixels(width, height,pixels);
        this.origin = new Point(0,0);
    }
    
    /**
     * Inicializar todos os pixel da imagem para um dado nivel de cinza
     * @param color
     */
    public void initImage(float color){
        for (int i = 0; i < getWidth(); i++){
            for (int j = 0; j < getHeight(); j++){
                setPixel(i,j, color);
            }
        }
    }

    /**
     * Cria uma copia da imagem original
     * @return FloatImage - nova imagem
     */
    public FloatImage duplicate(){
        FloatImage clone = new FloatImage(getWidth(), getHeight());
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
    public float getPixel(int x, int y){
        if(x >= getWidth() || y >= getHeight()) throw new RuntimeException("(x="+x+", y="+y+") Nao existe essa posicao na matriz de pixels");
        return pixels[x][y];
    }
    
    
    /**
     * Modifica o valor do pixel (x, y) = value
     * @param x - largura
     * @param y - altura
     * @param value - valor do pixel
     */
    public void setPixel(int x, int y, float value){
        if(x >= getWidth() || y >= getHeight()) throw new RuntimeException("(x="+x+", y="+y+") Nao existe essa posicao na matriz de pixels");
        pixels[x][y] = value;
    }
    
    /**
     * Pega uma matriz bidimensional de pixel da imagem
     * @return float[][]
     */
    public float[][] getPixels(){
        return pixels;
    }

    /**
     * Modifica a matriz de pixel da imagem para os valores da matriz dada
     * @param matrix 
     */
    public void setPixels(float matrix[][]){
        this.width = matrix.length;
        this.height = matrix[0].length;
        this.pixels = new float[width][height];
        for (int i = 0; i < getWidth(); i++){
            for (int j = 0; j < getHeight(); j++){
                setPixel(i, j, matrix[i][j]);
            }
        }
    }
    
    /**
     * Modifica a matriz de pixel da imagem para os valores da matriz dada
     * @param matrix 
     */
    public void setPixels(int width, int height, float matrix[]){
        this.width = width;
        this.height = height;
        this.pixels = new float[width][height];
        int w=0,h=0;
        for (int i = 0; i < matrix.length; i++){
            setPixel(w, h, matrix[i]);
            w++;
            if(w % width == 0){
                h++;
                w = 0;
            }
        }
    }
    

    /**
     * Pega o maior pixel da imagem
     */
    public float getPixelMax() {
        float max = Float.MIN_VALUE;
        for (int i = 0; i < getWidth(); i++){
            for (int j = 0; j < getHeight(); j++){
                if(getPixel(i, j) > max)
                    max = getPixel(i, j);
            }
        }
        return max;
    }
    
    /**
     * Pega o menor pixel da imagem
     */
    public float getPixelMin() {
        float min = Float.MAX_VALUE;
        for (int i = 0; i < getWidth(); i++){
            for (int j = 0; j < getHeight(); j++){
                if(getPixel(i, j) < min)
                    min = getPixel(i, j);
            }
        }
        return min;
    }

    /**
     * Verifica se duas imagens sao iguais
     * @param img - IGrayScaleImage
     * @return true se forem iguais false caso contrario
     */
    public boolean equals(Object o){
        FloatImage img = (FloatImage) o;
        for(int x = 0 ; x < getWidth() ; x++)
            for(int y = 0 ; y < getHeight(); y++)
                if(getPixel(x, y) != img.getPixel(x, y)) 
                    return false;
                
        return true;
    }

    public int getOriginX() {
        return origin.x;
    }

    public int getOriginY() {
        return origin.y;
    }

    public void setOrigin(int x, int y) {
        origin = new Point(x, y);
        
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    /**
     * Retorna o tamanho da imagem
     * @return
     */
    public int getSize(){
        return getHeight() * getWidth();
    }
    
    

    public void setPixel(int i, float level){
    	int w = i % getWidth();
        int h = i / getWidth();
        pixels[w][h] = level;
    }
    
    public float getPixel(int i){
    	int w = i % getWidth();
        int h = i / getWidth();
        return pixels[w][h];
    }
    
   
}
