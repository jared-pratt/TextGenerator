package comprehensive;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Wrapper class for java's PriorityQueue, adding ability to keep track
 * of number of words that have attempted to enter the queue.
 * 
 * @author Jared Pratt and Grant Beck
 * @version April 23, 2024
 */
public class SpecialPriorityQueue extends PriorityQueue<Word> {
	private static final long serialVersionUID = 1L;
	public int count;

	public SpecialPriorityQueue(Comparator<? super Word> cmp) {
		super(cmp);
		count = 0;
	}

	public Word poll() {
		return super.poll();
	}
}