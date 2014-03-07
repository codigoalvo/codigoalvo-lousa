package tcc.operators;

import tcc.GrayScaleImage;
import tcc.IGrayScaleImage;
import tcc.utils.AdjacencyRelation;
import tcc.utils.ImageArithmetic;

public class MorphlogicalOperators {

	public static IGrayScaleImage opening(IGrayScaleImage img, AdjacencyRelation adjEE){
		return MorphlogicalOperators.erosion(MorphlogicalOperators.dilation(img, adjEE), adjEE);
	}

	public static IGrayScaleImage closing(IGrayScaleImage img, AdjacencyRelation adjEE){
		return MorphlogicalOperators.dilation(MorphlogicalOperators.erosion(img, adjEE), adjEE);
	} 

	
	public static IGrayScaleImage dilation(IGrayScaleImage img, AdjacencyRelation adjEE){
		IGrayScaleImage imgOut = new GrayScaleImage(img.getWidth(), img.getHeight());
		int max;
		for(int p = 0; p < img.getSize(); p++){
			max = 0;
			for(Integer q: adjEE.getAdjPixelsVectorDesc(img, p)){
				if(max < img.getPixel(q))
					max = img.getPixel(q);
			}
			imgOut.setPixel(p, max);
		}
		return imgOut;
	}
	
	
	public static IGrayScaleImage erosion(IGrayScaleImage img, AdjacencyRelation adjEE){
		IGrayScaleImage imgOut = new GrayScaleImage(img.getWidth(), img.getHeight());
		int min;
		for(int p = 0; p < img.getSize(); p++){
			min = 255;
			for(Integer q: adjEE.getAdjPixelsVectorDesc(img, p)){
				if(min > img.getPixel(q)){
					min = img.getPixel(q);
				}
			}
			imgOut.setPixel(p, min);
		}
		return imgOut;
	}

	public static IGrayScaleImage gradient(IGrayScaleImage img, AdjacencyRelation adjB){
		IGrayScaleImage imgOut = new GrayScaleImage(img.getWidth(), img.getHeight());
		IGrayScaleImage dilatacao = dilation(img, adjB);
		IGrayScaleImage erode = erosion(img, adjB);
		for(int p=0; p < img.getSize(); p++){
			imgOut.setPixel(p, dilatacao.getPixel(p) - erode.getPixel(p));
		}
		return imgOut;
		
	}
	public static IGrayScaleImage gradientInternal(IGrayScaleImage img, AdjacencyRelation adjB){
		IGrayScaleImage imgOut = new GrayScaleImage(img.getWidth(), img.getHeight());
		IGrayScaleImage erode = erosion(img, adjB);
		for(int p=0; p < img.getSize(); p++){
			imgOut.setPixel(p, img.getPixel(p) - erode.getPixel(p));
		}
		
		return imgOut;
		
	}
	
    
    public static IGrayScaleImage closingTopHat(IGrayScaleImage img, AdjacencyRelation se){
        return ImageArithmetic.subtraction(MorphlogicalOperators.closing(img, se), img);
    }
    
    public static IGrayScaleImage openingTopHat(IGrayScaleImage img, AdjacencyRelation se){
        return ImageArithmetic.subtraction(img, MorphlogicalOperators.opening(img, se));
    }
    

}
