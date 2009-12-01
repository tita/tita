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
package at.ac.tuwien.ifs.tita.presentation.timeeffort;

import java.util.List;
import java.util.TimeZone;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
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
public class TimeEffortListView<T> extends ListView<TimeEffort> {

    public TimeEffortListView(String id, List<TimeEffort> list) {
        super(id, list);
    }

    /**
     * Populates the items of the table.
     * 
     * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
     * @param item
     *            items that will be inserted in the table.
     */
    @Override
    protected void populateItem(ListItem<TimeEffort> item) {
        TimeEffort timeEffort = item.getModelObject();
        Label lbDate = new Label("date", GlobalUtils.DATEFORMAT
                .format(timeEffort.getDate()));

        Label lbDescription = new Label("description", timeEffort
                .getDescription());

        String startTime = GlobalUtils.TIMEFORMAT24HOURS.format(timeEffort
                .getStartTime());
        String endTime = GlobalUtils.TIMEFORMAT24HOURS.format(timeEffort
                .getEndTime());

        Label lbStartTime = new Label("starttime", startTime);
        Label lbEndTime = new Label("endtime", endTime);

        GlobalUtils.TIMEFORMAT24HOURS.setTimeZone(TimeZone.getTimeZone("GMT"));
        Label lbLength = new Label("length", ""
                + GlobalUtils.TIMEFORMAT24HOURS.format(GlobalUtils
                        .getTimeDifference(timeEffort.getStartTime(),
                                timeEffort.getEndTime())));

        lbDate.setOutputMarkupId(true);
        lbDescription.setOutputMarkupId(true);
        lbStartTime.setOutputMarkupId(true);
        lbEndTime.setOutputMarkupId(true);
        lbLength.setOutputMarkupId(true);

        item.add(lbDate);
        item.add(lbDescription);
        item.add(lbStartTime);
        item.add(lbEndTime);
        item.add(lbLength);

        addEditButton(item);
        addDeleteButton(item);
    }

    /**
     * Adds an edit button
     * 
     * @param item
     *            where button is placed
     */
    private void addEditButton(ListItem<TimeEffort> item) {
        Button editButton = new Button("edit") {
            @Override
            public void onSubmit() {
                info("editButton.onSubmit executed");
            }
        };
        editButton.setOutputMarkupId(true);
        item.add(editButton);
    }

    /**
     * Adds a delete button
     * 
     * @param item
     *            where button is placed
     */
    private void addDeleteButton(ListItem<TimeEffort> item) {
        Button deleteButton = new Button("delete") {
            @Override
            public void onSubmit() {
                info("deleteButton.onSubmit executed");
            }
        };
        deleteButton.setOutputMarkupId(true);
        item.add(deleteButton);
    }
}
