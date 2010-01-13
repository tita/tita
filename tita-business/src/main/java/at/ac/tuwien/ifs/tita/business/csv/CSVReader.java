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

import java.io.FileReader;
import java.io.IOException;

import javax.persistence.PersistenceException;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;

/**
 * The implementation of the import reader using a csv reader.
 *
 * @author Christoph
 *
 */
public class CSVReader implements IImportReader {

    private IEffortService effortService;

    public CSVReader(IEffortService effortService) {
        this.effortService = effortService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void importEffortData(String path, String[] header, CellProcessor[] processors,
            TiTATask task, TiTAUser user) throws IOException, PersistenceException {
        ICsvBeanReader inFile = new CsvBeanReader(new FileReader(path),
                CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

        try {
            EffortBean effortBean;
            while ((effortBean = inFile.read(EffortBean.class, header, processors)) != null) {
                Effort effort = new Effort(task, null, effortBean.getDate(), effortBean
                        .getStartTime().getTime(), effortBean.getEndTime().getTime(), effortBean
                        .getDuration(), effortBean.getDescription(), false, user);
                effortService.saveEffort(effort);
                task.getTitaEfforts().add(effort);
            }
        } finally {
            inFile.close();
        }

    }

}
