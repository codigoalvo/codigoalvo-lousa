package tcc.utils;

import tcc.GrayScaleImage;
import tcc.IGrayScaleImage;

public class ImageArithmetic {

	/**
     * Minimo entre duas imagem
     * @param imgA
     * @param imgB
     * @return
     */
    public static IGrayScaleImage minimum(IGrayScaleImage imgA, IGrayScaleImage imgB){
        IGrayScaleImage imgM = new GrayScaleImage(imgA.getWidth(), imgA.getHeight());
        for(int w=0; w < imgA.getWidth(); w++){
            for(int h=0; h < imgA.getHeight(); h++){
                if(imgA.getPixel(w,h) < imgB.getPixel(w,h))
                    imgM.setPixel(w, h, imgA.getPixel(w,h));
                else
                    imgM.setPixel(w, h, imgB.getPixel(w,h));
            }
        }
        return imgM;
    }
        
    /**
     * Maximo entre duas imagens
     * @param imgA
     * @param imgB
     * @return
     */
    public static IGrayScaleImage maximum(IGrayScaleImage imgA, IGrayScaleImage imgB){
        IGrayScaleImage imgM = new GrayScaleImage(imgA.getWidth(), imgA.getHeight());
        for(int w=0; w < imgA.getWidth(); w++){
            for(int h=0; h < imgA.getHeight(); h++){
                if(imgA.getPixel(w,h) > imgB.getPixel(w,h))
                    imgM.setPixel(w, h, imgA.getPixel(w,h));
                else
                    imgM.setPixel(w, h, imgB.getPixel(w,h));
            }
        }
        return imgM;
    }

    /**
     * Faz a diferenca entre duas imagens 
     * @param imgA - imagem A
     * @param imgB - imagem B
     * @return IGrayScaleImage com a diferenca entre a imagem A e a imagem B
     */
    public static IGrayScaleImage subtraction(IGrayScaleImage imgA, IGrayScaleImage imgB){
        IGrayScaleImage imgOut = new GrayScaleImage(imgA.getWidth(), imgA.getHeight());
        int tmp = 0;
        for(int x = 0 ; x < imgA.getWidth() ; x++){
            for(int y = 0 ; y < imgA.getHeight(); y++){
                if(x < imgB.getWidth() && y < imgB.getHeight())
                    tmp = (Math.abs(imgA.getPixel(x, y) - imgB.getPixel(x, y)));
                    imgOut.setPixel(x, y, tmp);
            }
        }
        return imgOut;
    }
  

    /**
     * Verifica se duas imagens sao iguais
     * @param imgA - IGrayScaleImage
     * @param imgB - IGrayScaleImage
     * @return true se forem iguais false caso contrario
     */
    public static boolean equals(IGrayScaleImage imgA, IGrayScaleImage imgB){
        for(int x = 0 ; x < imgA.getWidth() ; x++)
            for(int y = 0 ; y < imgA.getHeight(); y++)
                if(imgA.getPixel(x, y) != imgB.getPixel(x, y)) return false;
                
        return true;
    }
}
