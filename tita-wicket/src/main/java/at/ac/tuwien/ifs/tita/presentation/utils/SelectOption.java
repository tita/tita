package at.ac.tuwien.ifs.tita.presentation.utils;

import java.io.Serializable;

/**
 * For key-value pairs in select tag.
 * 
 * @author rene
 * 
 */
public class SelectOption implements Serializable {
    private String key;
    private String value;

    public SelectOption(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns key.
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets key.
     * 
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Returns value.
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets value.
     * 
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Overwrites toString.
     * 
     * @return key as string
     */
    @Override
    public String toString() {
        return getKey();
    }
}
