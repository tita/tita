package at.ac.tuwien.ifs.tita.reporting;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
 * Implementation of the pdf resource type
 * @author rene
 *
 */
public class JasperPdfResource extends JasperResource {
	

	public JasperPdfResource() {
		
	}
	
	/**
	 * @see JasperResource#getContentType()
	 */
	public String getContentType() {
		return "";
	}
	
	/**
	 * @see JasperResource#createExporter()
	 */
	public JRAbstractExporter createExporter() {
		return new JRPdfExporter();
	}
}
