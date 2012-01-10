package ca.odell.glazedlists;

import java.util.List;

/**
 * Inmutable path of node, highly customized "arraylist" implementation
 * 
 * <p>TODO KI could optimize size()==1 case to avoid array allocation.
 * Would help in "flat" treetables a lot (path size 0..1 in 95% rows)
 */
public final class Path<E> {
    private final Object[] mPath;
    private final int mFromIndex;
    private final int mToIndex;
    private int mHashCode;

    /**
     * @param pPath Path, this can be re-used collection, new copy is created
     */
    public Path(List<E> pPath) {
        mPath = pPath.toArray(new Object[pPath.size()]);
        mFromIndex = 0;
        mToIndex = mPath.length;

        updateHash(); 
    }

    /**
     * <p>NOTE KI This constructor is useless since Path is inmutable
     */
    public Path(Path<E> pPath) {
        // can share array; it's inmutable
        mPath = pPath.mPath;
        mFromIndex = pPath.mFromIndex;
        mToIndex = pPath.mToIndex;
        
        mHashCode = pPath.mHashCode;
    }
    
    private Path(Path pPath, int pFromIndex, int pToIndex) {
        // can share array; it's inmutable
        mPath = pPath.mPath;
        mFromIndex = pFromIndex;
        mToIndex = pToIndex;
        
        updateHash(); 
    }

    private void updateHash() {
        // cache hash value
        // @see AbstractList.hashCode()
        int hash = 1;
        for (int i = mToIndex - 1; i >= mFromIndex; i--) {
            Object elem = mPath[i];
            hash = 31 * hash + (elem == null ? 0 : elem.hashCode());
        }
        mHashCode = hash;
    }

    @Override
    public int hashCode() {
        return mHashCode;
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (null == pObj) {
            return false;
        }
        
        boolean result;
        final Path<E> b = (Path<E>)pObj;
        final int size = size();
        
        result = mHashCode == b.mHashCode
            && size == b.size();
        
        if (result) {
            for (int i = size - 1; result && i >= 0; i--) {
                E elemA = get(i);
                E elemB = b.get(i);
                result = elemA != null 
                    ? elemA.equals(elemB) 
                    : elemB == null;
            }
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append('[');
        for (int i = mFromIndex; i < mToIndex; i++) {
            sb.append(mPath[i]);
            if (i < mToIndex - 1) {
                sb.append(',');
            }
        }
        sb.append(']');
        return sb.toString();
    }

    public int size() {
        return mToIndex - mFromIndex;
    }
    
    public E get(int pIndex) {
        return (E)mPath[mFromIndex + pIndex];
    }
    
    /**
     * @param pFromIndex inclusive
     * @param pToIndex Exclusive
     * @return sub-path
     */
    public Path<E> subPath(int pFromIndex, int pToIndex) {
        return new Path<E>(this, pFromIndex, pToIndex);
    }
}

