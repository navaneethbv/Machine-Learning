/**
 * Class to represent an attribute 
 *
 * @author Navaneeth.Rao
 *
 */ 
public class Attribute {
	/**
	 * Name of this attribute 
	 */
	public String name;
	/**
	 * Value of this attribute in the corresponding data instance
	 */
	public int value;
	/**
	 * Constructor
	 * 
	 * @param name
	 *            Name to set
	 * @param value
	 *            Value to set
	 */
	public Attribute(String name, int value) {
		this.name = name;
		this.value = value;
	}
}
