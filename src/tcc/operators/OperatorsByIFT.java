package tcc.operators;

import tcc.GrayScaleImage;
import tcc.IGrayScaleImage;
import tcc.datastruct.PriorityQueueDial;
import tcc.utils.AdjacencyRelation;

public abstract class OperatorsByIFT {


	
	public static IGrayScaleImage watershedByMarker(AdjacencyRelation adj, IGrayScaleImage img, IGrayScaleImage marked){
		long ti = System.currentTimeMillis();	
		 IGrayScaleImage imgMapaPredecessores = new GrayScaleImage(img.getWidth(), img.getHeight());
		 IGrayScaleImage imgMapaCusto = new GrayScaleImage(img.getWidth(), img.getHeight());
		 IGrayScaleImage imgLabel = new GrayScaleImage(img.getWidth(), img.getHeight());
		 int NIL = -1;
		 boolean NO_PROCESSED = false;
		 boolean PROCESSED = true;
		 boolean states[] = new boolean[img.getSize()];
		 int infinitoValue = img.getPixelMax() + 1;
		 PriorityQueueDial fifo = new PriorityQueueDial(imgMapaCusto, infinitoValue, PriorityQueueDial.FIFO);
		 
		 for(int p = 0; p < img.getSize(); p++){
			 imgMapaPredecessores.setPixel(p, NIL);
			 if(marked.getPixel(p) != -1){
				 imgMapaCusto.setPixel(p, img.getPixel(p) + 1);
				 imgLabel.setPixel(p, marked.getPixel(p));
				 fifo.add(p);
			 }else{
				 imgMapaCusto.setPixel(p, infinitoValue);
				}
		 }
	
		 
		 while(!fifo.isEmpty()){
			 int p = fifo.remove();
			 states[p] = PROCESSED;
			 
			 if(imgMapaPredecessores.getPixel(p) == NIL){
				 imgMapaCusto.setPixel(p, img.getPixel(p));
			 }
			 
			 for(Integer q: adj.getAdjPixelsVectorDesc(img, p)){
				 if(states[q] == NO_PROCESSED){
					 int custoAresta = ( img.getPixel(q) + img.getPixel(p) ) / 2;  
					 int tmp = Math.max(imgMapaCusto.getPixel(p), custoAresta);
					 if(tmp < imgMapaCusto.getPixel(q)){
						 if(states[q] == NO_PROCESSED){
							 fifo.remove(q);
						 }
						 imgLabel.setPixel(q, imgLabel.getPixel(p));
						 imgMapaPredecessores.setPixel(q, p);
						 imgMapaCusto.setPixel(q, tmp);
						 fifo.add(q);	    
					 }
				 }
			 }  
		 }
			
		 long tf = System.currentTimeMillis();
		 System.out.println("Tempo de execucao [watershedPorMarcador]  "+ ((tf - ti) /1000.0)  + "s");
		 imgMapaCusto = null;
		 imgMapaPredecessores = null;
		 states = null;
		 
		 return imgLabel;
	}

	

}
