/* Glazed Lists                                                 (c) 2003-2006 */
/* http://publicobject.com/glazedlists/                      publicboject.com,*/
/*                                                     O'Dell Engineering Ltd.*/
package ca.odell.glazedlists.impl.swt;

// to interact with Sliders
import ca.odell.glazedlists.ThresholdList;
import ca.odell.glazedlists.util.concurrent.Lock;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * A Viewer class that binds a BoundedRangeControl to the upper threshold on a
 * ThresholdList.
 *
 * @author <a href="mailto:kevin@swank.ca">Kevin Maltby</a>
 */
public class UpperThresholdViewer implements SelectionListener {

    /** the ThresholdList that is the target for changes */
    private ThresholdList target = null;

    /** the BoundedRangeControl that manipulates the lower threshold on the target list */
    private BoundedRangeControl control = null;

    /** a cache of the minimum value which will likely not change much */
    private int minimum = -1;

    /**
     * Creates a Viewer that binds a BoundedRangeControl to the upper
     * threshold on a ThresholdList.
     */
    public UpperThresholdViewer(ThresholdList target, BoundedRangeControl control) {
        this.target = target;
        this.control = control;
        widgetSelected(null);
        control.addSelectionListener(this);
    }

    /**
     * Allows this Viewer to respond to changes to the BoundedRangeControl
     */
    public void widgetSelected(SelectionEvent e) {
        final Lock writeLock = target.getReadWriteLock().writeLock();
        writeLock.lock();
        try {
            if(minimum != control.getMinimum()) {
                minimum = control.getMinimum();
                target.setLowerThreshold(minimum);
            }
            target.setUpperThreshold(control.getSelection());
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * No-op on a Slider, but the SWT documentation excludes information
     * on whether or not it is called for Scale.
     */
    public void widgetDefaultSelected(SelectionEvent e) {
        widgetSelected(e);
    }
}