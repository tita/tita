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

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import at.ac.tuwien.ifs.tita.datasource.entity.TimeEffort;
import at.ac.tuwien.ifs.tita.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.presentation.utils.SelectOption;
import at.ac.tuwien.ifs.tita.service.time.IEffortService;

/**
 * Monthly evaluation.
 */
public class MonthlyView extends WebPage {
    @SpringBean(name = "timeEffortService")
    private IEffortService service;

    private SelectOption selectedYear;
    private SelectOption selectedMonth;

    private List<SelectOption> months;

    public MonthlyView() {
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        int month = Calendar.getInstance().get(Calendar.MONTH);

        this.selectedYear = new SelectOption(year, year);

        initMonths();
        this.selectedMonth = new SelectOption(this.months.get(month).getKey(), this.months.get(month).getValue());

        initPage();
    }

    /**
     * Inits Page.
     */
    private void initPage() {
        Form<TimeEffort> form = new Form<TimeEffort>("dailyviewform", new CompoundPropertyModel<TimeEffort>(
                new TimeEffort()));
        add(form);
        form.setOutputMarkupId(true);

        ChoiceRenderer choiceRenderer = new ChoiceRenderer("value", "key");
        /* TODO: read years from database. */
        SelectOption[] options = new SelectOption[] { new SelectOption("2009", "2009"),
                new SelectOption("2008", "2008") };
        final DropDownChoice ddYears = new DropDownChoice("yearSelection", new PropertyModel(this, "selectedYear"),
                Arrays.asList(options), choiceRenderer);
        form.add(ddYears);

        final DropDownChoice ddMonths = new DropDownChoice("monthSelection", new PropertyModel(this, "selectedMonth"),
                getMonths(), choiceRenderer);
        form.add(ddMonths);

        final TimeEffortEvaluationListView<TimeEffort> listView = new TimeEffortEvaluationListView<TimeEffort>(
                "dailyList", getTimeEffortsMonthlyView(Calendar.getInstance()));
        listView.setOutputMarkupId(true);

        final WebMarkupContainer timeeffortContainer = new WebMarkupContainer("timeeffortContainer");
        timeeffortContainer.setOutputMarkupId(true);
        timeeffortContainer.setOutputMarkupPlaceholderTag(true);
        add(timeeffortContainer);
        timeeffortContainer.add(listView);

        form.add(new AjaxButton("btnShowMonthly", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.valueOf(MonthlyView.this.selectedYear.toString()), Integer
                        .valueOf(MonthlyView.this.selectedMonth.toString()), 1);
                listView.setList(getTimeEffortsMonthlyView(cal));
                target.addComponent(timeeffortContainer);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form1) {
                // TODO Set border red on textfields which are'nt filled
            }
        });
    }

    /**
     * Gets time effort data by date.
     * 
     * @param cal
     *            date of timeeffort entry
     * @return all timeefforts that match the date
     */
    private List<TimeEffort> getTimeEffortsMonthlyView(Calendar cal) {
        List<TimeEffort> list = null;
        try {
            list = this.service.getTimeEffortsMonthlyView(cal);
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Inits all months.
     * 
     */
    private void initMonths() {
        this.months = Arrays.asList(new SelectOption("0", "January"), new SelectOption("1", "February"),
                new SelectOption("2", "March"), new SelectOption("3", "April"), new SelectOption("4", "May"),
                new SelectOption("5", "June"), new SelectOption("6", "July"), new SelectOption("7", "August"),
                new SelectOption("8", "September"), new SelectOption("9", "October"),
                new SelectOption("10", "November"), new SelectOption("11", "December"));
    }

    /**
     * Return months.
     * 
     * @return the months
     */
    public List<SelectOption> getMonths() {
        return this.months;
    }

    /**
     * Set Months.
     * 
     * @param months
     *            the months to set
     */
    public void setMonths(List<SelectOption> months) {
        this.months = months;
    }

    /**
     * Returns selected year.
     * 
     * @return the selectedYear
     */
    public SelectOption getSelectedYear() {
        return this.selectedYear;
    }

    /**
     * Sets selected year.
     * 
     * @param selectedYear
     *            the selectedYear to set
     */
    public void setSelectedYear(SelectOption selectedYear) {
        this.selectedYear = selectedYear;
    }

    /**
     * Returns selected month.
     * 
     * @return the selectedMonth
     */
    public SelectOption getSelectedMonth() {
        return this.selectedMonth;
    }

    /**
     * Sets selected month.
     * 
     * @param selectedMonth
     *            the selectedMonth to set
     */
    public void setSelectedMonth(SelectOption selectedMonth) {
        this.selectedMonth = selectedMonth;
    }
}
