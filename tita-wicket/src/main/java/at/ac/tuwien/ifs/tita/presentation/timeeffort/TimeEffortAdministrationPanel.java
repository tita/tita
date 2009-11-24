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

import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
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

    private final Date date = new Date();

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
        List<TimeEffort> list = initData();

        addDateTextfield();
        add(new TextField<String>("descriptionTextfield"));
        addTimeTextfield("startTime");
        addTimeTextfield("endTime");
        add(new TimeEffortListView<TimeEffort>("timeEffortList", list));
        addSaveButton();
    }

    private void addSaveButton() {
        Button saveButton = new Button("saveButton") {
            @Override
            public void onSubmit() {
                info("saveButton.onSubmit executed");
                refresh(new ArrayList<TimeEffort>());
            }
        };
        add(saveButton);
    }

    private void addEditButton(ListItem<TimeEffort> item) {
        Button editButton = new Button("edit") {
            @Override
            public void onSubmit() {
                info("editButton.onSubmit executed");
            }
        };
        item.add(editButton);
    }

    private void addDeleteButton(ListItem<TimeEffort> item) {
        Button deleteButton = new Button("delete") {
            @Override
            public void onSubmit() {
                info("deleteButton.onSubmit executed");
            }
        };
        item.add(deleteButton);
    }

    /**
     * Creates a textfield with date picker
     * 
     * @return
     */
    private void addDateTextfield() {
        DateTextField dateTextField = new DateTextField("dateTextField",
                new PropertyModel<Date>(this, "date"), new StyleDateConverter(
                        "S-", true));

        dateTextField.add(new DatePicker());
        add(dateTextField);
    }

    /**
     * Creates a textfield with date picker
     * 
     * @return
     */
    private void addTimeTextfield(String timeField) {
        TextField<Integer> startTextField = new TextField<Integer>("hour"
                + timeField);
        TextField<Integer> endTextField = new TextField<Integer>("minute"
                + timeField);

        add(startTextField);
        add(endTextField);
    }

    @SuppressWarnings("unchecked")
    private void refresh(List<TimeEffort> list) {
        ((TimeEffortListView<TimeEffort>) this.get("timeEffortList"))
                .setList(list);
    }

    /**
     * Time efforts are displayed in a table list
     * 
     * @author msiedler
     * 
     * @param <T>
     */
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
            addEditButton(item);
            addDeleteButton(item);
        }
    }

    // temp method
    // TODO search issue
    private List<TimeEffort> initData() {
        List<TimeEffort> list = new ArrayList<TimeEffort>();
        try {
            // TimeEffort timeEffort2 = service.getTimeEffortById(0L);
            TimeEffort timeEffort2 = new TimeEffort();
            timeEffort2.setDate(new Date());
            timeEffort2.setDescription("Das ist eine Testbeschreibung");
            timeEffort2.setEndTime(new Date());
            timeEffort2.setStartTime(new Date());
            timeEffort2 = service.saveTimeEffort(timeEffort2);
            list.add(timeEffort2);

            timeEffort2 = new TimeEffort();
            timeEffort2.setDate(new Date());
            timeEffort2
                    .setDescription("Das ist eine Testbeschreibung Das ist eine Testbeschreibung");
            timeEffort2.setEndTime(new Date());
            timeEffort2.setStartTime(new Date());
            timeEffort2 = service.saveTimeEffort(timeEffort2);
            list.add(timeEffort2);
        } catch (TitaDAOException e) {
            add(new Label("message", "Couldn't read data from DB."));
        }
        return list;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }
}
