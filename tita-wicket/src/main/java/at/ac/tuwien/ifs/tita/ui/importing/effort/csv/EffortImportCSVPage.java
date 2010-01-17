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
package at.ac.tuwien.ifs.tita.ui.importing.effort.csv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.swing.ListSelectionModel;

import org.apache.wicket.Application;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.lang.Bytes;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.wicketstuff.table.Table;

import at.ac.tuwien.ifs.tita.business.csv.IImportReader;
import at.ac.tuwien.ifs.tita.business.service.project.IProjectService;
import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.ui.BasePage;
import at.ac.tuwien.ifs.tita.ui.TiTAApplication;
import at.ac.tuwien.ifs.tita.ui.controls.dropdown.SelectOption;
import at.ac.tuwien.ifs.tita.ui.login.TitaSession;
import at.ac.tuwien.ifs.tita.ui.models.TableModelEffortImport;
import at.ac.tuwien.ifs.tita.ui.models.TableModelTiTAProject;
import at.ac.tuwien.ifs.tita.ui.utils.IntegerConstants;

/**
 * Page for importing efforts via csv files.
 *
 * @author Christoph
 *
 */
public class EffortImportCSVPage extends BasePage {

    @SpringBean(name = "titaCSVReader")
    private IImportReader reader;

    @SpringBean(name = "userService")
    private IUserService userService;

    @SpringBean(name = "titaProjectService")
    private IProjectService titaProjectService;

    private Form<Object> form;

    private List<TiTAProject> titaProjectList = new ArrayList<TiTAProject>();
    private List<Effort> effortResult = new ArrayList<Effort>();

    private Table tableForTiTAProject;
    private Table tableForImportedEfforts;

    private TableModelTiTAProject tmForTiTAProject;
    private TableModelEffortImport tmForImportedEfforts;

    private AjaxButton importData;

    private DropDownChoice<SelectOption> ddDate;
    private DropDownChoice<SelectOption> ddStarttime;
    private DropDownChoice<SelectOption> ddEndtime;
    private DropDownChoice<SelectOption> ddDuration;
    private DropDownChoice<SelectOption> ddDescription;
    private List<DropDownChoice<SelectOption>> listOfDropDowns = new ArrayList<DropDownChoice<SelectOption>>();

    private FileChooserForm ajaxSimpleUploadForm;
    private File csvFile = null;

    private SelectOption selectedEffortOptions;

    private List<SelectOption> selectOptions = Arrays.asList(new SelectOption[] {
            new SelectOption("nochoice", "  "), new SelectOption("column1", "Column 1"),
            new SelectOption("column2", "Column 2"), new SelectOption("column3", "Column 3"),
            new SelectOption("column4", "Column 4"), new SelectOption("column5", "Column 5") });

    public EffortImportCSVPage() {

        addFileChooserForm();

        form = new Form<Object>("choiceForImportEffortOptions");
        form.setOutputMarkupId(true);
        displayDropDownChoices();
        displayButtons();
        add(form);

        // initialise tables
        loadTableValues();
        tmForTiTAProject = new TableModelTiTAProject(titaProjectList);
        tmForImportedEfforts = new TableModelEffortImport(effortResult);
        displayTables();
    }

    /**
     * Displays the buttons for the target/actual comparison.
     */
    public void displayButtons() {
        importData = new AjaxButton("btnEffortImport", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {



                if (tableForTiTAProject.getSelectedRows().length > 0 && csvFile != null) {
                    TiTAProject titaProject = (TiTAProject) tableForTiTAProject.getTableModel()
                            .getValueAt(tableForTiTAProject.getSelectedRows()[0],
                                    IntegerConstants.FIFTY);

                    CellProcessor[] processors = new CellProcessor[] { new ParseDate("dd.MM.yyyy"),
                            null, new ParseLong(), new ParseDate("HH:mm:ss"),
                            new ParseDate("HH:mm:ss") };

                    String[] header = new String[] { "date", "description", "duration",
                            "startTime", "endTime" };

                    String[] testHeader = getDropDownValues(listOfDropDowns);
                    CellProcessor[] testProcessors = getCellProcessors(testHeader);

                    try {
                        List<Effort> importedEfforts = importEffortData(csvFile.getAbsolutePath(),
                                testHeader, testProcessors, titaProject);

                        tmForImportedEfforts.reload(effortResult);
                        tableForImportedEfforts.setVisible(true);
                        target.addComponent(tableForImportedEfforts);

                    } catch (PersistenceException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };

        importData.setVisible(true);

        add(importData);
    }

    /**
     * Displays the drop down choices.
     */
    public void displayDropDownChoices() {

        // set select options
        selectedEffortOptions = new SelectOption("nochoice", "  ");
        ChoiceRenderer<SelectOption> choiceRenderer = new ChoiceRenderer<SelectOption>("value",
                "key");

        ddDate = new DropDownChoice<SelectOption>("dropdownDate", new PropertyModel<SelectOption>(
                this, "selectedEffortOptions"), selectOptions, choiceRenderer);

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

        listOfDropDowns.add(ddDate);
        form.add(new Label("labelDate", "Date:"));
        form.add(ddDate);

        listOfDropDowns.add(ddStarttime);
        form.add(new Label("labelStarttime", "Starttime:"));
        form.add(ddStarttime);

        listOfDropDowns.add(ddEndtime);
        form.add(new Label("labelEndtime", "Endtime:"));
        form.add(ddEndtime);

        listOfDropDowns.add(ddDuration);
        form.add(new Label("labelDuration", "Duration:"));
        form.add(ddDuration);

        listOfDropDowns.add(ddDescription);
        form.add(new Label("labelDescription", "Description:"));
        form.add(ddDescription);

        addDropDownBehavior(listOfDropDowns);
    }

    /**
     * Displays the tables.
     */
    public void displayTables() {
        tableForTiTAProject = new Table("effortImportTableForTiTAProject", tmForTiTAProject) {
            @Override
            protected void onSelection(AjaxRequestTarget target) {
                if (!(tableForTiTAProject.getSelectedRows()[0] == tmForTiTAProject.getSelectedRow())) {
                    tmForTiTAProject.setSelectedRow(tableForTiTAProject.getSelectedRows()[0]);
                    tmForTiTAProject.reload();

                }
            }
        };

        tableForTiTAProject.setWidths(new String[] { "150", "150", "150" });
        tableForTiTAProject.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(tableForTiTAProject);

        tableForImportedEfforts = new Table("effortImportTableForResults", tmForImportedEfforts) {
            @Override
            protected void onSelection(AjaxRequestTarget target) {
                if (!(tableForImportedEfforts.getSelectedRows()[0] == tmForImportedEfforts
                        .getSelectedRow())) {
                    tmForImportedEfforts
                            .setSelectedRow(tableForImportedEfforts.getSelectedRows()[0]);
                    tmForImportedEfforts.reload();

                }
            }
        };

        tableForImportedEfforts.setWidths(new String[] { "95", "300", "80", "80", "78" });
        tableForImportedEfforts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableForImportedEfforts.setVisible(false);
        add(tableForImportedEfforts);
    }


    /**
     * Method to set the behavior for each dropDownChoice.
     *
     * @param dropDownChoiceList
     *            - a list of dropDownChoices.
     */
    public void addDropDownBehavior(List<DropDownChoice<SelectOption>> dropDownChoiceList) {
        for (DropDownChoice<SelectOption> dropDownChoice : dropDownChoiceList) {
            dropDownChoice.setOutputMarkupId(true);
            dropDownChoice.setNullValid(false);
            dropDownChoice.setConvertedInput(selectedEffortOptions);

            dropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    // importData.setVisible(checkDropDownChoiceValues(listOfDropDowns));
                    target.addComponent(importData);
                }
            });
        }
    }

    /**
     * Check the chosen values for the dropDownChoices to set the import Button
     * visible.
     *
     * @param dropDownChoiceList
     *            - a list of dropDownChoices.
     * @return true, if every dropDwon has set a value, else false.
     */
    public boolean checkDropDownChoiceValues(List<DropDownChoice<SelectOption>> dropDownChoiceList) {
        for (DropDownChoice<SelectOption> d : dropDownChoiceList) {
            String o = "";
            if (d.getConvertedInput() != null) {
                o = d.getConvertedInput().getKey();
            } else {
                o = d.getModel().getObject().getKey();
            }
            if ("  ".equals(o)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the selected order to map the data from the csv file to tita.
     *
     * @param dropDownChoiceList
     *            a list of dropDownChoices
     * @return the order of the data in the csv file as String array.
     */
    public String[] getDropDownValues(List<DropDownChoice<SelectOption>> dropDownChoiceList) {

        // CHECKSTYLE:OFF
        String[] headers = new String[5];

        int i = 0;
        int counter = 0;
        String header = "";
        for (DropDownChoice<SelectOption> d : dropDownChoiceList) {

            if (counter == 0) {
                header = "date";
            }

            if (counter == 1) {
                header = "startTime";
            }

            if (counter == 2) {
                header = "endTime";
            }

            if (counter == 3) {
                header = "duration";
            }

            if (counter == 4) {
                header = "description";
            }

            String key = d.getConvertedInput().getKey();
            i = Integer.valueOf(key.substring(key.length() - 1, key.length())) - 1;
            headers[i] = header;
            counter++;

        }
        // CHECKSTYLE:ON

        return headers;
    }

    /**
     * Get cell processors for chosen headers.
     *
     * @param headers
     *            the selected order
     * @return the order for the cell processors
     */
    public CellProcessor[] getCellProcessors(String[] headers) {
        // CHECKSTYLE:OFF
        CellProcessor[] processors = new CellProcessor[5];
        // CHECKSTYLE:ON
        for (int i = 0; i < headers.length; i++) {

            if (headers[i].equals("date")) {
                processors[i] = new ParseDate("dd.MM.yyyy");
            }

            if (headers[i].equals("startTime")) {
                processors[i] = new ParseDate("HH:mm:ss");
            }

            if (headers[i].equals("endTime")) {
                processors[i] = new ParseDate("HH:mm:ss");
            }

            if (headers[i].equals("duration")) {
                processors[i] = new ParseLong();
            }

            if (headers[i].equals("description")) {
                processors[i] = null;
            }
        }

        return processors;
    }

    /**
     * Load the tita projects for the table.
     */
    private void loadTableValues() {
        try {
            titaProjectList = titaProjectService.findTiTAProjectsForUser(userService
                    .getUserByUsername(TitaSession.getSession().getUsername()));

            if (tmForTiTAProject != null) {
                tmForTiTAProject.reload(titaProjectList);
            }

        } catch (PersistenceException e) {
            e.getMessage();
        }
    }

    /**
     * Imports the efforts to the database.
     *
     * @param csvPath
     *            - path to the csv file as String
     * @param header
     *            - the header of the table
     * @param processors
     *            - the columns
     * @param titaProject
     *            - the tita project to set the efforts
     * @return list of imported efforts
     *
     * @throws PersistenceException
     *             titaDao
     * @throws IOException
     *             io
     */
    public List<Effort> importEffortData(String csvPath, String[] header,
            CellProcessor[] processors, TiTAProject titaProject) throws PersistenceException,
            IOException {

        // for example header and processors see CSVReaderTest

        TiTAUser user = userService.getUserByUsername(TitaSession.getSession().getUsername());

        // find existing default-Task

        TiTATask task = new TiTATask("importingEfforts", user, titaProject, new HashSet<Effort>());
        titaProjectService.saveTiTATask(task);
        List<Effort> a = reader.importEffortData(csvPath, header, processors, task, user);
        return a;
    }

    /**
     * Add the file chooser form to the page.
     */
    public void addFileChooserForm() {

        // Add upload form with ajax progress bar
        ajaxSimpleUploadForm = new FileChooserForm("ajax-csv-chooser");
        ajaxSimpleUploadForm.add(new UploadProgressBar("progressBarForCSVChooser",
                ajaxSimpleUploadForm));
        add(ajaxSimpleUploadForm);
    }

    /**
     * Form for chosing csv files.
     */
    private class FileChooserForm extends Form<Void> {
        private static final int C_EXTENTION_LENGTH = 3;
        private static final int C_UPLOAD_MAX_SIZE = 10000;
        private FileUploadField fileChooser;

        private Label lblFeedbackForLoadedFile;
        private String messageForlblFeedbackForLoadedFile = "";

        /**
         * Constructor.
         *
         * @param name
         *            Component name
         */
        public FileChooserForm(String name) {
            super(name);

            Folder uploadFolder = getUploadFolder();

            lblFeedbackForLoadedFile = new Label("lblFeedbackForLoadedFile",
                    new PropertyModel<String>(this, "messageForlblFeedbackForLoadedFile"));
            lblFeedbackForLoadedFile.setVisible(false);
            lblFeedbackForLoadedFile.setOutputMarkupId(true);

            // Add feedback label
            add(lblFeedbackForLoadedFile);

            // set this form to single part mode
            setMultiPart(false);

            // Add one file input field
            fileChooser = new FileUploadField("fileInput");
            add(fileChooser);

            // Set maximum size to 10000K
            setMaxSize(Bytes.kilobytes(C_UPLOAD_MAX_SIZE));
        }

        /**
         * Upload File.
         *
         */
        @Override
        protected void onSubmit() {

            // Delete already uploaded files
            ((TiTAApplication) Application.get()).getUploadFolder().removeFiles();

            final FileUpload upload = fileChooser.getFileUpload();
            if (upload != null) {

                // Create a new file
                File newFile = new File(getUploadFolder(), upload.getClientFileName());
                csvFile = newFile;

                // Check new file, delete if it already existed
                checkFileExists(newFile);

                // String extention =
                // newFile.getName().substring(newFile.getName().length() -
                // C_EXTENTION_LENGTH,
                // newFile.getName().length());

                if (newFile.getName().endsWith("csv")) {
                    try {
                        // Save to new file
                        newFile.createNewFile();
                        upload.writeTo(newFile);

                        setMessageForlblFeedbackForLoadedFile(upload.getClientFileName());
                        lblFeedbackForLoadedFile.setVisible(true);

                        // EffortImportCSVPage.this.info("saved file: " +
                        // upload.getClientFileName());
                    } catch (Exception e) {
                        throw new IllegalStateException("Unable to write file");
                    }
                } else {
                    messageForlblFeedbackForLoadedFile = "No CSV File!";
                    lblFeedbackForLoadedFile.setVisible(true);
                }
            }
        }

        public void setMessageForlblFeedbackForLoadedFile(String messageForlblFeedbackForLoadedFile) {
            this.messageForlblFeedbackForLoadedFile = messageForlblFeedbackForLoadedFile;
        }

        public String getMessageForlblFeedbackForLoadedFile() {
            return messageForlblFeedbackForLoadedFile;
        }
    }

    /**
     * Check whether the file already exists, and if so, try to delete it.
     *
     * @param newFile
     *            the file to check
     */
    private void checkFileExists(File newFile) {
        if (newFile.exists()) {
            // Try to delete the file
            if (!Files.remove(newFile)) {
                throw new IllegalStateException("Unable to overwrite " + newFile.getAbsolutePath());
            }
        }
    }

    /**
     * Get temp folder for uploads.
     *
     * @return the folder
     */
    private Folder getUploadFolder() {
        return ((TiTAApplication) Application.get()).getUploadFolder();
    }

}
