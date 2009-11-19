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

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
 * Implementation of pdf resource type.
 * 
 * @author rene
 * 
 */
public class JasperPdfResource extends JasperResource {

    /**
     * Default Construct.
     * 
     */
    public JasperPdfResource() {

    }

    /**
     * Returns file extension of the resource.
     * 
     * @see JasperResource#getFileExtension()
     * @return file extension as string
     */
    @Override
    public String getFileExtension() {
        return ".pdf";
    }

    /**
     * Returns content type of the resource.
     * 
     * @see JasperResource#getContentType()
     * @return content type as string
     */
    @Override
    public String getContentType() {
        return "application/pdf";
    }

    /**
     * Creates a PdfExporter.
     * 
     * @see JasperResource#createExporter()
     * @return JRPdfExporter instance
     */
    @Override
    public JRAbstractExporter createExporter() {
        return new JRPdfExporter();
    }
}
