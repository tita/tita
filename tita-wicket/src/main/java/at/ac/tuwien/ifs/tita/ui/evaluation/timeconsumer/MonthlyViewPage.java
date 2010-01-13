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
package at.ac.tuwien.ifs.tita.ui.evaluation.timeconsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.table.Table;

import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.reporting.JasperPdfResource;
import at.ac.tuwien.ifs.tita.ui.BasePage;
import at.ac.tuwien.ifs.tita.ui.controls.dropdown.SelectOption;
import at.ac.tuwien.ifs.tita.ui.login.TitaSession;
import at.ac.tuwien.ifs.tita.ui.models.TableModelTimeConsumerEvaluation;

/**
 * Monthly evaluation.
 */
public class MonthlyViewPage extends BasePage {
    private final Logger log = LoggerFactory.getLogger(MonthlyViewPage.class);

    @SpringBean(name = "timeEffortService")
    private IEffortService effortService;

    @SpringBean(name = "userService")
    private IUserService userService;

    @SpringBean(name = "monthlyViewReport")
    private JasperPdfResource pdfResource;

    private SelectOption selectedYear;
    private SelectOption selectedMonth;

    private List<SelectOption> years;
    private List<SelectOption> months;

    private TableModelTimeConsumerEvaluation tableModel;

    public MonthlyViewPage() {
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
        Form<Effort> form = new Form<Effort>("timeConsumerEvaluationForm", new CompoundPropertyModel<Effort>(
                new Effort()));
        add(form);
        form.setOutputMarkupId(true);

        ChoiceRenderer choiceRenderer = new ChoiceRenderer("value", "key");

        final DropDownChoice ddYears = new DropDownChoice("yearSelection", new PropertyModel(this, "selectedYear"),
                getYears(), choiceRenderer);
        form.add(ddYears);

        final DropDownChoice ddMonths = new DropDownChoice("monthSelection", new PropertyModel(this, "selectedMonth"),
                getMonths(), choiceRenderer);
        form.add(ddMonths);

        final WebMarkupContainer timeeffortContainer = new WebMarkupContainer("timeeffortContainer");
        timeeffortContainer.setOutputMarkupId(true);
        timeeffortContainer.setOutputMarkupPlaceholderTag(true);
        add(timeeffortContainer);

        initButtons(form, timeeffortContainer);

        Calendar cal = Calendar.getInstance();
        tableModel = new TableModelTimeConsumerEvaluation(getTimeEffortsMonthlyView(cal.get(Calendar.YEAR), cal
                .get(Calendar.MONTH)));
        Table table = new Table("tetable", tableModel);
        timeeffortContainer.add(table);
    }

    /**
     * Initializes buttons of form.
     *
     * @param form form of page.
     * @param container container of page.
     */
    private void initButtons(final Form<Effort> form, final WebMarkupContainer container) {
        final Button btnShowAsPDF = new Button("btnShowPDF") {
            @Override
            public void onSubmit() {
                try {
                    loadReport();
                    ResourceStreamRequestTarget rsrtarget = new ResourceStreamRequestTarget(pdfResource
                            .getResourceStream());
                    rsrtarget.setFileName(pdfResource.getFilename());
                    RequestCycle.get().setRequestTarget(rsrtarget);
                } catch (JRException e) {
                    // TODO: GUI Exception Handling
                    log.error(e.getMessage());

                } catch (PersistenceException e) {
                    // TODO: GUI Exception Handling
                    log.error(e.getMessage());
                }
            }

            @Override
            public boolean isEnabled() {
                return tableModel.getRowCount() == 0 ? false : true;
            }
        };

        form.add(btnShowAsPDF);

        form.add(new AjaxButton("btnShowEvaluation", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                Integer year = Integer.valueOf(selectedYear.toString());
                Integer month = Integer.valueOf(selectedMonth.toString());
                tableModel.reload(getTimeEffortsMonthlyView(year, month));
                target.addComponent(container);
                target.addComponent(btnShowAsPDF);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form1) {
                // TODO Set border red on textfields which are'nt filled
            }
        });
    }

    /**
     * loads report and sets data source.
     * 
     * @throws JRException
     *             JasperReports Exception
     * @throws PersistenceException
     *             if user cannot be found
     */
    private void loadReport() throws JRException, PersistenceException {
        ServletContext context = ((WebApplication) getApplication()).getServletContext();
        pdfResource.loadReport(context.getRealPath(pdfResource.getDesignFilename()));
        pdfResource.setReportDataSource(new JRTableModelDataSource(tableModel));

        TiTAUser currentUser = userService.getUserByUsername(TitaSession.getSession().getUsername());
        String name = currentUser.getFirstName() + " " + currentUser.getLastName();
        pdfResource.addReportParameter("name", name.replaceAll("\n", ""));

        pdfResource.addReportParameter("month", selectedMonth.getValue().toString());
        pdfResource.addReportParameter("year", selectedYear.getValue().toString());
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
            list = effortService.getEffortsMonthlyView(year, month);
        } catch (PersistenceException e) {
            // TODO: GUI Exception Handling
            log.error(e.getMessage());
        }

        return list;
    }

    /**
     * Inits all years.
     */
    private void initYears() {
        years = new ArrayList<SelectOption>();
        List<Integer> effortYears = effortService.getEffortsYears();
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
