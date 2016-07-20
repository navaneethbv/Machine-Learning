import java.util.ArrayList;
import java.util.Random;    
/**
 * Class to implement a custom decision tree  
 *  
 * @author Navaneeth.Rao 
 * 
 */ 
public class DecisionTree {
	/** The tree's root node. */
	public Node root;
	/**
	 * Variable to number the nodes
	 */
	private int index; 
	/**
	 * Traverses along a decision tree till a leaf node is reached to check if
	 * the value of class attribute in data set matches with decision tree
	 *
	 * @param data
	 *            Data instance
	 * @param root
	 *            Root node of the decision tree
	 * @return True if class value matches leaf node value, false otherwise
	 */
	private boolean treeMatchesClassValue(ArrayList<Attribute> data, Node root) {
		// If we reached the leaf node, just compare the values in data to
		// decision tree
		if (root.children == null)
			return data.get(data.size() - 1).value == root.value;
		else {
			// Else, Compute the value of attribute in the data set
			int value = -1;
			for (Attribute attribute : data)
				if (attribute.name.equals(root.ruleAttribute.name)) {
					value = attribute.value;
					break;
				}
			// Call recursively the child node which the value of attribute in
			// root corresponds to
			return treeMatchesClassValue(data, root.children[value]);
		}
	}
	/**
	 * Computes the accuracy of a decision tree
	 *
	 * @param data
	 *            The test data
	 * @param root
	 *            Root node of the decision tree
	 * @return Accuracy percentage as a decimal
	 */
	public double getAccuracy(ArrayList<ArrayList<Attribute>> data, Node root) {
		double accuracy = 0.0;

		// For each data set, check if result is correct or not
		for (ArrayList<Attribute> arrayList : data)
			if (treeMatchesClassValue(arrayList, root))
				accuracy++;
		// Return the computed accuracy
		return (accuracy * 100 / data.size());
	}
	/**
	 * Method building decision tree using entropy/impurity as a heuristic
	 *
	 * @param data
	 *            the training data
	 * @param root
	 *            Starting node of the tree to be constructed
	 * @param heuristic
	 *            entropy/impurity
	 */
	@SuppressWarnings("unchecked")
	public void buildTree(ArrayList<ArrayList<Attribute>> data, Node root,
			String heuristic) {
		// Initialize variables
		Heuristic h = new Heuristic();
		ArrayList<ArrayList<Attribute>> left = new ArrayList<>();
		ArrayList<ArrayList<Attribute>> right = new ArrayList<>();
		int index = -1;
		double maxGain = 0.0;
		Attribute newRule = null;
		// Compute root`s heuristic
		if (heuristic.equalsIgnoreCase("entropy"))
			root.entropy = h.computeEntropy(data);
		else
			root.entropy = h.computeVarianceImpurity(data);
		// Split data in class of 0 as left tree and 1 as right tree
		for (int i = 0; i < data.get(0).size() - 1; i++) {
			ArrayList<ArrayList<Attribute>> leftSubset = new ArrayList<>();
			ArrayList<ArrayList<Attribute>> rightSubset = new ArrayList<>();
			// Calculate subtree split on this attribute
			for (int j = 0; j < data.size(); j++) {
				ArrayList<Attribute> list = new ArrayList<>();
				list.addAll(data.get(j));
				if (data.get(j).get(i).value == 0)
					leftSubset.add(list);
				else
					rightSubset.add(list);
			}
			// Calculate sub entropies
			ArrayList<Double> subEntropies = new ArrayList<>();
			if (heuristic.equalsIgnoreCase("entropy")) {
				subEntropies.add(h.computeEntropy(leftSubset));
				subEntropies.add(h.computeEntropy(rightSubset));
			} else {
				subEntropies.add(h.computeVarianceImpurity(leftSubset));
				subEntropies.add(h.computeVarianceImpurity(rightSubset));
			}
			// Calculate size
			ArrayList<Integer> sizesOfSubsets = new ArrayList<>();
			sizesOfSubsets.add(leftSubset.size());
			sizesOfSubsets.add(rightSubset.size());
			// Compute attribute with maximum information gain
			double gain = h.informationGain(root.entropy, subEntropies,
					sizesOfSubsets, data.size());
			if ((int) (gain * 100000000) > (int) (maxGain * 100000000)) {
				maxGain = gain;
				newRule = data.get(0).get(i);
				index = i;
				left = (ArrayList<ArrayList<Attribute>>) leftSubset.clone();
				right = (ArrayList<ArrayList<Attribute>>) rightSubset.clone();
			}
		}
		// If index was set, means we have an attribute on which data can be
		// split
		if (index > -1) {
			// Remove the attribute used from further consideration
			for (ArrayList<Attribute> attributes : left)
				attributes.remove(index);
			for (ArrayList<Attribute> attributes : right)
				attributes.remove(index);

			// Set the nodes for the recursive call to the subtree
			Node leftChild = new Node();
			Node rightChild = new Node();
			leftChild.data = left;
			rightChild.data = right;
			root.children = new Node[2];
			root.children[0] = leftChild;
			root.children[1] = rightChild;
			root.ruleAttribute = newRule;
			root.index = ++this.index;
			// Recursively call subtrees
			buildTree(left, leftChild, heuristic);
			buildTree(right, rightChild, heuristic);
		} else {
			// Else, no more splitting is possible for this subtree
			root.value = data.get(0).get(data.get(0).size() - 1).value;
			return;
		}
		this.root = root;
	}
	/**
	 * Prunes a tree for better accuracy
	 *
	 * @param L
	 *            Bound L on post pruning algorithm
	 * @param K
	 *            Bound K on post pruning algorithm
	 * @param data
	 *            The validation data
	 * @throws CloneNotSupportedException
	 */
	public void postPrune(int L, int K, ArrayList<ArrayList<Attribute>> data)
			throws CloneNotSupportedException {
		// Assign the tree as best
		Node best = (Node) root.clone();
		for (int i = 0; i < L; i++) {
			// Copy root to D
			Node D = (Node) root.clone();
			// Generate a random number between 1 and K
			Random random = new Random();
			int M = random.nextInt(K);
			for (int j = 0; j < M; j++) {
				// Generate a random number between 1 and index
				int P = random.nextInt(index);
				// Find subtree rooted at P and remove it
				findAndRemoveNode(D, P);
			}
			// Set D to best if its accuracy is better
			if (getAccuracy(data, D) > getAccuracy(data, best))
				best = (Node) D.clone();
		}
		// Set root as the best tree formed
		root = (Node) best.clone();
	}
	/**
	 * Finds and remove subtree rooted at index
	 *
	 * @param root
	 *            Root of the subtree being processed
	 * @param index
	 *            Index to be removed
	 */
	private void findAndRemoveNode(Node root, int index) {
		// If we found the root with the index
		if (root.index == index) {
			// Decide which is the majority class, and make it a leaf node
			if (root.children[0].data.size() > root.children[1].data.size()) {
				int[] valuecount = new int[2];
				for (ArrayList<Attribute> attribute : root.children[0].data)
					valuecount[attribute.get(attribute.size() - 1).value]++;

				root.children[0].value = valuecount[0] > valuecount[1] ? 0 : 1;
				root.children[0].children = null;
				root.children[0].index = -1;
			} else {
				int[] valuecount = new int[2];
				for (ArrayList<Attribute> attribute : root.children[1].data)
					valuecount[attribute.get(attribute.size() - 1).value]++;

				root.children[1].value = valuecount[0] > valuecount[1] ? 0 : 1;
				root.children[1].children = null;
				root.children[1].index = -1;
			}
		} else if (root.children != null) {
			// Else, if the children are present, recursively explore the
			// subtrees in them
			findAndRemoveNode(root.children[0], index);
			findAndRemoveNode(root.children[1], index);
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return print(this.root, 0);
	}
	/**
	 * Calculates string representation a decision tree
	 *
	 * @param root
	 *            Root node of the subtree
	 * @param level
	 *            Level of the node
	 * @return String representation of the tree
	 */
	private String print(Node root, int level) {
		// Initialize string builder and append tabs according to the level of
		// the node
		StringBuilder stringBuilder = new StringBuilder();
		// Do for all subtrees
		for (int j = 0; j < root.children.length; j++) {
			// Append tabs for indentation according to level
			for (int i = 0; i < level; i++)
				stringBuilder.append("| ");

			// Print subtree if present, or just class attribute value as the
			// leaf node
			stringBuilder.append(root.ruleAttribute.name + " = " + j + " :");
			if (root.children[j].children != null)
				stringBuilder.append("\n" + print(root.children[j], level + 1));
			else
				stringBuilder.append(" " + root.children[j].value + "\n");
		}
		return stringBuilder.toString();
	}
}
