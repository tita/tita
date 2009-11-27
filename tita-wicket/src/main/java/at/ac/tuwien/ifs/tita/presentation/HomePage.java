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

import java.util.ArrayList;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;

import at.ac.tuwien.ifs.tita.datasource.domain.Role;
import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.datasource.service.IUserService;
import at.ac.tuwien.ifs.tita.presentation.timeeffort.TimeEffortAdministrationPanel;
import at.ac.tuwien.ifs.tita.reporting.JasperPdfResource;


/**
 * Homepage of Hello World Application.
 */
public class HomePage extends WebPage {

    @SpringBean(name = "userService")
    private IUserService service;

    @SpringBean(name = "helloWorldReport")
    private JasperPdfResource helloWorldReport;
    

    public HomePage() {
        displayHello();
        
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
            // initReport(helloWorld);
            // add(new Label("message", helloWorld.getDescription()));
            add(new TimeEffortAdministrationPanel("timeEffortPanel"));
            // add(new JasperResourceLink("linkToPdf", helloWorldReport));
            // add(new ResourceLink("linkToPdf", helloWorldReport));
        } catch (TitaDAOException e) {
            // add(new Label("message", "Couldn't read data from DB."));
        }/*
          * catch (JRException e) { // add(new Label("message",
          * "Couldn't generate pdf file.")); e.printStackTrace(); }
          */
    }
    

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
     * 
     * @param r Role
     * @exception JRException on runtime error
     */
    private void initReport(Role r) throws JRException {
        ServletContext context = ((WebApplication) getApplication()).getServletContext();

        ArrayList<Role> roles = new ArrayList<Role>();
        roles.add(r);

        helloWorldReport.loadReport(context.getRealPath(helloWorldReport.getDesignFilename()));
        helloWorldReport.setReportDataSource(new JRBeanCollectionDataSource(roles));
    }
}
