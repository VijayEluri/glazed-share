package ca.odell.glazedlists.gui;


/**
 * Table format for sorting
 */
public interface SortableFormat extends AdvancedTableFormat<Object> {
    /**
     * Get all model indexes supported by the model
     *
     */
    int[] getModelIndexes();
}
