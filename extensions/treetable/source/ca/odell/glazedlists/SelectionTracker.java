package ca.odell.glazedlists;

/**
 * Improve update-selected element hack; reduce changes for TreeList corruption
 * 
 * @author kari
 */
public interface SelectionTracker {
    /**
     * Does tracker have currently selection in TreeList or not
     */
    boolean hasSelection();
}
