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
package at.ac.tuwien.ifs.tita.presentation.timeeffort.evaluation;

import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import at.ac.tuwien.ifs.tita.presentation.utils.GlobalUtils;
import at.ac.tuwien.ifs.tita.timeeffort.domain.TimeEffort;

/**
 * Time efforts are displayed in a table list.
 * 
 * @author msiedler
 * @author rene
 * 
 * @param <T>
 */
public class TimeEffortEvaluationListView<T> extends ListView<TimeEffort> {

    private static final Integer SUBTRACTTIME = 3600000;

    public TimeEffortEvaluationListView(String id, List<TimeEffort> list) {
        super(id, list);
    }

    /**
     * Populates the items of the table.
     * 
     * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
     * @param item items that will be inserted in the table.
     */
    @Override
    protected void populateItem(ListItem<TimeEffort> item) {
        TimeEffort timeEffort = item.getModelObject();
        Label lbDate = new Label("date", GlobalUtils.DATEFORMAT.format(timeEffort.getDate()));

        Label lbDescription = new Label("description", timeEffort.getDescription());

        Label lbLength = new Label("length", ""
                + GlobalUtils.TIMELENGTHFORMAT.format(new Date(timeEffort.getEndTime().getTime()
                        - timeEffort.getStartTime().getTime() - SUBTRACTTIME)));

        lbDate.setOutputMarkupId(true);
        lbDescription.setOutputMarkupId(true);
        lbLength.setOutputMarkupId(true);

        item.add(lbDate);
        item.add(lbDescription);
        item.add(lbLength);
    }
}
