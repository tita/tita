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

package at.ac.tuwien.ifs.tita.timeeffort.test.init;

import java.util.GregorianCalendar;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.timeeffort.domain.TimeEffort;
import at.ac.tuwien.ifs.tita.timeeffort.service.ITimeEffortService;

public class Init {

    /**
     * @param args
     *            no args
     */
    public static void main(String[] args) {

        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "timeeffort-context.xml");

        ITimeEffortService service = (ITimeEffortService) ctx
                .getBean("timeEffortService");

        GregorianCalendar cal = new GregorianCalendar();

        // init the db and save some values in it
        TimeEffort te1 = new TimeEffort();
        cal.set(2008, 10, 10);
        te1.setDate(cal.getGregorianChange());
        te1.setDeleted(false);
        te1.setDescription("Testbeschreibung 1");
        (cal = new GregorianCalendar()).set(2008, 10, 10, 10, 10, 10);
        te1.setStartTime(cal.getTime());
        (cal = new GregorianCalendar()).set(2008, 10, 10, 11, 11, 11);
        te1.setEndTime(cal.getTime());

        TimeEffort te2 = new TimeEffort();
        cal.set(2009, 10, 10);
        te2.setDate(cal.getGregorianChange());
        te2.setDeleted(false);
        te2.setDescription("Testbeschreibung 2");
        (cal = new GregorianCalendar()).set(2009, 10, 10, 10, 10, 10);
        te2.setStartTime(cal.getTime());
        (cal = new GregorianCalendar()).set(2009, 10, 10, 11, 11, 11);
        te2.setEndTime(cal.getTime());

        try {
            te1 = service.saveTimeEffort(te1);
            te2 = service.saveTimeEffort(te2);
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }

    }

}
