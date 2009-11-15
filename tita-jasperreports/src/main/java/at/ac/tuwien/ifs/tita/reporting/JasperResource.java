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
