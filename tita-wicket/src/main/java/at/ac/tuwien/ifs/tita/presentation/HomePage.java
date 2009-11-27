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
package at.ac.tuwien.ifs.tita.presentation;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.mantisbt.connect.AccessLevel;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.axis.MCSession;
import org.mantisbt.connect.model.IIssue;
import org.mantisbt.connect.model.IProject;
import org.mantisbt.connect.model.Issue;
import org.mantisbt.connect.model.MCAttribute;
import org.mantisbt.connect.model.Project;

import at.ac.tuwien.ifs.tita.datasource.domain.Role;
import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.datasource.service.IUserService;
import at.ac.tuwien.ifs.tita.reporting.JasperPdfResource;
import at.ac.tuwien.ifs.tita.reporting.JasperResourceLink;


/**
 * Homepage of Hello World Application.
 */
public class HomePage extends WebPage {

    @SpringBean(name = "userService")
    private IUserService service;
    @SpringBean(name = "helloWorldReport")
    private JasperPdfResource helloWorldReport;
    
    private MCSession session;
    private String url = "http://localhost/mantisbt-1.1.8/api/soap/mantisconnect.php";
    private String user = "administrator";
    private String pwd = "root";


    public HomePage() {
        displayHello();
        
        
        try {
            URL u = new URL(url);
            session = new MCSession(u, user, pwd);
        } catch (MCException e) {
            assertTrue(false);
        } catch (MalformedURLException e) {
            assertTrue(false);
        }
        //displayIssueTable();
    }

    /**
     * Puts "Hello World" in DB and reads it afterwards and generates link to
     * PDF File.
     */
    private void displayHello() {
        Role helloWorld = initPage();

        try {
            Role temp = service.saveRole(helloWorld);
            helloWorld = null;
            helloWorld = service.getRoleById(temp.getId());
            initReport(helloWorld);
            add(new Label("message", helloWorld.getDescription()));
            add(new JasperResourceLink("linkToPdf", helloWorldReport));
        } catch (TitaDAOException e) {
            add(new Label("message", "Couldn't read data from DB."));
        } catch (JRException e) {
            add(new Label("message", "Couldn't generate pdf file."));
        }
    }
    
    /*private void displayIssueTable(){
        long taskId = 0;
        long projectId = 0;
        try {
            projectId = createTestProject("tita_test", "tita_test_description", true, false);
            taskId = createTestTask("tita_test_issue1", "issue_summary1","tita_test" );

            IssueTrackerMantisDao dao = new IssueTrackerMantisDao();
            List<IIsTaskTrackable> mantisTask = dao.findAllTasksForProject(projectId);
            
            final DataView dataView = new DataView("simple", new ListDataProvider(
                    list)) {
                public void populateItem(final Item item) {
                    final IIssue user = (IIssue) item.getModelObject();
                 
                    item.add(new Label("id", Integer.())));
                }
            };

             dataView.setItemsPerPage(10);
            
            add(dataView);

            add(new PagingNavigator("navigator", dataView));

        
        } catch (Exception e) {
            assertTrue(false);
        } finally{
            deleteTestTask(taskId);
            deleteTestProject("tita_test");   
        }
    }
*/
    /**
     * Initialize homepage data.
     * 
     * @return Role "Hello World"
     */
    private Role initPage() {
        Role r = new Role();
        r.setDescription("Hello World.");
        return r;
    }

    /**
     * Loads report and initializes parameters for report.
     * @param r Role 
     * @exception JRException on runtime error
     */
    private void initReport(Role r) throws JRException {
        ServletContext context = ((WebApplication) getApplication()).getServletContext();
        File reportFile = new File(context.getRealPath(helloWorldReport.getDesignFilename()));

        ArrayList<Role> roles = new ArrayList<Role>();
        roles.add(r);

        helloWorldReport.loadReport(reportFile);
        helloWorldReport.setReportDataSource(new JRBeanCollectionDataSource(roles));
    }
    
    /**
     * Creates a Project on the Mantis-Server.
     * @param projectName - name of the project
     * @param description - description of the project
     * @param enabled - status of the project
     * @param viewStatePrivate - private or public
     * @return id of the created project
     * @throws MCException - if error occurs, when project is added
     */
    private long createTestProject(String projectName, String description, boolean enabled,
            boolean viewStatePrivate) throws MCException{
        
        IProject newProject = new Project();
        newProject.setName(projectName);
        newProject.setAccessLevelMin(AccessLevel.DEVELOPER);
        newProject.setDesription(description);
        newProject.setEnabled(enabled); //ProjectStatus: Open
        newProject.setPrivate(viewStatePrivate); //ViewState:Public
        long id = session.addProject(newProject);
        session.flush();
        return id;
    }
    
    /**
     * Creates a task on the Mantis-Server.
     * @param description - description of the project
     * @param summary - summary of the project
     * @param projectName - name of the project of the task
     * @return id of the created task
     * @throws MCException - if error occurs, when task is added
     */
    private long createTestTask(String description, String summary, String projectName)
        throws MCException{
        
        IIssue newIssue = new Issue();
        newIssue.setDescription(description);
        //newIssue.setHandler(new Account(100, "test", "test", "test@test"));
        newIssue.setPriority(session.getDefaultIssuePriority());
        newIssue.setSummary(summary);
        newIssue.setSeverity(session.getDefaultIssueSeverity());
        //newIssue.setReporter(new Account(101, "rep1", "rep1", "rep@rep"));  
        IProject p = session.getProject(projectName);
        newIssue.setProject(new MCAttribute(p.getId(), p.getName()));
        long id = session.addIssue(newIssue);
        session.flush();
        return id;
    }

    /**
     * Deletes project on the Mantis-Server.
     * @param projectName - name of the project to delete
     */
    private void deleteTestProject(String projectName){
        IProject old;
        try {
            old = session.getProject(projectName);
            if(old != null){
                session.deleteProject(old.getId());
                session.flush();
            }
        } catch (MCException e) {
            assertTrue(false);
        }
    }
    /**
     * Deletes task on the Mantis-Server.
     * @param taskId - id of the task to delete
     */
    private void deleteTestTask(long taskId) {
        try {
            session.deleteIssue(taskId);
            session.flush();
        } catch (MCException e) {
            assertTrue(false);
        }
    }
}
