package comprehensive;

import java.util.HashMap;

/**
 * Wrapper class that provides added functionality to java's HashMap class. This
 * includes the ability to keep track of the number of items that attempt to
 * enter the map and keeps a most probable word in a variable for easy access
 * later on.
 * 
 * @author Jared Pratt and Grant Beck
 * @version April 23, 2024
 */
public class SpecialHashMap extends HashMap<String, Word> {
    private static final long serialVersionUID = 1L;
    private int count;
    private Word mostProbable;

    /**
     * Constructs a new SpecialHashMap with default initial capacity.
     */
    public SpecialHashMap() {
        super();
        this.count = 0;
        this.mostProbable = null;
    }

    /**
     * Updates the count of items in the map.
     */
    public void updateCount() {
        this.count++;
    }

    /**
     * Gets the count of items in the map.
     * 
     * @return The count of items.
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Gets the most probable word stored in the map.
     * 
     * @return The most probable word.
     */
    public Word getMostProbable() {
        return this.mostProbable;
    }

    /**
     * Sets the most probable word in the map.
     * 
     * @param mostProbable The most probable word to set.
     */
    public void setMostProbable(Word mostProbable) {
        this.mostProbable = mostProbable;
    }
}
