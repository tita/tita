package at.ac.tuwien.ifs.tita.common.test.csv;

import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.StrMinMax;
import org.supercsv.cellprocessor.ift.CellProcessor;

import at.ac.tuwien.ifs.tita.business.csv.CSVReader;
import at.ac.tuwien.ifs.tita.business.csv.IImportReader;
import at.ac.tuwien.ifs.tita.business.service.time.EffortService;
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;

public class CSVReaderTest {

    @Test
    public void testImportCSV() throws IOException, TitaDAOException{
        CellProcessor[] processors = new CellProcessor[] {
                new ParseDate("yyyy-MM-dd"),
                null,
                new ParseDate("dd/MM/yyyy"),
                new Optional(new ParseInt()),
                null
            };
        EffortService service = new EffortService();
        IImportReader reader = new CSVReader(service);
        TiTATask t1 = new TiTATask();
        TiTAUser u1 = new TiTAUser();
        String path = 
            new ClassPathResource(".", CSVReaderTest.class).getFile().getAbsolutePath() +
            "/CSVReaderTest.csv";
        
        String[] header = new String[]{"date", "description", "duration", 
                "startTime", "endTime"};
        
        reader.importEffortData(path, header, processors, t1, u1);
        
    }
}
