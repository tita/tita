package at.ac.tuwien.ifs.tita.datasource.dao;

/**
 * Interface to allow Spring Injection of PersistenceContext.
 */
public interface IPersistenceContextProvider {

    /**
     * Return 'true' if data has been changed and should be saved into the database.
     * @return true/false
     */
    boolean isDataChanged();

    /**
     * Clears the session completely. 
     * Evict all loaded instances and cancel all pending saves, updates and deletions.
     */
    void clear();

    /**
     * Flushes the session, executes all necessary SQL-Statements immediately.
     */
    void flush();
    
    
    /**
     * Flushes the session und clears the session completely.
     * Evict all loaded instances and cancel all pending saves, updates and deletions.
     * 
     */
    void flushnClear();
}