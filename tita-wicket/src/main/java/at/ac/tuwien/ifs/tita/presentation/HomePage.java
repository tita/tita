package at.ac.tuwien.ifs.tita.presentation;

import java.io.File;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;

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
            initReport(helloWorld);
            add(new Label("message", helloWorld.getDescription()));
            add(new JasperResourceLink("linkToPdf", helloWorldReport));
        } catch (TitaDAOException e) {
            add(new Label("message", "Couldn't read data from DB."));
        } catch (JRException e) {
            add(new Label("message", "Couldn't generate pdf file."));
        }
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
     * @return JasperPdfResource instance
     */
    private void initReport(Role r) throws JRException {
        ServletContext context = ((WebApplication) getApplication()).getServletContext();
        File reportFile = new File(context.getRealPath(helloWorldReport.getDesignFilename()));

        ArrayList<Role> roles = new ArrayList<Role>();
        roles.add(r);

        helloWorldReport.loadReport(reportFile);
        helloWorldReport.setReportDataSource(new JRBeanCollectionDataSource(roles));
    }
}
