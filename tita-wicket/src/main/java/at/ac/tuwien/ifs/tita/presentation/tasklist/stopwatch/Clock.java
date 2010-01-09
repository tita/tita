/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.ac.tuwien.ifs.tita.presentation.tasklist.stopwatch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * A simple component that displays current time
 * 
 * @author Igor Vaynberg (ivaynberg)
 */
public class Clock extends Label {
    private ClockModel cm;
    /**
     * Constructor
     * 
     * @param id
     *            Component id
     * @param tz
     *            Timezone
     */
    public Clock(String id, Calendar cal) {
        super(id);
        cm = new ClockModel(cal);
        this.setDefaultModel(cm);
    }
       
    /**
     * A model that returns current time in the specified timezone via a
     * formatted string
     * 
     * @author Igor Vaynberg (ivaynberg)
     */
    private static class ClockModel implements IModel<String> {
        private SimpleDateFormat sdf;

        /**
         * @param tz
         */
        public ClockModel(Calendar cal) {
            sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            sdf.setCalendar(cal);
        }

        /**
         * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
         */
        @Override
        public String getObject() {
            return sdf.format(new Date());
        }

        @Override
        public void setObject(String object) {
            try {
                Date dat = sdf.parse(object);
                Calendar cal = Calendar.getInstance();
                cal.setTime(dat);
                sdf.setCalendar(cal);
            } catch (ParseException e) {
                //should not happen
            }
        }

        @Override
        public void detach() {
        }
    }
    
    public String getDate(){
        return cm.getObject();
    }
    
    public void setDate(String object){
        cm.setObject(object);
    }
}