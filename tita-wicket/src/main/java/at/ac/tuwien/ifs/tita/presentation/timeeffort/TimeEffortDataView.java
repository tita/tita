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

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;

import at.ac.tuwien.ifs.tita.datasource.entity.BaseTimeEffort;
import at.ac.tuwien.ifs.tita.datasource.entity.TimeEffort;
import at.ac.tuwien.ifs.tita.datasource.util.TiTATimeConverter;
import at.ac.tuwien.ifs.tita.presentation.utils.GlobalUtils;

/**
 * Time efforts are displayed in a table list
 * 
 * @author msiedler
 * 
 */
public class TimeEffortDataView extends DataView<TimeEffort> {

    protected TimeEffortDataView(String id,
            IDataProvider<TimeEffort> dataProvider) {
        super(id, dataProvider);
    }

    @Override
    protected void populateItem(Item<TimeEffort> item) {

        if (item.getIndex() % 2 == 0) {
            item.add(new SimpleAttributeModifier("class", "even"));
        } else {
            item.add(new SimpleAttributeModifier("class", "odd"));
        }

        item.setDefaultModel(new CompoundPropertyModel<BaseTimeEffort>(item
                .getModel()));
        TimeEffort timeEffort = (TimeEffort) item.getModelObject();

        Label lbDate = new Label("date", GlobalUtils.DATEFORMAT
                .format(timeEffort.getDate()));

        Label lbDescription = new Label("description", timeEffort
                .getDescription());

        Label lbStarttime = new Label("starttime",
                (timeEffort.getStartTime() == null ? ""
                        : GlobalUtils.TIMEFORMAT24HOURS.format(timeEffort
                                .getStartTime())));

        Label lbEndtime = new Label("endtime",
                (timeEffort.getEndTime() == null ? ""
                        : GlobalUtils.TIMEFORMAT24HOURS.format(timeEffort
                                .getEndTime())));

        Label lbLength = new Label("length",
                (timeEffort.getDuration() == null ? "" : TiTATimeConverter
                        .Duration2String(timeEffort.getDuration())));
        lbDate.setOutputMarkupId(true);
        lbDescription.setOutputMarkupId(true);
        lbLength.setOutputMarkupId(true);

        item.add(lbDate);
        item.add(lbDescription);
        item.add(lbStarttime);
        item.add(lbEndtime);
        item.add(lbLength);

        item.add(getEditButton());
        item.add(getDeleteButton());
    }

    private Button getEditButton() {
        Button editButton = new Button("edit") {
            @Override
            public void onSubmit() {
                info("editButton.onSubmit executed");
            }
        };
        editButton.setOutputMarkupId(true);
        return editButton;
    }

    private Button getDeleteButton() {
        Button deleteButton = new Button("delete") {
            @Override
            public void onSubmit() {
                info("deleteButton.onSubmit executed");
            }
        };
        deleteButton.setOutputMarkupId(true);
        return deleteButton;
    }

}
