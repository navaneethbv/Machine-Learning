import java.io.BufferedReader;
import java.io.BufferedWriter; 
import java.io.File;
import java.io.FileNotFoundException; 
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator; 
import java.util.LinkedList;
import java.util.Set;

public class TweetClustering 
{	
	int K;
	float distanceMatrix[][];
	File seedsFile;
	File tweetDataFile;
	File opFile;
	HashMap<String, TweetNode>map;
	ArrayList<TweetNode>tweetNodes;
	ArrayList<LinkedList<TweetNode>>centroids;
    ArrayList<LinkedList<TweetNode>>tempCentroids;	
	private class IndexNode{
		int fromIndex;
		int toIndex;
	}		
	public TweetClustering(int numClusters,String seedFile,String tweetFile,String opDatFile){		
		seedsFile = new File(seedFile);
		tweetDataFile = new File(tweetFile);
		opFile = new File(opDatFile);
		K = numClusters;		
	}
	
	public IndexNode getIndex(String text,String line){		
		IndexNode index = new IndexNode();
		int to;
		int from  = line.indexOf(text);			
		switch(text){		
		case "\"text\":":from+=text.length()+2;
			             to = line.indexOf("\"profile_image_url\":")-3; break;
		case "\"profile_image_url\":":from+=text.length()+2;
			                          to = line.indexOf("\"from_user\":")-3;
			break;
		case "\"from_user\":":from+=text.length()+2;
			                  to = line.indexOf("\"from_user_id\":")-3;
			break;
		case "\"from_user_id\":":from+=text.length()+1;		
			                     to = line.indexOf("\"geo\":")-2;
			break;
		case "\"geo\":":from+=text.length()+1;
			            to = line.indexOf("\"id\":")-2;
			break;
		case "\"id\":":from+=text.length()+1;
			           to = line.indexOf("\"iso_language_code\":")-2;
			break;
		case "\"iso_language_code\":":from+=text.length()+2;
			                          to = line.indexOf("\"from_user_id_str\":")-3;
			break;
		case "\"from_user_id_str\":":from+=text.length()+2;
									 to = line.indexOf("\"created_at\":")-3;
			break;
		case "\"created_at\":":from+=text.length()+2;
							   to = line.indexOf("\"source\":")-3;
			break;
		case "\"source\":":from+=text.length()+2;
						   to = line.indexOf("\"id_str\":")-3;
			break;
		case "\"id_str\":":from+=text.length()+2;
						   to = line.indexOf("\"from_user_name\":")-3;
			break;
		case "\"from_user_name\":":from+=text.length()+2;
			                       to = line.indexOf("\"profile_image_url_https\":")-3;
			break;
		case "\"profile_image_url_https\":":from+=text.length()+2;
											to = line.indexOf("\"metadata\":")-3;
			break;
		case "\"metadata\":":from+=text.length()+2;
			                 to = line.length()-2;
			break;
			default:from=-1;
			        to=-1;
		}
		
		index.fromIndex = from;
		index.toIndex = to;
		return index;
	}
	
public void parseLine(String line){		
		TweetNode tweetNode = new TweetNode();
		IndexNode index;
		index = getIndex("\"text\":",line);
		tweetNode.text = line.substring(index.fromIndex, index.toIndex);
		
		index = getIndex("\"profile_image_url\":",line);
		tweetNode.profile_image_url = line.substring(index.fromIndex, index.toIndex);
		
		index = getIndex("\"from_user\":",line);
		tweetNode.from_user = line.substring(index.fromIndex, index.toIndex);
		
		index = getIndex("\"from_user_id\":",line);
		tweetNode.from_user_id = line.substring(index.fromIndex, index.toIndex);
		
		index = getIndex("\"geo\":",line);
		tweetNode.geo = line.substring(index.fromIndex, index.toIndex);
		
		index = getIndex("\"id\":",line);
		tweetNode.id = line.substring(index.fromIndex, index.toIndex);
		
		index = getIndex("\"iso_language_code\":",line);
		tweetNode.iso_language_code = line.substring(index.fromIndex, index.toIndex);
		
		index = getIndex("\"from_user_id_str\":",line);
		tweetNode.from_user_id_str = line.substring(index.fromIndex, index.toIndex);
		
		index = getIndex("\"created_at\":",line);
		tweetNode.created_at = line.substring(index.fromIndex, index.toIndex);
		
		index = getIndex("\"source\":",line);
		tweetNode.source = line.substring(index.fromIndex, index.toIndex);
		
		index = getIndex("\"id_str\":",line);
		tweetNode.id_str = line.substring(index.fromIndex, index.toIndex);
		
		index = getIndex("\"from_user_name\":",line);
		tweetNode.from_user_name = line.substring(index.fromIndex, index.toIndex);
		
		index = getIndex("\"profile_image_url_https\":",line);
		tweetNode.profile_image_url_https = line.substring(index.fromIndex, index.toIndex);
		
		index = getIndex("\"metadata\":",line);
		tweetNode.metadata = new String[2];
		String input[] = line.substring(index.fromIndex, index.toIndex).split(":");
		tweetNode.metadata[0] = input[0];
		tweetNode.metadata[1] = input[1];		
		tweetNodes.add(tweetNode);
		map.put(tweetNode.id, tweetNode);
}
	
	public void parseTweetData(){	
		try {			
			map = new HashMap<String, TweetNode>();
			tweetNodes = new ArrayList<TweetNode>();
			BufferedReader br = new BufferedReader(new FileReader(tweetDataFile));			
			String line;
			line = br.readLine();
			
			while(line!=null){
				parseLine(line);
				line = br.readLine();
			}
			br.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		
	}
	
	public void setInitialCentroids(){
		
		centroids = new ArrayList<LinkedList<TweetNode>>();
		TweetNode centroid;
		LinkedList<TweetNode>list;
		
		try {
			
			int ITER = 0;
			String line;
			BufferedReader br = new BufferedReader(new FileReader(seedsFile));			
			line = br.readLine();
			while(line!=null){
				
				if(line.contains(",")){
					line = line.substring(0, line.length()-1);
				}
				
				centroid = map.get(line);
				list = new LinkedList<TweetNode>();
				//list.add(ITER, centroid);
				list.add(centroid);
				centroids.add(ITER, list);
				ITER++;
				
				line = br.readLine();
			}
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
public float calculateDistance(TweetNode node1,TweetNode node2){
		
		String filter1,filter2;
		
		filter1 = node1.text;
		filter2 = node2.text;
		
		String tweet1[] = filter1.split("\\s+");
		String tweet2[] = filter2.split("\\s+");
		

		Set<String> A = new HashSet<String>();
		for(String s1:tweet1){
			A.add(s1);
		}
		Set<String>t1 = new HashSet<String>(A);		
		Set<String> B = new HashSet<String>();
		for(String s2: tweet2){
			B.add(s2);
		}
		
		A.addAll(B); // Union
		B.retainAll(t1); // Intersection
		
		float sim = (float)B.size()/A.size();
		float dist = 1-sim;
		return dist;
	}
	
	
	
	public void computeJaccardDistance(){
		
		distanceMatrix = new float[K][tweetNodes.size()];
		TweetNode tweet,centroid;
		
		for (int i = 0; i < centroids.size(); i++) {
			
			centroid = centroids.get(i).getFirst();

			for (int j = 0; j < tweetNodes.size(); j++) {
				
				tweet = tweetNodes.get(j);
				
				distanceMatrix[i][j] = calculateDistance(centroid,tweet);
				
				if(distanceMatrix[i][j]<tweet.dist){					
					tweet.dist = distanceMatrix[i][j];
					tweet.clusterID = i;
				}
			}
		}
		
	}

	
	public boolean recomputeCentroid(){
		
		boolean allChanged = false;
		int size;
		int minI;
		//float dist[][];
		float sum,square;
		float min;
		
		LinkedList<TweetNode>list;
		TweetNode newTweet,centroid,tweet1,tweet2;
		
		tempCentroids = new ArrayList<LinkedList<TweetNode>>();		
		for (int i = 0; i < centroids.size(); i++) {
			
			list = centroids.get(i);

			centroid = list.getFirst();
			size = list.size()-1;
			//dist = new float[size][size];
			
			minI = -1;
			min = Float.MAX_VALUE;

			for (int j = 1; j < size; j++) {
				
				sum = 0;
				tweet1 = list.get(j);
				tweet1.dist = Float.MAX_VALUE;
				for (int k = 1; k < size; k++) {
					sum+=calculateDistance(tweet1, list.get(k));
					//square = (float)Math.pow(square, 2);
					//sum+=square;
				}
				sum = (float)sum/size;
				if(sum<min){
					min = sum;
					minI = j;
				}
			}
			
			if(minI== -1){
				newTweet = centroid;
			}
			else{
				newTweet = list.get(minI);
			}
			
			if(centroid.id.equals(newTweet.id)){
				
			}
			else{
				allChanged = true;
			}			
			TweetNode node = new TweetNode();
			node = newTweet;
			LinkedList<TweetNode>newList = new LinkedList<TweetNode>();
			newList.add(node);
			tempCentroids.add(i, newList);
		}
		
		return allChanged;
	}
	
	public void formClusters(){
		
		for (int i = 0; i < tweetNodes.size(); i++) {
			centroids.get(tweetNodes.get(i).clusterID).addLast(tweetNodes.get(i));
		}
	}
	
public void writeClusters(){
		
		LinkedList<TweetNode>list;
		try {			
			BufferedWriter bw = new BufferedWriter(new FileWriter(opFile));
			
			for (int i = 0; i < centroids.size(); i++) {				
				list = centroids.get(i);
				Iterator<TweetNode>ite = list.iterator();
				TweetNode p;
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
	
	float dist;
	float kSum = 0;
	LinkedList<TweetNode>list;
	Iterator<TweetNode>ite;
    TweetNode centroid;
	
	for (int i = 0; i < centroids.size(); i++) {
		
		list = centroids.get(i);
		centroid = list.getFirst();
		ite = list.iterator();
		ite.next();
		
		while(ite.hasNext()){
			
			dist = calculateDistance(ite.next(), centroid);
			dist = (float)Math.pow(dist, 2);
			kSum+=dist;
		}
	}
	System.out.println("SSE value with "+K+" clusters = "+kSum);
}


	public void clusterTweets(){
		
		boolean doRepeat=true;
		parseTweetData();
    	setInitialCentroids();    	
    	int ITER = 0;
		while(doRepeat && ITER<=25){
		
			computeJaccardDistance();
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
		
    	if(args.length!=4){
    		System.out.println("Usage Kmeans <numberOfClusters>	<initialSeedsFile> <TweetsDataFile>	<outputFile>");
    		return;
    	}    	
    	TweetClustering tweetObj = new TweetClustering(Integer.parseInt(args[0]), args[1], args[2], args[3]);
    	tweetObj.clusterTweets();
	}
}
