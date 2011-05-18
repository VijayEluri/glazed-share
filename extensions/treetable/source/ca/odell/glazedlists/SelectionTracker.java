package ca.odell.glazedlists;

import java.util.List;

/**
 * Improve update-selected element hack; reduce changes for TreeList corruption
 * 
 * @author kari
 * 
 * @see TreeList#setSelectionTracker(SelectionTracker)
 * @see TreeList#setAvoidRemoveInUpdate(boolean)
 */
public interface SelectionTracker {
    /**
     * @return true if current selection is non empty
     */
    boolean hasSelection();
    
    /**
     * Does tracker have currently selection in TreeList or not
     * 
     * @param pUpdated Elements which are being updated
     * 
     * @return non-null marker if some of pUpdated are currently selected
     */
    Object markSelection(List<Object> pUpdated);
    
    /**
     * Restore selection tracked by markSelection 
     */
    void restoreSelection(Object pMarker);
}
