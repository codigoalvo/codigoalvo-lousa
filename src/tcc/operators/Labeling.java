package tcc.operators;

import java.util.List;

import tcc.GrayScaleImage;
import tcc.IBinaryImage;
import tcc.IGrayScaleImage;
import tcc.datastruct.Queue;
import tcc.utils.AdjacencyRelation;

public class Labeling {

	public static IGrayScaleImage labeling(IBinaryImage img, AdjacencyRelation adj){
		IGrayScaleImage imgOut = new GrayScaleImage(img.getWidth(), img.getHeight());
		
		int label = 1;
		for(int p=0; p < img.getSize(); p++){
			if(imgOut.getPixel(p) != 0) continue;
			imgOut.setPixel(p, label);
			Queue<Integer> fifo = new Queue<Integer>();
			fifo.enqueue(p);
			while(!fifo.isEmpty()){
				int pixel = fifo.dequeue();
				List<Integer> Np = adj.getAdjPixelsVectorDesc(img, pixel);
				for(Integer q: Np){
					if(imgOut.getPixel(q) == 0 && img.getPixel(p) == img.getPixel(q) && !img.isPixelBackground(p)){
						imgOut.setPixel(q, label);
						fifo.enqueue(q);
					}
				}
			}
			label++;
		}
		return imgOut;
	}
	
	
	public static IGrayScaleImage labeling(IGrayScaleImage img, AdjacencyRelation adj){
		IGrayScaleImage imgOut = new GrayScaleImage(img.getWidth(), img.getHeight());
		
		int label = 1;
		for(int p=0; p < img.getSize(); p++){
			if(imgOut.getPixel(p) != 0) continue;
			imgOut.setPixel(p, label);
			Queue<Integer> fifo = new Queue<Integer>();
			fifo.enqueue(p);
			while(!fifo.isEmpty()){
				int pixel = fifo.dequeue();
				List<Integer> Np = adj.getAdjPixelsVectorDesc(img, pixel);
				for(Integer q: Np){
					if(imgOut.getPixel(q) == 0 && img.getPixel(p) == img.getPixel(q)){
						imgOut.setPixel(q, label);
						fifo.enqueue(q);
					}
				}
			}
			label++;
		}
		return imgOut;
	}
	
	public static IGrayScaleImage labeling(IGrayScaleImage img, AdjacencyRelation adj, int k){
		IGrayScaleImage imgOut = new GrayScaleImage(img.getWidth(), img.getHeight());
		
		int label = 1;
		for(int p=0; p < img.getSize(); p++){
			if(imgOut.getPixel(p) != 0) continue;
			imgOut.setPixel(p, label);
			Queue<Integer> fifo = new Queue<Integer>();
			fifo.enqueue(p);
			while(!fifo.isEmpty()){
				int pixel = fifo.dequeue();
				List<Integer> Np = adj.getAdjPixelsVectorDesc(img, pixel);
				for(Integer q: Np){
					if(imgOut.getPixel(q) == 0 && Math.abs( img.getPixel(p) - img.getPixel(q) ) <= k){
						imgOut.setPixel(q, label);
						fifo.enqueue(q);
					}
				}
			}
			label++;
		}
		return imgOut;
	}
	

	
}
