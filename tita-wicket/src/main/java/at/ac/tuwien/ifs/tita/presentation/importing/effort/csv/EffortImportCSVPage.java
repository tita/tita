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
package at.ac.tuwien.ifs.tita.presentation.importing.effort.csv;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;

import at.ac.tuwien.ifs.tita.presentation.BasePage;
import at.ac.tuwien.ifs.tita.presentation.controls.dropdown.SelectOption;

/**
 * Page for importing efforts via csv files.
 *
 * @author Christoph
 *
 */
public class EffortImportCSVPage extends BasePage {

    private Form<Object> form;

    private DropDownChoice<SelectOption> ddDate;
    private DropDownChoice<SelectOption> ddStarttime;
    private DropDownChoice<SelectOption> ddEndtime;
    private DropDownChoice<SelectOption> ddDuration;
    private DropDownChoice<SelectOption> ddDescription;

    private SelectOption selectedEffortOptions;

    private List<SelectOption> selectOptions = Arrays.asList(new SelectOption[] {
            new SelectOption("nochoice", "  "),
            new SelectOption("date", "Date"), new SelectOption("starttime", "Starttime"),
            new SelectOption("endtime", "Endtime"), new SelectOption("duration", "Duration"),
            new SelectOption("description", "Description") });

    public EffortImportCSVPage() {

        form = new Form<Object>("choiceForImportEffortOptions");
        form.setOutputMarkupId(true);
        add(form);

        // set select options
        selectedEffortOptions = new SelectOption("date", "Date");
        ChoiceRenderer<SelectOption> choiceRenderer = new ChoiceRenderer<SelectOption>("value",
                "key");



        ddDate = new DropDownChoice<SelectOption>("dropdownDate",
                new PropertyModel<SelectOption>(this, "selectedEffortOptions"), selectOptions,
                choiceRenderer);

        ddStarttime = new DropDownChoice<SelectOption>("dropdownStarttime",
                new PropertyModel<SelectOption>(this, "selectedEffortOptions"), selectOptions,
                choiceRenderer);

        ddEndtime = new DropDownChoice<SelectOption>("dropdownEndtime",
                new PropertyModel<SelectOption>(this, "selectedEffortOptions"), selectOptions,
                choiceRenderer);

        ddDuration = new DropDownChoice<SelectOption>("dropdownDuration",
                new PropertyModel<SelectOption>(this, "selectedEffortOptions"), selectOptions,
                choiceRenderer);

        ddDescription = new DropDownChoice<SelectOption>("dropdownDescription",
                new PropertyModel<SelectOption>(this, "selectedEffortOptions"), selectOptions,
                choiceRenderer);

        ddDate.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {

                SelectOption option = getSelectOptionForDropdownValue(ddDate
                        .getValue());

                if (option != null) {
                    removeSelectOption(option);
                }

                ddStarttime
                        .setChoices(selectOptions);

                ddEndtime
                        .setChoices(selectOptions);

                ddDuration
                        .setChoices(selectOptions);

                ddDescription
                        .setChoices(selectOptions);
            }
        });

        form.add(new Label("labelDate", "Date:"));
        form.add(ddDate);

        form.add(new Label("labelStarttime", "Starttime:"));
        form.add(ddStarttime);

        form.add(new Label("labelEndtime", "Endtime:"));
        form.add(ddEndtime);

        form.add(new Label("labelDuration", "Duration:"));
        form.add(ddDuration);

        form.add(new Label("labelDescription", "Description:"));
        form.add(ddDescription);
    }

    /**
     * Remove a selectOption.
     *
     * @param selectOption
     *            - s
     */
    public void removeSelectOption(SelectOption selectOption) {

        for (int i = 0; i < selectOptions.size(); i++) {
            if (selectOptions.get(i).getKey().equals(selectOption.getKey())) {
                selectOptions.remove(i);
            }
        }
    }

    /**
     * Create a SelectOption for the selected value of the chosenOption.
     *
     * @param chosenOption
     *            - the key
     * @return the select option
     */
    public SelectOption getSelectOptionForDropdownValue(String chosenOption) {

        if (chosenOption.equals("date")) {
            return new SelectOption("date", "Date");
        }

        if (chosenOption.equals("starttime")) {
            return new SelectOption("starttime", "Starttime");
        }

        if (chosenOption.equals("endtime")) {
            return new SelectOption("endtime", "Endtime");
        }

        if (chosenOption.equals("duration")) {
            return new SelectOption("duration", "Duration");
        }

        if (chosenOption.equals("description")) {
            return new SelectOption("description", "Description");
        } else {
            return null;
        }
    }
}
