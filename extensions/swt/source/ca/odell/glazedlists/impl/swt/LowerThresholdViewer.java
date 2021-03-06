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
 * A Viewer class that binds a BoundedRangeControl to the lower threshold on a
 * ThresholdList.
 *
 * @author <a href="mailto:kevin@swank.ca">Kevin Maltby</a>
 */
public class LowerThresholdViewer implements SelectionListener {

    /** the ThresholdList that is the target for changes */
    private ThresholdList target = null;

    /** the BoundedRangeControl that manipulates the lower threshold on the target list */
    private BoundedRangeControl control = null;

    /** a cache of the maximum value which will likely not change much */
    private int maximum = -1;

    /**
     * Creates a Viewer that binds a BoundedRangeControl to the lower
     * threshold on a ThresholdList.
     */
    public LowerThresholdViewer(ThresholdList target, BoundedRangeControl control) {
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
            target.setLowerThreshold(control.getSelection());
            if(maximum != control.getMaximum()) {
                maximum = control.getMaximum();
                target.setUpperThreshold(maximum);
            }
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