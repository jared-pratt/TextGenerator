package comprehensive;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Class containing methods that provide functionality for the TextGenerator
 * class. Methods include ability to generate text based on input file based on
 * probability value of words or output k most probable words following the
 * given seed. Also contains comparator used to compare Word objects based on
 * probability value and break ties lexicographically.
 * 
 * Use of BufferedReader/Writer from Programiz and codegym Use of
 * StringTokenizer from Programiz
 * 
 * @author Jared Pratt and Grant Beck
 * @version April 23, 2024
 */
public class TextGeneratorUtility {

	/**
	 * Constructor for TextGeneratorUtility.
	 */
	public TextGeneratorUtility() {
	}

	/**
	 * Comparator class to compare Word objects based on probability value. Breaks
	 * ties lexicographically.
	 */
	static class wordComparator implements Comparator<Word> {
		/**
		 * Compares two Word objects.
		 * 
		 * @param word1 The first Word object to compare.
		 * @param word2 The second Word object to compare.
		 * @return An integer representing the comparison result.
		 */
		public int compare(Word word1, Word word2) {
			if (word1 == null) {
				return 1;
			}
			if (word1.getProbability() == word2.getProbability()) {
				return word1.getData().compareTo(word2.getData());
			} else if (word1.getProbability() - word2.getProbability() < 0) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	/**
	 * Generates text based on input file, seed, k value, and modifier. If the user
	 * uses the modifier "all," words will be randomized based on highest
	 * probability. If the user uses the modifer "one," the most probable word will
	 * be outputted following the seed of the file. Writes the generated text to
	 * standard output.
	 * 
	 * @param filename The name of the input file.
	 * @param seed     The seed to start text generation.
	 * @param k        The number of words to generate.
	 * @param modifier The generation modifier (all or one).
	 */
	public static void getText(String filename, String seed, int k, String modifier) {
		// Create a map of word probabilities based on the input file
		HashMap<String, SpecialHashMap> map = createMap(filename);
		// Convert seed to lowercase
		String current = seed.toLowerCase();
		StringBuilder result = new StringBuilder();
		result.append(current).append(" ");
		// Generate text based on the modifier
		if (modifier.equals("all")) {
			ArrayList<Word> words;
			for (int i = 1; i < k; i++) {
				// Retrieve word probabilities for the current word
				SpecialHashMap wordProbabilities = map.get(current);
				if (wordProbabilities != null) {
					// Generate a random number
					double rand = Math.random();
					double probabilitySum = 0.0;
					words = new ArrayList<Word>();
					// Collect all possible words and shuffle them
					for (Map.Entry<String, Word> entry : map.get(current).entrySet()) {
						words.add(entry.getValue());
					}
					Collections.shuffle(words);
					// Choose the next word based on its probability
					for (Word word : words) {
						probabilitySum += word.getProbability();
						if (rand < probabilitySum) {
							result.append(word.getData()).append(" ");
							current = word.getData();
							break;
						}
					}
				} else {
					result.append(seed).append(" ");
					current = seed;
				}
			}
		} else {
			for (int i = 1; i < k; i++) {
				// Retrieve word probabilities for the current word
				SpecialHashMap wordProbabilities = map.get(current);
				if (wordProbabilities != null) {
					// Get the most probable word
					String word = wordProbabilities.getMostProbable().getData();
					result.append(word).append(" ");
					current = word;
				} else {
					result.append(seed).append(" ");
					current = seed;
				}
			}
		}

		try (BufferedWriter out = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(FileDescriptor.out), "ASCII"), 512)) {

			// Write the generated text to standard output
			out.write(result.toString());
			out.newLine();
			out.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Creates a map of word probabilities based on the input file.
	 * 
	 * @param filename The name of the input file.
	 * @return A HashMap containing word probabilities.
	 */
	private static HashMap<String, SpecialHashMap> createMap(String filename) {
		String previous = null;
		String current = null;
		// Map to store first word frequencies and probabilities
		HashMap<String, SpecialHashMap> firstWordMap = new HashMap<>();
		StringBuilder result;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line1 = reader.readLine();
			while (line1 != null) {
				StringTokenizer line = new StringTokenizer(line1, " ");
				while (line.hasMoreTokens()) {
					// Tokenize line
					String item = line.nextToken().toLowerCase();
					// Check if the token is alphanumeric
					if (!item.equals("") && isAlphaNumeric(item.charAt(0))) {
						result = new StringBuilder();
						int index = 0;
						// Extract alphanumeric characters
						while (index < item.length() && isAlphaNumeric(item.charAt(index))) {
							result.append(item.charAt(index));
							index++;
						}
						String key = result.toString();
						if (previous == null && current == null) {
							// Set the first key if it's the first token
							current = key;
							continue;
						} else {
							previous = current;
							current = key;
							// Update word frequencies and probabilities
							if (!firstWordMap.containsKey(previous)) {
								firstWordMap.put(previous, new SpecialHashMap());
							}
							if (!firstWordMap.get(previous).containsKey(current)) {
								firstWordMap.get(previous).put(current, new Word(current));
							} else {
								firstWordMap.get(previous).get(current).updateFrequency();
							}
							firstWordMap.get(previous).updateCount();
						}
					}
				}
				line1 = reader.readLine();

			}
			reader.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		Word mostProbable;
		TextGeneratorUtility.wordComparator cmp = new TextGeneratorUtility.wordComparator();
		// Calculate and set the most probable word for each key
		for (Map.Entry<String, SpecialHashMap> entry : firstWordMap.entrySet()) {
			mostProbable = null;
			for (Map.Entry<String, Word> innerEntry : entry.getValue().entrySet()) {
				innerEntry.getValue().updateProbability(entry.getValue().getCount());
				if (cmp.compare(mostProbable, innerEntry.getValue()) > 0) {
					mostProbable = innerEntry.getValue();
				}
			}
			entry.getValue().setMostProbable(mostProbable);
		}
		return firstWordMap;
	}

	/**
	 * Generates k most probable words following the given seed. Writes the
	 * generated words to standard output.
	 * 
	 * @param filename The name of the input file.
	 * @param seed     The seed word.
	 * @param k        The number of most probable words to generate.
	 */
	public static void getKProbable(String filename, String seed, int k) {
		// Create a priority queue of words based on probabilities
		SpecialPriorityQueue queue = createQueue(filename, seed, new wordComparator());

		// Limit k to the size of the queue
		if (k > queue.size()) {
			k = queue.size();
		}

		try (BufferedWriter out = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(FileDescriptor.out), "ASCII"), 512)) {

			// Write the k most probable words to standard output
			for (int i = 0; i < k; i++) {
				out.write(queue.poll().getData());
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Creates a priority queue of words based on probabilities.
	 * 
	 * @param filename The name of the input file.
	 * @param seed     The seed word.
	 * @param cmp      The comparator used for priority ordering.
	 * @return A SpecialPriorityQueue containing words.
	 */
	private static SpecialPriorityQueue createQueue(String filename, String seed, wordComparator cmp) {
		SpecialPriorityQueue queue = new SpecialPriorityQueue(cmp);
		SpecialHashMap map = new SpecialHashMap();
		StringBuilder result;
		boolean isNextWord = false;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line1 = reader.readLine();
			while (line1 != null) {
				StringTokenizer line = new StringTokenizer(line1, " ");
				while (line.hasMoreTokens()) {
					// Tokenize line
					String item = line.nextToken().toLowerCase();
					if (isNextWord) {
						// Check if the token is alphanumeric
						if (isAlphaNumeric(item.charAt(0))) {
							result = new StringBuilder();
							int index = 0;
							// Extract alphanumeric characters
							while (index < item.length() && isAlphaNumeric(item.charAt(index))) {
								result.append(item.charAt(index));
								index++;
							}
							String key = result.toString();
							if (map.containsKey(key)) {
								map.get(key).updateFrequency();
							} else {
								map.put(key, new Word(key));
							}
							map.updateCount();
						}
					}
					if (item.equals(seed)) {
						// Set the flag to start collecting words after the seed
						isNextWord = true;
						continue;
					} else {
						isNextWord = false;
					}
				}
				line1 = reader.readLine();
			}
			reader.close();
			// Calculate and set the probability for each word
			for (Word word : map.values()) {
				word.updateProbability(map.getCount());
				queue.add(word);
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return queue;
	}

	/**
	 * Checks if a character is alphanumeric.
	 * 
	 * @param letter The character to check.
	 * @return True if the character is alphanumeric, otherwise false.
	 */
	private static boolean isAlphaNumeric(char letter) {
		return letter == 95 || Character.isLetterOrDigit(letter);
	}
}