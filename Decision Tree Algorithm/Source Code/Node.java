import java.util.ArrayList;
/**
 * Class to represent a node in the decision tree
 *  
 * @author Navaneeth.Rao 
 *
 */ 
public class Node implements Cloneable {
	/** 
	 * Children of this node  
	 */ 
	public Node[] children;
	/**
	 * Entropy/Impurity at this node
	 */
	public double entropy;
	/**
	 * Value of Class attribute at this node, if this is a leaf node
	 */
	public int value;
	/**
	 * The attribute which defined the rule to split data at this node
	 */
	public Attribute ruleAttribute;
	/**
	 * Data set to be considered at this subtree
	 */
	public ArrayList<ArrayList<Attribute>> data;
	/**
	 * Index to node in the tree (used when pruning)
	 */
	public int index;
	/**
	 * Constructor
	 */
	public Node() {
		children = null;
		entropy = 0.0;
		value = 0;
		ruleAttribute = new Attribute("", 0);
		data = new ArrayList<>();
		index = -1;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		Node clonedNode = new Node();
		clonedNode.index = this.index;
		clonedNode.value = this.value;
		clonedNode.ruleAttribute = new Attribute(this.ruleAttribute.name,
				this.ruleAttribute.value);
		clonedNode.entropy = this.entropy;
		// Copy the data
		for (ArrayList<Attribute> attributes : this.data) {
			ArrayList<Attribute> clonedAttributes = new ArrayList<>();
			for (Attribute attribute : attributes)
				clonedAttributes.add(new Attribute(attribute.name,
						attribute.value));
			clonedNode.data.add(clonedAttributes);
		}
		// Recursively clone the children
		if (this.children != null) {
			clonedNode.children = new Node[2];
			for (int i = 0; i < this.children.length; i++)
				clonedNode.children[i] = (Node) this.children[i].clone();
		}
		// Return the cloned node
		return clonedNode;
	}
}
