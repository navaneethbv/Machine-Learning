import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/**
 * Class to implement the program of inducing decision trees  
 *
 * @author Navaneeth.Rao
 *
 */
public class Solution {
	/** 
	 * Parses a CSV file to read a data set
	 *
	 * @param fileLocation
	 *            Location of the file to be parsed
	 * @return List of attribute name, value pairs
	 */
	@SuppressWarnings("finally")
	private static ArrayList<ArrayList<Attribute>> parseCSV(String fileLocation) {
		BufferedReader br = null;
		String line = "";
		ArrayList<ArrayList<Attribute>> attributes = new ArrayList<>();
		try {
			// Read the first line as header and parse the attribute names from
			// it
			br = new BufferedReader(new FileReader(fileLocation));
			String[] header = br.readLine().split(",");
			// Read each line of the file
			while ((line = br.readLine()) != null) {
				// Use comma as separator
				String[] values = line.split(",");
				// Parse each value, assign it to the specified attribute and
				// add the attribute to the list
				ArrayList<Attribute> row = new ArrayList<>();
				for (int i = 0; i < values.length; i++)
					row.add(new Attribute(header[i], Integer
							.parseInt(values[i])));
				attributes.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Finally, try to close reader
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return attributes;
		}
	}
	/**
	 * Execution starts here
	 *
	 * @param args
	 *            Command line arguments
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Parse arguments
		int L = Integer.parseInt(args[0]);
		int K = Integer.parseInt(args[1]);
		ArrayList<ArrayList<Attribute>> trainingSet = parseCSV(args[2]);
		ArrayList<ArrayList<Attribute>> validationSet = parseCSV(args[3]);
		ArrayList<ArrayList<Attribute>> testSet = parseCSV(args[4]);
		boolean toPrint = args[5].equalsIgnoreCase("yes");
		// Build tree using information gain and print its accuracy over test
		// set
		DecisionTree tree = new DecisionTree();
		tree.buildTree(trainingSet, new Node(), "entropy");
		System.out
		.println("Accuracy of decision tree constructed using information gain before pruning: "
				+ tree.getAccuracy(testSet, tree.root) + "%\n");
		// If to-Print is yes, print the decision tree
		if (toPrint) {
			System.out.println("Decision tree before pruning: ");
			System.out.println(tree);
		}
		// Post prune the tree and print its accuracy over test set
		tree.postPrune(L, K, validationSet);
		System.out
		.println("Accuracy of decision tree constructed using information gain after pruning: "
				+ tree.getAccuracy(testSet, tree.root) + "%\n");
		// If to-Print is yes, print the decision tree
		if (toPrint) {
			System.out.println("Decision tree after pruning: ");
			System.out.println(tree);
		}
		// Build tree using impurity gain and print its accuracy over test
		// set
		tree = new DecisionTree();
		tree.buildTree(trainingSet, new Node(), "impurity");
		System.out
		.println("Accuracy of decision tree constructed using impurity gain before pruning: "
				+ tree.getAccuracy(testSet, tree.root) + "%\n");
		// If to-Print is yes, print the decision tree
		if (toPrint) {
			System.out.println("Decision tree before pruning: ");
			System.out.println(tree);
		}
		// Post prune the tree and print its accuracy over test set
		tree.postPrune(L, K, validationSet);
		System.out
		.println("Accuracy of decision tree constructed using impurity gain after pruning: "
				+ tree.getAccuracy(testSet, tree.root) + "%\n");
		// If to-Print is yes, print the decision tree
		if (toPrint) {
			System.out.println("Decision tree after pruning: ");
			System.out.println(tree);
		}
	}
}
