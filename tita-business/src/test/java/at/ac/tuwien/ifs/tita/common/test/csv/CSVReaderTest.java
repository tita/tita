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
package at.ac.tuwien.ifs.tita.common.test.csv;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.ift.CellProcessor;

import at.ac.tuwien.ifs.tita.business.csv.CSVReader;
import at.ac.tuwien.ifs.tita.business.csv.IImportReader;
import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasourceContext-test.xml" })
@TransactionConfiguration
@Transactional
public class CSVReaderTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private IEffortService service;
    
    @Test
    public void testImportCSV() throws IOException, TitaDAOException{
        CellProcessor[] processors = new CellProcessor[] {
                new ParseDate("dd.MM.yyyy"),
                null,
                new ParseLong(),
                new ParseDate("HH:mm:ss"),
                new ParseDate("HH:mm:ss")
            };
        
        IImportReader reader = new CSVReader(service);
        TiTAUser u1 = new TiTAUser();
        TiTATask t1 = new TiTATask("testtask", u1, new TiTAProject(), new HashSet<Effort>());
        
        String path = 
            new ClassPathResource(".", CSVReaderTest.class).getFile().getAbsolutePath() +
            "/CSVReaderTest.csv";
        
        String[] header = new String[]{"date", "description", "duration", 
                "startTime", "endTime"};
        
        reader.importEffortData(path, header, processors, t1, u1);
        
        assertEquals("Three Efforts were imported", 3, t1.getTitaEfforts().size());
        
        assertEquals("Three Efforts were imported", 3, service.getActualEfforts(10).size());
    }
}
