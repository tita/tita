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

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.StrMinMax;
import org.supercsv.cellprocessor.constraint.Unique;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import at.ac.tuwien.ifs.tita.business.service.time.EffortService;
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;

public class CSVReader implements IImportReader{

    private EffortService effortService;
    
    public CSVReader(EffortService effortService){
        this.effortService = effortService;
    }
    @Override
    public void importEffortData(String path, String[] header,CellProcessor[] processors, 
            TiTATask task, TiTAUser user) throws IOException, TitaDAOException {
        ICsvBeanReader inFile = new CsvBeanReader(new FileReader(path), CsvPreference.EXCEL_PREFERENCE);
        try {
          EffortBean effortBean;
          while( (effortBean = inFile.read(EffortBean.class, header, processors)) != null) {
            Effort effort = new Effort(task, null, effortBean.getDate(), 
                    effortBean.getStartTime(), effortBean.getEndTime(), effortBean.getDuration(),
                    effortBean.getDescription(), false, user);
            effortService.saveEffort(effort);
            
            System.out.println(effortBean.getDate());
          }
        } finally {
          inFile.close();
        }

        
    }

}
