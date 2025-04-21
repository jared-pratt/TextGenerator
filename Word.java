package comprehensive;

/**
 * Represents a string from the input file that can be added to a HashMap. Keeps
 * track of the frequency and probobility for every word in the HashMap.
 * 
 * @author Jared Pratt and Grant Beck
 * @version April 23, 2024
 */
public class Word {
	private String data;
	private double frequency;
	private double probability;

	/**
	 * Constructor for Word.
	 * 
	 * @param data The word data.
	 */
	public Word(String data) {
		this.data = data;
		this.frequency = 1;
		this.probability = 0.0;
	}

	/**
	 * Gets the word data.
	 * 
	 * @return The word data.
	 */
	public String getData() {
		return data;
	}

	/**
	 * Gets the probability of the word.
	 * 
	 * @return The probability of the word.
	 */
	public double getProbability() {
		return this.probability;
	}

	/**
	 * Updates the probability of the word based on the total count.
	 * 
	 * @param count The total count of occurrences.
	 */
	public void updateProbability(int count) {
		this.probability = frequency / count;
	}

	/**
	 * Gets the frequency of the word.
	 * 
	 * @return The frequency of the word.
	 */
	public double getFrequency() {
		return frequency;
	}

	/**
	 * Updates the frequency of the word and returns the updated frequency.
	 * 
	 * @return The updated frequency of the word.
	 */
	public double updateFrequency() {
		return frequency++;
	}
}