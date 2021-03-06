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

package at.ac.tuwien.ifs.tita.jetty;

import java.io.File;


/**
 * Class to start the web development environment directly from code and allow
 * to debug the webapp at all looking for some special issues and show if
 * everything works as expected.
 */
public class JettyStart {

    /**
     * Starts a jetty server.
     * @param args String[]
     * @throws Exception e
     */
    public static void main(final String[] args) throws Exception {
        new JettyStartManager("/", "src/main/webapp", new File("jetty-env-mySQL.xml")).start();
    }
}
