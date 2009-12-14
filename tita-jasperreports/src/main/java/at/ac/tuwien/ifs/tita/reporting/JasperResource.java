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
package at.ac.tuwien.ifs.tita.reporting;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.protocol.http.WebResponse;

/**
 * Base Class for different Resources.
 * 
 * @author rene
 * 
 */
public abstract class JasperResource extends DynamicWebResource {

    /** compiled report. */
    private JasperReport jasperReport;
    /** data source of report. */
    private JRDataSource reportDataSource;
    /** report parameters. */
    private Map<String, String> reportParameters;
    /** report filename. */
    private String filename;
    /** compiled report filename. */
    private String designFilename;

    /**
     * Loads report from compiled report file.
     * 
     * @param filepath filepath to report file.
     * @throws JRException if file could not be loaded.
     */
    public void loadReport(String filepath) throws JRException {
        compileReport(filepath);
    }

    /**
     * Compiles report on the fly.
     * 
     * @param filepath filepath to report file.
     * @throws JRException if file could not be loaded.
     */
    private void compileReport(String filepath) throws JRException {
        setJasperReport(JasperCompileManager.compileReport(filepath));
    }

    /**
     * Fills the report with the applied data source and parameter.
     * 
     * @return JasperPrint instance
     * @throws JRException if report could not be filled.
     */
    protected JasperPrint createJasperPrint() throws JRException {
        if (reportDataSource == null) {
            setReportDataSource(new JREmptyDataSource());
        }
        if (reportParameters == null) {
            setParameters(new HashMap<String, String>());
        }
        return JasperFillManager.fillReport(getJasperReport(), getReportParameters(), getReportDataSource());
    }

    /**
     * Returns the generated resource.
     * 
     * @see org.apache.wicket.markup.html.DynamicWebResource#getResourceState()
     * @return resource as ResourceState
     */
    @Override
    protected ResourceState getResourceState() {
        ByteArrayOutputStream os = null;
        try {
            JasperPrint printer = createJasperPrint();
            JRAbstractExporter exporter = createExporter();

            os = new ByteArrayOutputStream();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, printer);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);

            exporter.exportReport();
        } catch (JRException e) {
            throw new WicketRuntimeException(e);
        }

        final byte[] data = os.toByteArray();
        return new ResourceState() {
            @Override
            public String getContentType() {
                return getFileExtension();
            }

            @Override
            public byte[] getData() {
                return data;
            }

            @Override
            public int getLength() {
                return data.length;
            }
        };
    }

    /**
     * Set Headers for file and content type.
     * 
     * @param response WebResponse
     */
    @Override
    public void setHeaders(WebResponse response) {
        super.setHeaders(response);

        String name = getFilename();
        if (name != null) {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");

        }
    }

    /**
     * Creates an exporter for a resource type.
     * 
     * @return exporter instance
     */
    protected abstract JRAbstractExporter createExporter();

    /**
     * Returns content type of resource.
     * 
     * @return content type as string.
     */
    public abstract String getContentType();

    /**
     * Returns file extension of the resource.
     * 
     * @return file extension as string
     */
    public abstract String getFileExtension();

    /**
     * Returns a compiled report.
     * 
     * @return jasperReport instance
     */
    private JasperReport getJasperReport() {
        return jasperReport;
    }

    /**
     * Sets compiled report.
     * 
     * @param jasperReport report as JasperReport
     */
    private void setJasperReport(JasperReport jasperReport) {
        this.jasperReport = jasperReport;
    }

    /**
     * Returns report data source.
     * 
     * @return the report data source as JRDataSource
     */
    public JRDataSource getReportDataSource() {
        return reportDataSource;
    }

    /**
     * Sets report data source.
     * 
     * @param reportDataSource report data source as JRDataSource
     */
    public void setReportDataSource(JRDataSource reportDataSource) {
        this.reportDataSource = reportDataSource;
    }

    /**
     * Returns report parameters.
     * 
     * @return the reportParameters report parameter as Map
     */
    public Map<String, String> getReportParameters() {
        return reportParameters;
    }

    /**
     * Sets report parameters.
     * 
     * @param parameters report parameter as Map
     */
    public void setReportParameters(Map<String, String> parameters) {
        reportParameters = parameters;
    }

    /**
     * Returns filename of report.
     * 
     * @return the filename
     */
    public String getFilename() {
        if (filename == null) {
            filename = this.getJasperReport().getName() + this.getFileExtension();
        }
        return filename;
    }

    /**
     * Sets filename of report.
     * 
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Returns filename of compiled report.
     * 
     * @return the designFilename
     */
    public String getDesignFilename() {
        return designFilename;
    }

    /**
     * sets name of compiled report.
     * 
     * @param designFilename the designFilename to set
     */
    public void setDesignFilename(String designFilename) {
        this.designFilename = designFilename;
    }
}
