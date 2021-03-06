package ca.odell.glazedlists.swing;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.GlazedLists;
import junit.framework.TestCase;

import javax.swing.*;

public class AutoCompleteSupportThreadingTest extends TestCase {

    public void testNonEDTAccess() throws Exception {
        try {
            AutoCompleteSupport.install(new JComboBox(), new BasicEventList<Object>());
            fail("failed to receive IllegalStateException installing AutoCompleteSupport from non-EDT");
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            AutoCompleteSupport.install(new JComboBox(), new BasicEventList<Object>(), GlazedLists.toStringTextFilterator());
            fail("failed to receive IllegalStateException installing AutoCompleteSupport from non-EDT");
        } catch (IllegalStateException e) {
            // expected
        }

        final InstallAutoCompleteSupportRunnable installAutoCompleteSupportRunnable = new InstallAutoCompleteSupportRunnable();
        SwingUtilities.invokeAndWait(installAutoCompleteSupportRunnable);

        final AutoCompleteSupport support = installAutoCompleteSupportRunnable.getSupport();

        try {
            support.setStrict(true);
            fail("failed to receive IllegalStateException mutating AutoCompleteSupport from non-EDT");
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            support.setCorrectsCase(true);
            fail("failed to receive IllegalStateException mutating AutoCompleteSupport from non-EDT");
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            support.uninstall();
            fail("failed to receive IllegalStateException uninstalling AutoCompleteSupport from non-EDT");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    private static final class InstallAutoCompleteSupportRunnable implements Runnable {
        private AutoCompleteSupport support;

        public void run() {
            support = AutoCompleteSupport.install(new JComboBox(), new BasicEventList<Object>());
        }
        public AutoCompleteSupport getSupport() {
            return support;
        }
    }
}