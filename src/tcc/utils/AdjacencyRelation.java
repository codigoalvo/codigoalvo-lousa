package tcc.utils;

import java.util.ArrayList;
import java.util.List;

import tcc.IImage;

public class AdjacencyRelation {
	int px[];
	int py[];
	//int p[];
	
	public int getSize(){
		return px.length;
	}
	
	private AdjacencyRelation(int n) {
		px = new int[n];
		py = new int[n];
	}
	
	public List<Integer> getAdjPixelsVectorDesc(IImage img, int x, int y) {
        ArrayList<Integer> neighbors = new ArrayList<Integer>();        
        for(int i=0; i < px.length; i++){
        	if(ImageUtils.isIndexInImage(img, px[i] + x, py[i] + y))
        			neighbors.add( (py[i] + y) * img.getWidth() + (px[i] + x) );
        }
        return neighbors;
    }
	
	public List<Integer> getAdjPixelsVectorDesc(IImage img, int i) {
    	int x = i % img.getWidth();
    	int y =i / img.getWidth();
        ArrayList<Integer> neighbors = new ArrayList<Integer>();        
        for(i=0; i < px.length; i++){
        	if(ImageUtils.isIndexInImage(img, px[i] + x, py[i] + y))
        			neighbors.add( ((py[i] + y) * img.getWidth()) + (px[i] + x) );
        }
        return neighbors;
    }
	
	
	public AdjacencyRelation CloneAdjRel(){
		AdjacencyRelation adj = new AdjacencyRelation(px.length);
		int i;  
		for(i=0; i < this.px.length; i++){
			adj.px[i] = this.px[i];
			adj.py[i] = this.py[i];
		}
		return adj;
	}
	

	
	public static AdjacencyRelation getFastCircular(float raio) {

		int i, n, dx, dy, r0, r2, auxX, auxY, i0 = 0;
		n = 0;
		r0 = (int) raio;
		r2 = (int) (raio * raio);
		for (dy = -r0; dy <= r0; dy++) {
			for (dx = -r0; dx <= r0; dx++) {
				if (((dx * dx) + (dy * dy)) <= r2) {
					n++;
				}
			}
		}
		AdjacencyRelation adj = new AdjacencyRelation(n);

		i = 0;
		for (dy = -r0; dy <= r0; dy++) {
			for (dx = -r0; dx <= r0; dx++) {
				if (((dx * dx) + (dy * dy)) <= r2) {
					adj.px[i] =dx;
					adj.py[i] =dy;
					if ((dx == 0) && (dy == 0))
						i0 = i;
					i++;
				}
			}
		}

		/* origem */
		auxX = adj.px[i0];
		auxY = adj.py[i0];
		adj.px[i0] = adj.px[0];
		adj.py[i0] = adj.py[0];
		
		adj.px[0] = auxX;
		adj.py[0] = auxY;
		
		return (adj);
	}

	public static AdjacencyRelation getCircular(float raio) {

		int i, j, k, n, dx, dy, r0, r2, i0 = 0;
		n = 0;
		r0 = (int) raio;
		r2 = (int) (raio * raio);
		for (dy = -r0; dy <= r0; dy++)
			for (dx = -r0; dx <= r0; dx++)
				if (((dx * dx) + (dy * dy)) <= r2)
					n++;

		AdjacencyRelation adj = new AdjacencyRelation(n);

		i = 0;
		for (dy = -r0; dy <= r0; dy++) {
			for (dx = -r0; dx <= r0; dx++) {
				if (((dx * dx) + (dy * dy)) <= r2) {
					adj.px[i] =dx;
					adj.py[i] =dy;
					if ((dx == 0) && (dy == 0))
						i0 = i;
					i++;
				}
			}
		}

		double aux;
		double da[] = new double[n];
		double dr[] = new double[n];

		/* ordenacao clockwise */
		for (i = 0; i < n; i++) {
			dx = adj.px[i];
			dy = adj.py[i];
			dr[i] = Math.sqrt((dx * dx) + (dy * dy));
			if (i != i0) {
				da[i] = (Math.atan2(-dy, -dx) * 180.0 / Math.PI);
				if (da[i] < 0.0)
					da[i] += 360.0;
			}
		}
		da[i0] = 0.0;
		dr[i0] = 0.0;

		/* origem */
		aux = da[i0];
		da[i0] = da[0];
		da[0] = aux;

		aux = dr[i0];
		dr[i0] = dr[0];
		dr[0] = aux;

		int auxX, auxY;
		auxX = adj.px[i0];
		auxY = adj.py[i0];
		adj.px[i0] = adj.px[0];
		adj.py[i0] = adj.py[0];
		
		adj.px[0] = auxX;
		adj.py[0] = auxY;
		

		/* sort by angle */
		for (i = 1; i < n - 1; i++) {
			k = i;
			for (j = i + 1; j < n; j++)
				if (da[j] < da[k]) {
					k = j;
				}
			aux = da[i];
			da[i] = da[k];
			da[k] = aux;
			aux = dr[i];
			dr[i] = dr[k];
			dr[k] = aux;

			auxX = adj.px[i];
			auxY = adj.py[i];
			adj.px[i] = adj.px[k];
			adj.py[i] = adj.py[k];
			
			adj.px[k] = auxX;
			adj.py[k] = auxY;
		}

		/* sort by radius for each angle */
		for (i = 1; i < n - 1; i++) {
			k = i;
			for (j = i + 1; j < n; j++)
				if ((dr[j] < dr[k]) && (da[j] == da[k])) {
					k = j;
				}
			aux = dr[i];
			dr[i] = dr[k];
			dr[k] = aux;

			auxX = adj.px[i];
			auxY = adj.py[i];
			adj.px[i] = adj.px[k];
			adj.py[i] = adj.py[k];
			
			adj.px[k] = auxX;
			adj.py[k] = auxY;
			
		}

		return (adj);
	}
	

}
