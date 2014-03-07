package tcc;

import tcc.utils.Color;
import tcc.utils.Point;

import tcc.utils.ImageUtils;


/**
 * Project: Computer Vision Framework
 * 
 * @author Wonder Alexandre Luz Alves
 * @advisor Ronaldo Fumio Hashimoto
 * 
 * @date 10/09/2007
 *  
 * @description
 * Classe que representa uma imagem digital. 
 * Essa classe utiliza somente as APIs do java para manipular os pixels da imagens  
 * 
 */ 
public class RGBImage implements IRGBImage{
    private int width; //largura da imagem
    private int height; //altura da imagem 
    private int pixels[][][]; //matriz de pixel da imagem
    private Point origin; //origem da imagem
    


    /**
     * Construtor para criar uma nova imagem
     * @param width - largura
     * @param height - altura
     */
    public RGBImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[3][width][height];
        this.origin = new Point(0,0);
    }

    public RGBImage(int matrix[][][]){
    	this.width = matrix[0].length;
    	this.height = matrix[0][0].length;
    	this.pixels = matrix;
    	this.origin = new Point(0,0);
    }
    
    /**
     * Construtor para criar uma nova imagem com os dados da imagem de entrada
     * @param img - imagem de entrada
     */
    public RGBImage(IGrayScaleImage img) {
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.pixels = new int[3][width][height];
        this.origin = new Point(0,0);
        setPixels(img.getPixels());
    }
    

    /**
     * Construtor para criar uma nova imagem com os dados da imagem de entrada
     * @param img - imagem de entrada
     */
    public RGBImage(IBinaryImage img) {
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.pixels = new int[3][width][height];
        this.origin = new Point(0,0);
        setPixels(img.getPixels());
    }

    /**
     * Inverter os pixels da imagem [255 - pixel(x,y)]
     * @return
     */
    public IRGBImage invert(){
        IRGBImage imgOut = new RGBImage(this.getWidth(), this.getHeight());
        for(int w=0;w<this.getWidth();w++){
            for(int h=0;h<this.getHeight();h++){
            	for(int b=0; b < 3; b++)
            		imgOut.setPixel(w, h, b, 255 - this.getPixel(w, h,b));
            }
        } 
        return imgOut;
    }
    
    public void paintBoundBox(int x1, int y1, int x2, int y2, Color c){
        int w = x2 - x1;
        int h = y2 - y1;
        for(int i=0; i < w; i++){
            for(int j=0; j < h; j++){
                if(i <= 1 || j <= 1 || i > w-3 || j > h-3)
                    setPixel(x1 + i, y1 + j, c);        
            }
        }
    }
    
    

    
    /**
     * Inicializar todos os pixel da imagem para um dado nivel de cinza
     * @param color
     */
    public void initImage(int color){
        for (int i = 0; i < getWidth(); i++){
            for (int j = 0; j < getHeight(); j++){
                setPixel(i,j, color,0);
                setPixel(i,j, color,1);
                setPixel(i,j, color,2);
            }
        }
    }
    
    /**
     * Cria uma copia da imagem original
     * @return BinaryImage - nova imagem
     */
    public IRGBImage duplicate(){
        RGBImage clone = new RGBImage(getWidth(), getHeight());
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
    public Color getPixel(int x, int y){
        if(x >= getWidth() || y >= getHeight()) throw new RuntimeException("(x="+x+", y="+y+") Nao existe essa posicao na matriz de pixels");
        return new Color(pixels[0][x][y], pixels[1][x][y], pixels[2][x][y]);
    }
    
    

    
    /**
     * Modifica o valor do pixel (x, y) = value
     * @param x - largura
     * @param y - altura
     * @param value - valor do pixel
     */
    public void setPixel(int x, int y, int value, int band){
        if(x >= getWidth() || y >= getHeight()) throw new RuntimeException("(x="+x+", y="+y+") Nao existe essa posicao na matriz de pixels");
        pixels[band][x][y] = value;
    }
    
    /**
     * Pega uma matriz bidimensional de pixel da imagem de um banda especifica
     * @return int[][]
     */
    public int[][] getPixels(int band){
        return pixels[band];
    }
    
   
    
    /**
     * Modifica a matriz de pixel da imagem para os valores da matriz dada
     * @param matrix 
     */
    public void setPixels(int matrix[][]){
        for (int i = 0; i < getWidth(); i++){
            for (int j = 0; j < getHeight(); j++){
                setPixel(i, j, matrix[i][j], 0);
                setPixel(i, j, matrix[i][j], 1);
                setPixel(i, j, matrix[i][j], 2);
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

   
    
    public int getPixel(int x, int y, int band) {
        if(x >= getWidth() || y >= getHeight()) throw new RuntimeException("(x="+x+", y="+y+") Nao existe essa posicao na matriz de pixels");
        return pixels[band][x][y];
    }

    public void setPixel(int x, int y, int[] value) {
        if(x >= getWidth() || y >= getHeight()) throw new RuntimeException("(x="+x+", y="+y+") Nao existe essa posicao na matriz de pixels");
        pixels[0][x][y] = value[0];
        pixels[1][x][y] = value[1];
        pixels[2][x][y] = value[2];
    }
    
    public void setPixel(int x, int y, int level) {
        if(x >= getWidth() || y >= getHeight()) throw new RuntimeException("(x="+x+", y="+y+") Nao existe essa posicao na matriz de pixels");
        pixels[0][x][y] = level;
        pixels[1][x][y] = level;
        pixels[2][x][y] = level;
    }

    public int[][][] getPixels() {
        return pixels;
    }
    
    public void setPixel(int x, int y, Color c) {
        if(x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) throw new RuntimeException("(x="+x+", y="+y+") Nao existe essa posicao na matriz de pixels");
        pixels[0][x][y] = c.getRed();
        pixels[1][x][y] = c.getGreen();
        pixels[2][x][y] = c.getBlue();
    }

    public void setPixels(int[][][] matrix) {
        pixels = matrix;
        
    }

    public void setPixels(int band, int[][] matrix) {
        pixels[band] = matrix;
        
    }

    public void setPixel(int i, Color c){
    	int w = i % getWidth();
    	int h = i / getWidth();
        pixels[0][w][h] = c.getRed();
        pixels[1][w][h] = c.getGreen();
        pixels[2][w][h] = c.getBlue();
    }

    public void setPixel(int i, int level){
    	int w = i % getWidth();
    	int h = i / getWidth();
        pixels[0][w][h] = level;
        pixels[1][w][h] = level;
        pixels[2][w][h] = level;
    }
    
    public Color getPixel(int i){
    	int w = i % getWidth();
    	int h = i / getWidth();
        return new Color(pixels[0][w][h], pixels[1][w][h], pixels[2][w][h]);
    }
    
    /**
     * Converte uma imagem em RGB para niveis de cinza
     */
    public IGrayScaleImage convertGrayScaleImage() {
        IGrayScaleImage image = new GrayScaleImage(this.getWidth(), this.getHeight());
        int r, g, b;
        for(int w=0;w<this.getWidth();w++){
            for(int h=0;h<this.getHeight();h++){
                r = this.getPixel(w, h, 0);
                g = this.getPixel(w, h, 1);
                b = this.getPixel(w, h, 2);
                image.setPixel(w, h, (int) Math.round(.299*r + .587*g + .114*b)); //convertendo para niveis de cinza
            }
        } 
        return image;
    }

   
    public void addSubImage(IRGBImage img, int x, int y){
        for(int i=x; i < img.getWidth(); i++){
            for(int j=y; j < img.getHeight(); j++){
                if(ImageUtils.isIndexInImage(this, i, j))
                    setPixel(i, j, img.getPixel(i, j));
            }
        }
    }
    
    public void addSubImage(IGrayScaleImage img, int x, int y){
        for(int i=x; i < img.getWidth(); i++){
            for(int j=y; j < img.getHeight(); j++){
                if(ImageUtils.isIndexInImage(this, i, j))
                    setPixel(i, j, img.getPixel(i, j));
            }
        }
    }

 
    
}
