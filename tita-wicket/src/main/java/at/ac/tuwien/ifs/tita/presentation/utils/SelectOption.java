/**
   Copyright 2009 TiTA Project, Vienna University of Technology
   
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
   
       http://www.apache.org/licenses/LICENSE\-2.0
       
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
 */
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
        return this.key;
    }

    /**
     * Sets key.
     * 
     * @param key
     *            the key to set
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
        return this.value;
    }

    /**
     * Sets value.
     * 
     * @param value
     *            the value to set
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
