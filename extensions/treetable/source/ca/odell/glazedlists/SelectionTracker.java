package ca.odell.glazedlists;

/**
 * Improve update-selected element hack; reduce changes for TreeList corruption
 * 
 * @author kari
 */
public interface SelectionTracker {
    /**
     * Does tracker have currently selection in TreeList or not
     * 
     * @return non-null marker if pElem is currently selected
     */
    Object markSelection(Object pElem);
    
    /**
     * Restore selection tracked by markSelection 
     */
    void restoreSelection(Object pMarker);
}
