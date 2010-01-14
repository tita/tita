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

package at.ac.tuwien.ifs.tita.dao.test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility Class for reading files containing SQL-commands.
 * 
 *
 */
public class SQLFileParser {
    private static Log log = LogFactory.getLog(SQLFileParser.class);

    public SQLFileParser() {
        super();
    }

    /**
     * Reads SQL commands from a file into a List.
     * Name of the file is the name of the class aClass
     * + default extension ".sql".
     * This file must reside in the same package as the Java file.
     * @param aClass a Java class
     * @throws Exception on File Errors
     * @return a Java List or null
     */
    public List<String> readSqlFile(Object aClass) throws Exception {
        InputStream instr = getInputStreamResource(aClass, 
                ".sql", false);
        return readSqlFileStream(instr);
    }
    
    /**
     * Reads SQL commands from a file into a List.
     * Name of the file is the name of the parameter aClass
     * + parameter filePostfix.
     * This file must reside in the same package as the Java file.
     * @param aClass a Java class
     * @param filePostfix  Filename after ClassName including extension (eq "_test1.sql")
     * @param noClassName boolean
     * @throws Exception on File Errors
     * @return a Java List or null
     */
    public List<String> readSqlFile(Object aClass, String filePostfix, boolean noClassName) 
        throws Exception {
        InputStream instr = getInputStreamResource(aClass, 
              filePostfix, noClassName);
        return readSqlFileStream(instr);
    }
   
    
    /**
     * Reads SQL commands from a file into a List.
     * @param fullFileName the complete (full path) filename.
     * @throws Exception when file errors occur
      * @return a Java List or null
    */
    public List<String> readSqlFile(String fullFileName) throws Exception {
        File f = new File(fullFileName);
        if (f.isFile()) {
            log.info("Reading SQL Statements (as FileInputStream) from " + fullFileName);
            FileInputStream str = new FileInputStream(fullFileName);
            return readSqlFileStream(str);
        } else {
            throw new IllegalArgumentException(
                    "File "+f.getCanonicalPath()+" not found!");
        }

    }
    /**
     * Reads SQL-commands from an input stream into a List.
     * The lines read are parsed in the following way:
     * Any comment lines beginning with "--" are ignored.
     * Comments at the end of the lines are removed.
     * When there is no SQL termination character ";" found on a line,
     * the next line(s) are read and concatenated until the termination
     * character is found. The concatenated line is then readd.
     * @param instr a File input stream
     * @throws Exception on IO-Errors
      * @return a Java List or null
     */
    public List<String> readSqlFileStream(InputStream instr) throws Exception {
        BufferedReader input = null;
        StringBuffer buf = null;
        List<String> sqlList = new ArrayList<String>();
        if (instr != null) {
            try {
                String line = null;
                // Open stream and start reading.
                input = new BufferedReader(new InputStreamReader(instr));
                while ((line = input.readLine()) != null) {
                    line = line.trim();
                    
                    // Check for comment and empty lines.
                    if (!(line.startsWith("--") || line.startsWith("//") ) && 
                        (line.length() > 0)) {
                        if (buf == null) {
                            buf = new StringBuffer();
                        }
                        // Search for comments at the end of the line
                        int i = line.indexOf("--");
                        if (i > 0) {
                            // Remove comment
                            line = line.substring(0, i);
                            line = line.trim();
                        }
                        // Search for comments at the end of the line
                        // and statement is not an insert
                        i = line.indexOf("//");
                        if (i > 0) {
                            if(!isSlashInCharString(line,i)){
                                // Remove comment
                                line = line.substring(0, i);
                                line = line.trim();
                            }
                        }
                        
                        
                        // Save line if the command continues on the next line.
                        buf.append(line);
                        
                        if (line.endsWith(";")) {
                            // Statement ready to read.
                            String statement = buf.toString();
                            statement = statement.substring(0,statement.length()-1);
                            sqlList.add(statement);
                            buf = null;
                        } else {
                            // Since the lines are trimmed we have to add a space.
                            buf.append(" ");
                        }
                    }
                }
            } finally {
                // Try to close the stream.
                if (input != null) {
                    input.close();
                }
            }

        } else {

            throw new IllegalArgumentException(
                    "readSqlFile: Argument instr null!");
        }
        return sqlList;
    }
    /**
     * Returns an InputStream.
     * The name of the input stream is composed out of the name of the
     * Object o together with the extension ext.
     * @param o a Java Object
     * @param ext a file extension
     * @param noClassName Boolean 
     * @return InputStream
     */
    private InputStream getInputStreamResource(Object o,String ext, boolean noClassName) {
        String className = o.getClass().getSimpleName();
        String fName = ((noClassName)?"":className) + ext;
        log.info("Reading SQL Statements (as ResourceStream) from " + fName);
        InputStream instr = o.getClass().getResourceAsStream(fName);
        if (instr == null) {
            throw new IllegalArgumentException(
                    "Resource "+fName+" not found!");
        }
        return instr;
    }

    /**
     * Returns if // is content of a char string.
     * @param line String
     * @param slashIndex Integer
     * @return true, if contained; otherwise false
     */
    private Boolean isSlashInCharString(String line, Integer slashIndex){
        if(slashIndex > line.indexOf("'") && slashIndex < line.lastIndexOf("'")){
            return true;
        }
        return false;
    }
}
