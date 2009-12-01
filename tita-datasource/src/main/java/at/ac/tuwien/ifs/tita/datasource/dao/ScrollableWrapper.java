package at.ac.tuwien.ifs.tita.datasource.dao;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.hibernate.ScrollableResults;

/**
 * A Wrapper Class for handling Hibernates ScrollableResults as Iterator.
 * @param <T>      Type contained in Iterator.
 */
public class ScrollableWrapper<T> implements Iterator<T> {
    private ScrollableResults hibernateResult;
    private boolean hasNextRecord = false;
    private boolean isFetched     = false;
    
    
    public ScrollableWrapper(ScrollableResults sr){
        hibernateResult = sr;
    }
    
    /** 
     * The Iterator can be asked, if there is a next element.
     * The hibernate ScrollableResult only knows if next element is fetched in buffer.
     * 
     * @return true if ther is another element.
     */
    public boolean hasNext() {
        // call hibernates next only one time until record is used.
        if(!isFetched) {
            hasNextRecord = hibernateResult.next();
            isFetched = true;
        } 
        return hasNextRecord;
    }

    /** 
     * The Iterator gives the next element or raises an Exception if there is none.
     * @return ölöklök
     */
    @SuppressWarnings("unchecked")
    public T next() {
        T record = null;
        if (hasNext()) {
            record = (T)hibernateResult.get(0);
        }
        // Reset the hasNext() logic. -> Next call is next record again. 
        isFetched = false;
        if (record == null) {
            throw new NoSuchElementException(
                "ScrollableWrapper: No next element in ScrollableResult.");
        }
        return record;
    }

    
    /** 
     * Remove is not implemented in this Wrapper for hibernate ScrollableResult.
     */
    public void remove() {
        throw new UnsupportedOperationException("Not possible to wrap this function!");
    }
    

}
