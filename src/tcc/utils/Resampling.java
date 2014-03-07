package tcc.utils;

import tcc.BinaryImage;
import tcc.GrayScaleImage;
import tcc.IBinaryImage;
import tcc.IGrayScaleImage;
import tcc.IRGBImage;
import tcc.RGBImage;
 
/**
 * Project: Computer Vision Framework
 * 
 * @author Wonder Alexandre Luz Alves
 * @advisor Ronaldo Fumio Hashimoto
 * 
 * @date 10/07/2008
 *  
 * @description
 * Classe que implementa metodos de reamostragem de imagem
 * - ampliacao e reducao de imagem pelo vizinho mais proximo
 * - ampliacao e reducao de imagem pelo metodo bilinear
 * - rotacao de imagem
 */
public class Resampling {
    
    /**
     * Ampliacao e reducao de imagem pelo vizinho mais proximo
     * @param img - imagem de entrada
     * @param fator - fator de ampliacao - (1 = 100%)
     * @return imgem de saida
     */
    public static IGrayScaleImage nearestNeighbor(IGrayScaleImage img, float fator){
        IGrayScaleImage imgOut = new GrayScaleImage( (int)(img.getWidth() * fator), (int) (img.getHeight() * fator));
        int value;
        if(fator <= 0) throw new RuntimeException("fator precisa ser maior que 0!");
        for(int w=0; w < imgOut.getWidth(); w++){
            for(int h=0; h < imgOut.getHeight(); h++){
                if(ImageUtils.isIndexInImage(img, Math.round(w / fator), Math.round(h / fator))){
                    value = img.getPixel(Math.round(w / fator), Math.round(h / fator));
                    imgOut.setPixel(w,h, value);
                }
            }
        }
        return imgOut;
    }
    

    /**
     * Ampliacao e reducao de imagem pelo metodo bilinear
     * @param img - imagem de entrada
     * @param width - largura
     * @param heigth - altura
     * @return imgem de saida
     */
    public static IBinaryImage bilinear(IBinaryImage img, int width, int heigth){
    	
    	
    	IBinaryImage imgOut = new BinaryImage(width, heigth);
    	imgOut.setBackground(img.getBackground());
    	imgOut.setOrigin(img.getOriginX(), img.getOriginY());
    	imgOut.setId(img.getId());
    	
        int value;
        if(width <= 0 || heigth <= 0) throw new RuntimeException("largura ou altura da imagem precisa ser maior que 0!");
        
        double p1,p2,p3,p4;
        
        double fatorW = (double) width / (double) img.getWidth();
        double fatorH = (double) heigth / (double) img.getHeight();
        
        for(int w=0; w < imgOut.getWidth(); w++){
            for(int h=0; h < imgOut.getHeight(); h++){
                p1=p2=p3=p4=0;
                int ww = (int) Math.floor(w / fatorW);
                int hh = (int) Math.floor(h / fatorH);
                
                double dw = (w / fatorW) - ww;
                double dh = (h / fatorH) - hh;
                
                if(ImageUtils.isIndexInImage(img, ww, hh))
                    p1 = (1.0 - dw) * (1.0 - dh) * img.getPixel(ww, hh);
                if(ImageUtils.isIndexInImage(img, ww, hh+1))
                    p2 = dh * (1.0 - dw) * img.getPixel(ww, hh + 1);
                if(ImageUtils.isIndexInImage(img, ww+1, hh))
                    p3 = dw * (1.0 - dh) * img.getPixel(ww+1, hh);
                if(ImageUtils.isIndexInImage(img, ww+1, hh+1))
                    p4 = dh * dw * img.getPixel(ww+1, hh + 1);
                
                
                
                value = (int) Math.round(p1 + p2 + p3 + p4);
                imgOut.setPixel(w,h, value);
            }
        }
        return imgOut;
    }
    
    /**
     * Ampliacao e reducao de imagem pelo metodo bilinear
     * @param img - imagem de entrada
     * @param width - largura
     * @param heigth - altura
     * @return imgem de saida
     */
    public static IGrayScaleImage bilinear(IGrayScaleImage img, int width, int heigth){
    	
    	
        IGrayScaleImage imgOut = new GrayScaleImage(width, heigth);
        int value;
        if(width <= 0 || heigth <= 0) throw new RuntimeException("largura ou altura da imagem precisa ser maior que 0!");
        
        double p1,p2,p3,p4;
        
        double fatorW = (double) width / (double) img.getWidth();
        double fatorH = (double) heigth / (double) img.getHeight();
        
        for(int w=0; w < imgOut.getWidth(); w++){
            for(int h=0; h < imgOut.getHeight(); h++){
                p1=p2=p3=p4=0;
                int ww = (int) Math.floor(w / fatorW);
                int hh = (int) Math.floor(h / fatorH);
                
                double dw = (w / fatorW) - ww;
                double dh = (h / fatorH) - hh;
                
                if(ImageUtils.isIndexInImage(img, ww, hh))
                    p1 = (1.0 - dw) * (1.0 - dh) * img.getPixel(ww, hh);
                if(ImageUtils.isIndexInImage(img, ww, hh+1))
                    p2 = dh * (1.0 - dw) * img.getPixel(ww, hh + 1);
                if(ImageUtils.isIndexInImage(img, ww+1, hh))
                    p3 = dw * (1.0 - dh) * img.getPixel(ww+1, hh);
                if(ImageUtils.isIndexInImage(img, ww+1, hh+1))
                    p4 = dh * dw * img.getPixel(ww+1, hh + 1);
                
                
                
                value = (int) Math.round(p1 + p2 + p3 + p4);
                imgOut.setPixel(w,h, value);
            }
        }
        return imgOut;
    }

    /**
     * Ampliacao e reducao de imagem pelo metodo bilinear
     * @param img - imagem de entrada
     * @param width - largura
     * @param heigth - altura
     * @return imgem de saida
     */
    public static IRGBImage bilinear(IRGBImage img, int width, int heigth){
    	
    	
    	IRGBImage imgOut = new RGBImage(width, heigth);
        int value;
        if(width <= 0 || heigth <= 0) throw new RuntimeException("largura ou altura da imagem precisa ser maior que 0!");
        
        double p1,p2,p3,p4;
        
        double fatorW = (double) width / (double) img.getWidth();
        double fatorH = (double) heigth / (double) img.getHeight();
        

        
        for(int w=0; w < imgOut.getWidth(); w++){
            for(int h=0; h < imgOut.getHeight(); h++){
               for(int i=0; i < 3; i++){
                
                    p1=p2=p3=p4=0;
                    int ww = (int) Math.floor(w / fatorW);
                    int hh = (int) Math.floor(h / fatorH);
                    
                    double dw = (w / fatorW) - ww;
                    double dh = (h / fatorH) - hh;
                    
                    if(ImageUtils.isIndexInImage(img, ww, hh))
                        p1 = (1.0 - dw) * (1.0 - dh) * img.getPixel(ww, hh, i);
                    if(ImageUtils.isIndexInImage(img, ww, hh+1))
                        p2 = dh * (1.0 - dw) * img.getPixel(ww, hh + 1, i);
                    if(ImageUtils.isIndexInImage(img, ww+1, hh))
                        p3 = dw * (1.0 - dh) * img.getPixel(ww+1, hh, i);
                    if(ImageUtils.isIndexInImage(img, ww+1, hh+1))
                        p4 = dh * dw * img.getPixel(ww+1, hh + 1, i);
                    
                    
                    
                    value = (int) Math.round(p1 + p2 + p3 + p4);
                    imgOut.setPixel(w,h, value, i);
               }
            }
        }
        return imgOut;
    }


    /**
     * Ampliacao e reducao de imagem pelo metodo bilinear
     * @param img - imagem de entrada
     * @param fator - fator de ampliacao - (1 = 100%)
     * @return imgem de saida
     */
    public static IBinaryImage bilinear(IBinaryImage img, float fator){
    	IBinaryImage imgOut = new BinaryImage( (int)(img.getWidth() * fator), (int) (img.getHeight() * fator));
    	imgOut.setBackground(img.getBackground());
    	imgOut.setOrigin(img.getOriginX(), img.getOriginY());
    	imgOut.setId(img.getId());
        int value;
        if(fator <= 0) throw new RuntimeException("fator precisa ser maior que 0!");
        
        double p1,p2,p3,p4;
        
        
        for(int w=0; w < imgOut.getWidth(); w++){
            for(int h=0; h < imgOut.getHeight(); h++){
                p1=p2=p3=p4=0;
                int ww = (int) Math.floor(w / fator);
                int hh = (int) Math.floor(h / fator);
                
                double dw = (w / fator) - ww;
                double dh = (h / fator) - hh;
                
                if(ImageUtils.isIndexInImage(img, ww, hh))
                    p1 = (1.0 - dw) * (1.0 - dh) * img.getPixel(ww, hh);
                if(ImageUtils.isIndexInImage(img, ww, hh+1))
                    p2 = dh * (1.0 - dw) * img.getPixel(ww, hh + 1);
                if(ImageUtils.isIndexInImage(img, ww+1, hh))
                    p3 = dw * (1.0 - dh) * img.getPixel(ww+1, hh);
                if(ImageUtils.isIndexInImage(img, ww+1, hh+1))
                    p4 = dh * dw * img.getPixel(ww+1, hh + 1);
                
                
                
                value = (int) Math.round(p1 + p2 + p3 + p4);
                imgOut.setPixel(w,h, value);
            }
        }
        return imgOut;
    }
    
    /**
     * Ampliacao e reducao de imagem pelo metodo bilinear
     * @param img - imagem de entrada
     * @param fator - fator de ampliacao - (1 = 100%)
     * @return imgem de saida
     */
    public static IGrayScaleImage bilinear(IGrayScaleImage img, float fator){
        IGrayScaleImage imgOut = new GrayScaleImage( (int)(img.getWidth() * fator), (int) (img.getHeight() * fator));
        int value;
        if(fator <= 0) throw new RuntimeException("fator precisa ser maior que 0!");
        
        double p1,p2,p3,p4;
        
        
        for(int w=0; w < imgOut.getWidth(); w++){
            for(int h=0; h < imgOut.getHeight(); h++){
                p1=p2=p3=p4=0;
                int ww = (int) Math.floor(w / fator);
                int hh = (int) Math.floor(h / fator);
                
                double dw = (w / fator) - ww;
                double dh = (h / fator) - hh;
                
                if(ImageUtils.isIndexInImage(img, ww, hh))
                    p1 = (1.0 - dw) * (1.0 - dh) * img.getPixel(ww, hh);
                if(ImageUtils.isIndexInImage(img, ww, hh+1))
                    p2 = dh * (1.0 - dw) * img.getPixel(ww, hh + 1);
                if(ImageUtils.isIndexInImage(img, ww+1, hh))
                    p3 = dw * (1.0 - dh) * img.getPixel(ww+1, hh);
                if(ImageUtils.isIndexInImage(img, ww+1, hh+1))
                    p4 = dh * dw * img.getPixel(ww+1, hh + 1);
                
                
                
                value = (int) Math.round(p1 + p2 + p3 + p4);
                imgOut.setPixel(w,h, value);
            }
        }
        return imgOut;
    }
    
    /**
     * Ampliacao e reducao de imagem pelo metodo bilinear
     * @param img - imagem de entrada
     * @param fator - fator de ampliacao - (1 = 100%)
     * @return imgem de saida
     */ 
    public static RGBImage bilinear(IRGBImage img, float fator){
        RGBImage imgOut = new RGBImage( (int)(img.getWidth() * fator), (int) (img.getHeight() * fator));
        int value;
        if(fator <= 0) throw new RuntimeException("fator precisa ser maior que 0!");
        
        double p1,p2,p3,p4;
        
        
        for(int w=0; w < imgOut.getWidth(); w++){
            for(int h=0; h < imgOut.getHeight(); h++){
               for(int i=0; i < 3; i++){
                
                    p1=p2=p3=p4=0;
                    int ww = (int) Math.floor(w / fator);
                    int hh = (int) Math.floor(h / fator);
                    
                    double dw = (w / fator) - ww;
                    double dh = (h / fator) - hh;
                    
                    if(ImageUtils.isIndexInImage(img, ww, hh))
                        p1 = (1.0 - dw) * (1.0 - dh) * img.getPixel(ww, hh, i);
                    if(ImageUtils.isIndexInImage(img, ww, hh+1))
                        p2 = dh * (1.0 - dw) * img.getPixel(ww, hh + 1, i);
                    if(ImageUtils.isIndexInImage(img, ww+1, hh))
                        p3 = dw * (1.0 - dh) * img.getPixel(ww+1, hh, i);
                    if(ImageUtils.isIndexInImage(img, ww+1, hh+1))
                        p4 = dh * dw * img.getPixel(ww+1, hh + 1, i);
                    
                    
                    
                    value = (int) Math.round(p1 + p2 + p3 + p4);
                    imgOut.setPixel(w,h, value, i);
               }
            }
        }
        return imgOut;
    }
    
    
    /**
     * Rotacao em sentido anti-horario
     * @param img - imagem de entrada
     * @param graus - graus
     * @return imgem de saida
     */
    public static IGrayScaleImage rotation(IGrayScaleImage img, float graus){
        GrayScaleImage imgOut = new GrayScaleImage(img.getWidth(), img.getHeight());
        //imgOut.initImage(255);
        
        double radian = 2.0 * Math.PI * graus / 360.0;
        double ct = Math.cos(radian);
        double st = Math.sin(radian);
        
        int hc = img.getHeight()/2;
        int wc = img.getWidth()/2;
        for(int w=0; w < img.getWidth(); w++){
            for(int h=0; h < img.getHeight(); h++){
                int nh = (int) ((h - hc) * ct - (w - wc) * st + hc);
                int nw = (int) ((h - hc) * st + (w - wc) * ct + wc);
                if(ImageUtils.isIndexInImage(img, nw, nh))
                    imgOut.setPixel(w, h, img.getPixel(nw, nh));
                else
                    imgOut.setPixel(w, h, 255);
               
               
            }
        }
        return imgOut;//adjustImage(imgOut);
        
    }
    

    
    /**
     * Rotacao em sentido anti-horario
     * @param img - imagem de entrada
     * @param graus - graus
     * @return imgem de saida
     */
    public static IBinaryImage rotation(IBinaryImage img, double graus){
        IBinaryImage imgOut = new BinaryImage(img.getWidth(), img.getHeight());
        imgOut.setBackground(img.getBackground());
       // if(img.getBackground() != 0)
       //     imgOut.initImage(img.getBackground());
        
        double radian = 2.0 * Math.PI * graus / 360.0;
        double ct = Math.cos(radian);
        double st = Math.sin(radian);
        
        int hc = img.getHeight()/2;
        int wc = img.getWidth()/2; 
        for(int w=0; w < img.getWidth(); w++){
            for(int h=0; h < img.getHeight(); h++){
                int nh = (int) ((h - hc) * ct - (w - wc) * st + hc);
                int nw = (int) ((h - hc) * st + (w - wc) * ct + wc);
                if(ImageUtils.isIndexInImage(img, nw, nh))
                    imgOut.setPixel(w, h, img.getPixel(nw, nh));
                else
                	imgOut.setPixel(w, h, img.getBackground());
            }
        }
        return imgOut;
    }
    
    
    
    /**
     * Rotacao em sentido anti-horario
     * @param img - imagem de entrada
     * @param graus - graus
     * @return imgem de saida
     */
    public static IRGBImage rotation(IRGBImage img, double graus){
        IRGBImage imgOut = new RGBImage(img.getWidth(), img.getHeight());
        
        double radian = 2.0 * Math.PI * graus / 360.0;
        double ct = Math.cos(radian);
        double st = Math.sin(radian);
        
        int hc = img.getHeight()/2;
        int wc = img.getWidth()/2;
        for(int w=0; w < img.getWidth(); w++){
            for(int h=0; h < img.getHeight(); h++){
                int nh = (int) ((h - hc) * ct - (w - wc) * st + hc);
                int nw = (int) ((h - hc) * st + (w - wc) * ct + wc);
                if(ImageUtils.isIndexInImage(img, nw, nh))
                    imgOut.setPixel(w, h, img.getPixel(nw, nh));
            }
        }
        return imgOut;
    }
    
  
    
    
}
