import java.util.ArrayList;
/**   
 * Class which implements calculation of heuristics   
 *
 * @author Navaneeth.Rao   
 * 
 */
public class Heuristic {
	/**
	 * Computes the variance impurity of a data set 
	 *
	 * @param data
	 *            Data for which impurity is to be computed 
	 * @return Variance impurity of the class distribution
	 */ 
	public double computeVarianceImpurity(ArrayList<ArrayList<Attribute>> data) {
		// Assuming there are two values 0,1 possible, count how many values are
		// in 0 and 1
		double[] K = new double[2];
		for (ArrayList<Attribute> attribute : data) 
			K[attribute.get(attribute.size() - 1).value]++;

		// Compute impurity (K0/K)(K1/K) as K0K1 / KK
		double impurity = 1;
		for (int i = 0; i < K.length; i++)
			impurity *= K[i];

		return impurity / (Math.pow(data.size(), K.length));
	}
	/**
	 * Computes the information gain for subset of data
	 *
	 * @param currentEntropy
	 *            Entropy/Impurity of root node
	 * @param entropiesOfSubsets
	 *            Entropy/Impurity of each subset whose parent is root node
	 * @param sizesOfSubsets
	 *            list of number of examples in each subset
	 * @param totalExamples
	 *            total number of examples for the given set
	 * @return
	 */
	public double informationGain(double currentEntropy,
			ArrayList<Double> entropiesOfSubsets,
			ArrayList<Integer> sizesOfSubsets, double totalExamples) {
		// Compute gain as root entropy - (sum of (K/N(entropy of K))
		double gain = currentEntropy;
		for (int j = 0; j < entropiesOfSubsets.size(); j++)
			gain -= (sizesOfSubsets.get(j) / totalExamples)
			* entropiesOfSubsets.get(j);
		return gain;
	}
	/**
	 * Computes the entropy of a data set.
	 *
	 * @param data
	 *            the data for which entropy is to be computed
	 * @return the entropy of the data's class distribution
	 */
	public double computeEntropy(ArrayList<ArrayList<Attribute>> data) {
		// Assuming there are two values 0,1 possible, count how many values are
		// in 0 and 1
		double[] valueCount = new double[2];
		for (ArrayList<Attribute> attribute : data)
			valueCount[attribute.get(attribute.size() - 1).value]++;
		// Calculate entropy as sum of K/N(log(K/N)) = (sum of KlogK) / N - logN
		double entropy = 0;
		for (int j = 0; j < valueCount.length; j++)
			if ((int) valueCount[j] != 0)
				entropy -= valueCount[j]
						* (Math.log(valueCount[j]) / Math.log(2));
		entropy /= data.size();
		return entropy + (Math.log(data.size()) / Math.log(2));
	}
}
