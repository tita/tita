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
package at.ac.tuwien.ifs.tita.presentation.effort.evaluation.timeconsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.presentation.BasePage;
import at.ac.tuwien.ifs.tita.presentation.controls.dropdown.SelectOption;
import at.ac.tuwien.ifs.tita.presentation.controls.listview.EffortEvaluationListView;

/**
 * Monthly evaluation.
 */
public class MonthlyView extends BasePage {
    @SpringBean(name = "timeEffortService")
    private IEffortService service;

    private SelectOption selectedYear;
    private SelectOption selectedMonth;

    private List<SelectOption> years;
    private List<SelectOption> months;

    public MonthlyView() {
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        int month = Calendar.getInstance().get(Calendar.MONTH);

        initYears();
        selectedYear = new SelectOption(year, year);

        initMonths();
        selectedMonth = new SelectOption(months.get(month).getKey(), months.get(month).getValue());

        initPage();
    }

    /**
     * Inits Page.
     */
    @SuppressWarnings("unchecked")
    private void initPage() {
        Form<Effort> form = new Form<Effort>("dailyviewform", new CompoundPropertyModel<Effort>(new Effort()));
        add(form);
        form.setOutputMarkupId(true);

        ChoiceRenderer choiceRenderer = new ChoiceRenderer("value", "key");

        final DropDownChoice ddYears = new DropDownChoice("yearSelection", new PropertyModel(this, "selectedYear"),
                getYears(), choiceRenderer);
        form.add(ddYears);

        final DropDownChoice ddMonths = new DropDownChoice("monthSelection", new PropertyModel(this, "selectedMonth"),
                getMonths(), choiceRenderer);
        form.add(ddMonths);

        Calendar cal = Calendar.getInstance();
        final EffortEvaluationListView<Effort> listView = new EffortEvaluationListView<Effort>("dailyList",
                getTimeEffortsMonthlyView(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)));
        listView.setOutputMarkupId(true);

        final WebMarkupContainer timeeffortContainer = new WebMarkupContainer("timeeffortContainer");
        timeeffortContainer.setOutputMarkupId(true);
        timeeffortContainer.setOutputMarkupPlaceholderTag(true);
        add(timeeffortContainer);
        timeeffortContainer.add(listView);

        form.add(new AjaxButton("btnShowMonthly", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                Integer year = Integer.valueOf(selectedYear.toString());
                Integer month = Integer.valueOf(selectedMonth.toString());
                listView.setList(getTimeEffortsMonthlyView(year, month));
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
     * @param year year of effort entry
     * @param month month of effort entry
     * @return all efforts that match the date
     */
    private List<Effort> getTimeEffortsMonthlyView(Integer year, Integer month) {
        List<Effort> list = null;
        try {
            list = service.getEffortsMonthlyView(year, month);
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Inits all years.
     */
    private void initYears() {
        years = new ArrayList<SelectOption>();
        List<Integer> effortYears = service.getEffortsYears();
        for (Integer e : effortYears) {
            years.add(new SelectOption(e.toString(), e.toString()));
        }
    }

    /**
     * Inits all months.
     * 
     */
    private void initMonths() {
        months = Arrays.asList(new SelectOption("0", "January"), new SelectOption("1", "February"), new SelectOption(
                "2", "March"), new SelectOption("3", "April"), new SelectOption("4", "May"), new SelectOption("5",
                "June"), new SelectOption("6", "July"), new SelectOption("7", "August"), new SelectOption("8",
                "September"), new SelectOption("9", "October"), new SelectOption("10", "November"), new SelectOption(
                "11", "December"));
    }

    /**
     * Return years.
     * 
     * @return the years
     */
    public List<SelectOption> getYears() {
        return years;
    }

    /**
     * Return months.
     * 
     * @return the months
     */
    public List<SelectOption> getMonths() {
        return months;
    }

    /**
     * Set Months.
     * 
     * @param months the months to set
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
        return selectedYear;
    }

    /**
     * Sets selected year.
     * 
     * @param selectedYear the selectedYear to set
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
        return selectedMonth;
    }

    /**
     * Sets selected month.
     * 
     * @param selectedMonth the selectedMonth to set
     */
    public void setSelectedMonth(SelectOption selectedMonth) {
        this.selectedMonth = selectedMonth;
    }
}
