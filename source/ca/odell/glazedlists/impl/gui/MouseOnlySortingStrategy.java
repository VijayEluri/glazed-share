/* Glazed Lists                                                 (c) 2003-2006 */
/* http://publicobject.com/glazedlists/                      publicobject.com,*/
/*                                                     O'Dell Engineering Ltd.*/
package ca.odell.glazedlists.impl.gui;

import java.util.Iterator;
import java.util.List;

/**
 * @see ca.odell.glazedlists.gui.AbstractTableComparatorChooser#SINGLE_COLUMN
 * @see ca.odell.glazedlists.gui.AbstractTableComparatorChooser#MULTIPLE_COLUMN_MOUSE
 *
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class MouseOnlySortingStrategy implements SortingStrategy {

    /** if false, other sorting columns will be cleared before a click takes effect */
    private final boolean multipleColumnSort;

    /**
     * Create a new {@link ca.odell.glazedlists.impl.gui.MouseOnlySortingStrategy}, sorting multiple
     * columns or not as specified.
     */
    public MouseOnlySortingStrategy(boolean multipleColumnSort) {
        this.multipleColumnSort = multipleColumnSort;
    }

    /**
     * Adjust the sorting state based on receiving the specified clicks.
     */
    public void columnClicked(
            SortingState pSortingState,
            int column,
            int clicks,
            boolean shift,
            boolean control)
    {
        boolean changed = true;
        SortingState sortingState = (SortingState)pSortingState;
        SortingState.SortingColumn clickedColumn = sortingState.getColumn(column);
        
        // Ignore fill column and such
        if (clickedColumn != null) {
            if(clickedColumn.getComparators().isEmpty()) return;
    
            List<SortingState.SortingColumn> recentlyClickedColumns = sortingState.getRecentlyClickedColumns();
    
            // on a double click, clear all click counts
            if (shift) {
                recentlyClickedColumns.remove(clickedColumn);
                changed = clickedColumn.getComparatorIndex() != -1;
                clickedColumn.clear();
            } else if (clicks == 2) {
                for(Iterator<SortingState.SortingColumn> i = recentlyClickedColumns.iterator(); i.hasNext(); ) {
                    SortingState.SortingColumn sortingColumn = i.next();
                    sortingColumn.clear();
                }
                recentlyClickedColumns.clear();
    
            // if we're only sorting one column at a time, clear other columns
            } else if(!multipleColumnSort) {
                for(Iterator<SortingState.SortingColumn> i = recentlyClickedColumns.iterator(); i.hasNext(); ) {
                    SortingState.SortingColumn sortingColumn = i.next();
                    if(sortingColumn != clickedColumn) {
                        sortingColumn.clear();
                    }
                }
                recentlyClickedColumns.clear();
            }

            if (!shift) {
                // add a click to the newly clicked column if it has any comparators
                int netClicks = 1 + clickedColumn.getComparatorIndex() * 2 + (clickedColumn.isReverse() ? 1 : 0);
                clickedColumn.setComparatorIndex((netClicks / 2) % clickedColumn.getComparators().size());
                clickedColumn.setReverse(netClicks % 2 == 1);
                if(!recentlyClickedColumns.contains(clickedColumn)) {
                    recentlyClickedColumns.add(clickedColumn);
                }
                changed = true;
            }
            
            if (changed) {
                // rebuild the sorting state
                sortingState.fireSortingChanged();
            }
        }
    }
}
