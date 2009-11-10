package at.ac.tuwien.ifs.tita.reporting;

import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.wicket.markup.html.DynamicWebResource;


/**
* Base Class for different Resources
* @author rene
*
*/
public abstract class JasperResource extends DynamicWebResource {
	
	/**
	 * compiled report.
	 */
	private JasperReport jasperReport;
	/**
	 * datasource of report.
	 */
	private JRDataSource reportDataSource;
	/**
	 * report parameters.
	 */
	private Map<String, String> params;
	
	/**
	 * Creates an exporter for a resource type.
	 * 
	 * @return exporter instance
	 */
	protected abstract JRAbstractExporter createExporter();
	
	/**
	 * 
	 * Fills the report with the applied data source and parameter.
	 * 
	 * @return jasperprint instance
	 * @throws JRException
	 */
	protected JasperPrint createJasperPrint() throws JRException
	{
		return new JasperPrint();
	}
	
	/**
	 * Gets the Content of a resource type.
	 * @return
	 */
	public abstract String getContentType();
	
	/**
	 * @see org.apache.wicket.markup.html.DynamicWebResource#getResourceState()
	 */
	protected ResourceState getResourceState()
	{
		final byte[] data = new byte[256];
	
		return new ResourceState() {
            public String getContentType() {
               return "application/pdf";
            }

            public byte[] getData() {
            	return data;
            }
            
			public int getLength()
			{
				return data.length;
			}
         };
	}

	/**
	 * @return the jasperReport
	 */
	public JasperReport getJasperReport() {
		return jasperReport;
	}

	/**
	 * @param jasperReport the jasperReport to set
	 */
	public void setJasperReport(JasperReport jasperReport) {
		this.jasperReport = jasperReport;
	}
	
	/**
	 * @return the reportDataSource
	 */
	public JRDataSource getReportDataSource() {
		return reportDataSource;
	}

	/**
	 * @param reportDataSource the reportDataSource to set
	 */
	public void setReportDataSource(JRDataSource reportDataSource) {
		this.reportDataSource = reportDataSource;
	}

	/**
	 * @return the params
	 */
	public Map<String, String> getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
