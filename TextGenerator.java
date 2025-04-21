package comprehensive;

/**
 * Class with main method that takes in command line arguments and executes
 * methods from TextGeneratorUtility class to generate text based on input file
 * or return k most probable words after seed.
 * 
 * User provides file name and path as first argument, seed word in second
 * argument, number of words desired in the generation k as third, and an
 * optional fourth argument to generate based on a deterministic path (using
 * only the most probable word after) or a random generation, drawing from all
 * possible words.
 * 
 * @author Jared Pratt and Grant Beck
 * @version April 23, 2024
 */
public class TextGenerator {
	public static void main(String[] args) {
		if (args.length == 4) {
			TextGeneratorUtility.getText(args[0], args[1], Integer.parseInt(args[2]), args[3]);
		} else if (args.length == 3) {
			TextGeneratorUtility.getKProbable(args[0], args[1], Integer.parseInt(args[2]));
		}
	}
}
