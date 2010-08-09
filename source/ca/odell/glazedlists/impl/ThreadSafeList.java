/* Glazed Lists                                                 (c) 2003-2006 */
/* http://publicobject.com/glazedlists/                      publicobject.com,*/
/*                                                     O'Dell Engineering Ltd.*/
package ca.odell.glazedlists.impl;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.util.concurrent.Lock;
import ca.odell.glazedlists.util.concurrent.ReadWriteLock;

import java.util.Collection;

/**
 * An {@link EventList} that obtains a {@link ReadWriteLock} for all operations.
 *
 * <p>This provides some support for sharing {@link EventList}s between multiple
 * threads.
 *
 * <p>Using a {@link ThreadSafeList} for concurrent access to lists can be expensive
 * because a {@link ReadWriteLock} is aquired and released for every operation.
 *
 * <p><strong><font color="#FF0000">Warning:</font></strong> Although this class
 * provides thread safe access, it does not provide any guarantees that changes
 * will not happen between method calls. For example, the following code is unsafe
 * because the source {@link EventList} may change between calls to {@link #size() size()}
 * and {@link #get(int) get()}:
 * <pre> EventList source = ...
 * ThreadSafeList myList = new ThreadSafeList(source);
 * if(myList.size() > 3) {
 *   System.out.println(myList.get(3));
 * }</pre>
 *
 * <p><strong><font color="#FF0000">Warning:</font></strong> The objects returned
 * by {@link #iterator() iterator()}, {@link #subList(int,int) subList()}, etc. are
 * not thread safe.
 *
 * @see ca.odell.glazedlists.util.concurrent
 *
 * @author <a href="mailto:kevin@swank.ca">Kevin Maltby</a>
 */
public final class ThreadSafeList<E> extends TransformedList<E, E> {

    /**
     * Creates a {@link ThreadSafeList} that provides thread safe access to all
     * methods in the source {@link EventList}.
     */
    public ThreadSafeList(EventList<E> source) {
        super(source);
        source.addListEventListener(this);
    }

    /** {@inheritDoc} */
    @Override
    public void listChanged(ListEvent<E> listChanges) {
        // just pass on the changes
        updates.forwardEvent(listChanges);
    }

    /** {@inheritDoc} */
    @Override
    public E get(int index) {
        final Lock readLock = getReadWriteLock().readLock();
        readLock.lock();
        try {
            return source.get(index);
        } finally {
            readLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public int size() {
        final Lock readLock = getReadWriteLock().readLock();
        readLock.lock();
        try {
            return source.size();
        } finally {
            readLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    protected boolean isWritable() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(Object object) {
        final Lock readLock = getReadWriteLock().readLock();
        readLock.lock();
        try {
            return source.contains(object);
        } finally {
            readLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsAll(Collection<?> collection) {
        final Lock readLock = getReadWriteLock().readLock();
        readLock.lock();
        try {
            return source.containsAll(collection);
        } finally {
            readLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object object) {
        final Lock readLock = getReadWriteLock().readLock();
        readLock.lock();
        try {
            return source.equals(object);
        } finally {
            readLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final Lock readLock = getReadWriteLock().readLock();
        readLock.lock();
        try {
            return source.hashCode();
        } finally {
            readLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public int indexOf(Object object) {
        final Lock readLock = getReadWriteLock().readLock();
        readLock.lock();
        try {
            return source.indexOf(object);
        } finally {
            readLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public int lastIndexOf(Object object) {
        final Lock readLock = getReadWriteLock().readLock();
        readLock.lock();
        try {
            return source.lastIndexOf(object);
        } finally {
            readLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        final Lock readLock = getReadWriteLock().readLock();
        readLock.lock();
        try {
            return source.isEmpty();
        } finally {
            readLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public Object[] toArray() {
        final Lock readLock = getReadWriteLock().readLock();
        readLock.lock();
        try {
            return source.toArray();
        } finally {
            readLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public <T> T[] toArray(T[] array) {
        final Lock readLock = getReadWriteLock().readLock();
        readLock.lock();
        try {
            return source.toArray(array);
        } finally {
            readLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean add(E value) {
        final Lock writeLock = getReadWriteLock().writeLock();
        writeLock.lock();
        try {
            return source.add(value);
        } finally {
            writeLock.unlock();
        }
    }
        
    /** {@inheritDoc} */
    @Override
    public boolean remove(Object toRemove) {
        final Lock writeLock = getReadWriteLock().writeLock();
        writeLock.lock();
        try {
            return source.remove(toRemove);
        } finally {
            writeLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean addAll(Collection<? extends E> values) {
        final Lock writeLock = getReadWriteLock().writeLock();
        writeLock.lock();
        try {
            return source.addAll(values);
        } finally {
            writeLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean addAll(int index, Collection<? extends E> values) {
        final Lock writeLock = getReadWriteLock().writeLock();
        writeLock.lock();
        try {
            return source.addAll(index, values);
        } finally {
            writeLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeAll(Collection<?> values) {
        final Lock writeLock = getReadWriteLock().writeLock();
        writeLock.lock();
        try {
            return source.removeAll(values);
        } finally {
            writeLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean retainAll(Collection<?> values) {
        final Lock writeLock = getReadWriteLock().writeLock();
        writeLock.lock();
        try {
            return source.retainAll(values);
        } finally {
            writeLock.unlock();
        }
    }
        
    /** {@inheritDoc} */
    @Override
    public void clear() {
        final Lock writeLock = getReadWriteLock().writeLock();
        writeLock.lock();
        try {
            source.clear();
        } finally {
            writeLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public E set(int index, E value) {
        final Lock writeLock = getReadWriteLock().writeLock();
        writeLock.lock();
        try {
            return source.set(index, value);
        } finally {
            writeLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void add(int index, E value) {
        final Lock writeLock = getReadWriteLock().writeLock();
        writeLock.lock();
        try {
            source.add(index, value);
        } finally {
            writeLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public E remove(int index) {
        final Lock writeLock = getReadWriteLock().writeLock();
        writeLock.lock();
        try {
            return source.remove(index);
        } finally {
            writeLock.unlock();
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        final Lock readLock = getReadWriteLock().readLock();
        readLock.lock();
        try {
            return source.toString();
        } finally {
            readLock.unlock();
        }
    }
}