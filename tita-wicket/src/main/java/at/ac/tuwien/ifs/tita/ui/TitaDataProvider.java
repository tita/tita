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
package at.ac.tuwien.ifs.tita.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * TODO: write javadoc.
 *
 * @author msiedler
 * @param <T> x
 */
public class TitaDataProvider<T> extends SortableDataProvider<T> {

    private List<T> list = new ArrayList<T>();

    public TitaDataProvider(List<T> list, String sort, boolean asc) {

        setSort(sort, asc);

        this.list = list;
    }

    /**
     * TODO: write javadoc.
     *
     * @param first x
     * @param count x
     * @return x
     */
    public Iterator<T> iterator(int first, int count) {
        List<T> newList = new ArrayList<T>();
        newList.addAll(list.subList(first, first + count));

        final String sortColumn = this.getSort().getProperty();
        final boolean ascending = this.getSort().isAscending();

        Collections.sort(newList, new Comparator<Object>() {

            @SuppressWarnings("unchecked")
            public int compare(Object obj1, Object obj2) {
                PropertyModel<Object> model1 = new PropertyModel<Object>(obj1, sortColumn);
                PropertyModel<Object> model2 = new PropertyModel<Object>(obj2, sortColumn);

                Object modelObject1 = model1.getObject();
                Object modelObject2 = model2.getObject();

                int compare = ((Comparable<Object>) modelObject1).compareTo(modelObject2);

                if (!ascending) {
                    compare *= -1;
                }

                return compare;
            }
        });

        return newList.iterator();
    }

    /**
     * TODO: write javadoc.
     *
     * @return x
     */
    public int size() {
        return list.size();
    }

    /**
     * TODO: write javadoc.
     *
     * @param list x
     */
    public void setList(List<T> list) {
        this.list = list;
    }

    /**
     * TODO: write javadoc.
     *
     * @param object x
     * @return x
     */
    public IModel<T> model(final T object) {
        return new AbstractReadOnlyModel<T>() {
            @Override
            public T getObject() {
                return object;
            }
        };
    }

}
