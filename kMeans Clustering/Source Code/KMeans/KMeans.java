import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException; 
import java.io.FileReader;
import java.io.FileWriter;  
import java.io.IOException;

import java.util.ArrayList; 
import java.util.Collections; 
import java.util.Iterator;
import java.util.LinkedList;

public class KMeans {
	int K;
	float distanceMatrix[][];
	ArrayList<Integer>randomNum;
    ArrayList<LinkedList<Point>>centroids;
    ArrayList<LinkedList<Point>>tempCentroids;
	
	Point[] points; 
	
	File file;
	File opFile;
	BufferedReader br;
	
	public KMeans(String k,String inputFile,String outputFile){
		// Initialize K
		K = Integer.parseInt(k);
		// File handle for input file
		file = new File(inputFile);
		// File handle for output file
		opFile = new File(outputFile);
		try {
			
			// Create bufferedReader to read input file
			br = new BufferedReader(new FileReader(file));
						
			// If output file doesnt exists, then create it
			if(!opFile.exists()){
				if(!opFile.createNewFile())System.out.println("Error creating output file");
			}			
			points = new Point[100];			
			// To get initial centroid position
			randomNum = new ArrayList<Integer>();
			
			// Initial centroids
			centroids = new ArrayList<LinkedList<Point>>();
			
			
		} catch (FileNotFoundException e) {
			System.out.println("test_data.txt file not found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public void readFile()
	{		
		String line;
		String input[];
		int i = 0;
		Point p;
		try 
		{
			
			line = br.readLine();
			line = br.readLine();
			while(line!=null){
				
				input = line.split("\\s+");				
				p = points[i];
				p = new Point();
				
				p.x = Float.parseFloat(input[1]);
				p.y = Float.parseFloat(input[2]);
				p.dist = Float.MAX_VALUE;
				p.id = i;
				p.clusterID = -1;
				
				points[i] = p;
				randomNum.add(new Integer(i));
				i++;
				line = br.readLine();
			}
		Collections.shuffle(randomNum);
		System.out.println();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public float calculateEuclidianDistance(Point p1,Point p2){
		return (float)Math.sqrt(Math.pow(p2.y-p1.y, 2)+Math.pow(p2.x-p1.x, 2));	
	}
	
	public void computeDistanceMatrix(){		
		// Create output distance matrix
		distanceMatrix = new float[K][100];
		
		for (int i = 0; i < centroids.size(); i++) {
			for (int j = 0; j < points.length; j++) {				
				distanceMatrix[i][j] = calculateEuclidianDistance(centroids.get(i).getFirst(), points[j]);
				if(distanceMatrix[i][j]<points[j].dist){
					points[j].dist = distanceMatrix[i][j];
					points[j].clusterID = i;
				}
			}
		}
	}
	
	public void formClusters(){		
		for (int i = 0; i < points.length; i++) {
			try{
			 centroids.get(points[i].clusterID).addLast(points[i]);
			}
			catch(ArrayIndexOutOfBoundsException exception){
				System.out.println("Exception at "+i);
			}
		}
	}
	

	public boolean recomputeCentroid(){		
		LinkedList<Point>list;
		Iterator<Point>ite;
		Point p;
		float X;
		float Y;
		boolean allChanged = false;
		tempCentroids = new ArrayList<LinkedList<Point>>();
		
		for (int i = 0; i < centroids.size(); i++) {
			
			list = centroids.get(i);
			ite = list.iterator();			
			X = 0;
			Y = 0;
			ite.next();
			while(ite.hasNext()){
				
				p = ite.next();
				
				// Reset point values(dist,clusterId) so that it can be used for calculating distance matrix in next step
				p.dist = Float.MAX_VALUE;
				
				X+=p.x;
				Y+=p.y;
			}
			X = X/(list.size()-1);
			Y = Y/(list.size()-1);
			
			//0.00000001
			if(Math.abs(X-list.getFirst().x)<0.000001
					&& Math.abs(Y-list.getFirst().y)<0.000001){
			}
			else{
				allChanged = true;
			}
						
		   Point cp = new Point();
		   cp.x = X;
		   cp.y = Y;
		   cp.dist = Float.MAX_VALUE;
		   cp.clusterID = list.getFirst().clusterID;
		   LinkedList<Point> newList = new LinkedList<Point>();
		   newList.add(cp);
			tempCentroids.add(i, newList);
			
		}
		return allChanged;
	}
		
	public void setInitialCentroids(){
		
		LinkedList<Point>list;
		
		for (int i = 0; i < K; i++) {
			
			list = new LinkedList<Point>();
			list.add(points[randomNum.get(i)]);
			centroids.add(i, list);
		}
		
	}
	
	public void writeClusters(){		
		LinkedList<Point>list;
		try {
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(opFile));
			
			for (int i = 0; i < centroids.size(); i++) {
				
				list = centroids.get(i);
				Iterator<Point>ite = list.iterator();
				Point p;
				ite.next();
				bw.write((i+1)+"    ");
				
				while(ite.hasNext()){
					p = ite.next();
					bw.write(p.id+",");
				}
				bw.write("\n");
			}
			bw.flush();
			bw.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void computeSSE(){
		
		double dist;
		double kSum = 0;
		LinkedList<Point>list;
		Iterator<Point>ite;
	    Point centroid;
		
		for (int i = 0; i < centroids.size(); i++) {
			
			list = centroids.get(i);
			centroid = list.getFirst();
			ite = list.iterator();
			ite.next();
			
			while(ite.hasNext()){
				
				dist = calculateEuclidianDistance(ite.next(), centroid);
				dist = Math.pow(dist, 2);
				kSum+=dist;
			}
		}
		
		System.out.println("SSE value with "+K+" clusters = "+kSum);
	}
	
	public void runKMeans(){
		
		boolean doRepeat=true;

		readFile();
		setInitialCentroids();
		int ITER = 0;
		while(doRepeat && ITER<=25){
		
			
			computeDistanceMatrix();
			formClusters();
			doRepeat = recomputeCentroid();
			
			if(doRepeat){
				for (int j = 0; j < centroids.size(); j++) {
					centroids.set(j,tempCentroids.get(j));
				}
				ITER++;
			}
			
		}
		
		writeClusters();
		computeSSE();
		
	}
	
	public static void main(String[] args) {
		
		KMeans kMeans = new KMeans(args[0],args[1],args[2]);
		kMeans.runKMeans();
		
	}
	
}
