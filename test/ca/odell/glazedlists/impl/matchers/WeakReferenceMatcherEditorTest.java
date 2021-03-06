package ca.odell.glazedlists.impl.matchers;

import java.lang.ref.WeakReference;

import junit.framework.TestCase;

import ca.odell.glazedlists.matchers.CountingMatcherEditorListener;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.matchers.Matchers;
import ca.odell.glazedlists.matchers.TextMatcherEditor;

public class WeakReferenceMatcherEditorTest extends TestCase {

    public void testAddRemoveListener() {
        final TextMatcherEditor<String> textMatcherEditor = new TextMatcherEditor<String>();
        MatcherEditor<String> weakMatcherEditor = Matchers.weakReferenceProxy(textMatcherEditor);

        final CountingMatcherEditorListener<String> counter = new CountingMatcherEditorListener<String>();
        weakMatcherEditor.addMatcherEditorListener(counter);
        counter.assertCounterState(0, 0, 0, 0, 0);

        textMatcherEditor.setFilterText(new String[] {"booblah"});
        counter.assertCounterState(0, 0, 0, 1, 0);

        weakMatcherEditor.removeMatcherEditorListener(counter);
        textMatcherEditor.setFilterText(new String[] {"bibbety"});
        counter.assertCounterState(0, 0, 0, 1, 0);
    }

    public void testGarbageCollectWeakListener() {
        final TextMatcherEditor<String> textMatcherEditor = new TextMatcherEditor<String>();
        MatcherEditor<String> weakMatcherEditor = Matchers.weakReferenceProxy(textMatcherEditor);

        CountingMatcherEditorListener<String> counter = new CountingMatcherEditorListener<String>();
        final WeakReference<CountingMatcherEditorListener> weakRef = new WeakReference<CountingMatcherEditorListener>(counter);

        weakMatcherEditor.addMatcherEditorListener(counter);
        counter.assertCounterState(0, 0, 0, 0, 0);

        textMatcherEditor.setFilterText(new String[] {"booblah"});
        counter.assertCounterState(0, 0, 0, 1, 0);

        // removing the only hard reference to counter should make it gc'able
        counter = null;
        System.gc();

        textMatcherEditor.setFilterText(new String[] {"bibbety"});
        assertNull(weakRef.get());
    }

    public void testGarbageCollectWeakReferenceProxy() {
        final TextMatcherEditor<String> textMatcherEditor = new TextMatcherEditor<String>();
        MatcherEditor<String> weakMatcherEditor = Matchers.weakReferenceProxy(textMatcherEditor);

        final CountingMatcherEditorListener<String> counter = new CountingMatcherEditorListener<String>();
        weakMatcherEditor.addMatcherEditorListener(counter);
        counter.assertCounterState(0, 0, 0, 0, 0);

        textMatcherEditor.setFilterText(new String[] {"booblah"});
        counter.assertCounterState(0, 0, 0, 1, 0);

        // removing the only hard reference to weakMatcherEditor should make it gc'able
        weakMatcherEditor = null;
        System.gc();

        textMatcherEditor.setFilterText(new String[] {"bibbety"});
        counter.assertCounterState(0, 0, 0, 1, 0);
    }
}