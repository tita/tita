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
package at.ac.tuwien.ifs.tita.business.csv;

import java.io.IOException;

import javax.persistence.PersistenceException;

import org.supercsv.cellprocessor.ift.CellProcessor;

import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;

/**
 * The interface for the importing entities.
 *
 * @author Christoph
 *
 */
public interface IImportReader {

    /**
     * Imports Efforts from a File.
     * 
     * @param path
     *            - file path
     * @param header
     *            - values to read in the right order
     * @param task
     *            - Task to add the efforts
     * @param user
     *            - the user, the efforts are for
     * @param processors
     *            - processors
     * @throws PersistenceException
     *             titaDao
     * @throws IOException
     *             io
     */
    void importEffortData(String path, String[] header, CellProcessor[] processors, TiTATask task,
            TiTAUser user) throws IOException, PersistenceException;
}
