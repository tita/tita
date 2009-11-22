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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.presentation.utils.GlobalUtils;
import at.ac.tuwien.ifs.tita.timeeffort.domain.TimeEffort;
import at.ac.tuwien.ifs.tita.timeeffort.service.ITimeEffortService;

/**
 * 
 * Wicket Panel for time effort administration
 * 
 * @author msiedler
 * 
 */
public class TimeEffortAdministrationPanel extends Panel implements GlobalUtils {

    @SpringBean(name = "timeEffortService")
    private ITimeEffortService service;

    // private User user;
    // private Project project;

    public TimeEffortAdministrationPanel(String id) {
        super(id);
        displayPanel();
    }

    /**
     * Displays panel
     */
    private void displayPanel() {
        try {
            // TimeEffort timeEffort2 = service.getTimeEffortById(0L);
            List<TimeEffort> list = new ArrayList<TimeEffort>();
            TimeEffort timeEffort2 = new TimeEffort();
            timeEffort2.setDate(new Date());
            timeEffort2.setDescription("Das ist eine Testbeschreibung");
            timeEffort2.setEndTime(new Date());
            timeEffort2.setStartTime(new Date());
            TimeEffort timeEffort3 = service.saveTimeEffort(timeEffort2);
            list.add(timeEffort3);

            add(new TimeEffortListView<TimeEffort>("timeEffortList", list));
        } catch (TitaDAOException e) {
            add(new Label("message", "Couldn't read data from DB."));
        }
    }

    private class TimeEffortListView<T> extends ListView<TimeEffort> {

        TimeEffortListView(String id, List<TimeEffort> list) {
            super(id, list);
        }

        @Override
        protected void populateItem(ListItem<TimeEffort> item) {
            TimeEffort timeEffort = item.getModelObject();
            item
                    .add(new Label("date", DATEFORMAT.format(timeEffort
                            .getDate())));
            item.add(new Label("description", timeEffort.getDescription()));
            item.add(new Label("length", TIMELENGTHFORMAT.format(timeEffort
                    .getEndTime().compareTo(timeEffort.getStartTime()))));
        }
    }
}
