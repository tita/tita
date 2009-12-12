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
import java.io.FileInputStream;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.xml.XmlConfiguration;

/**
 * Class which automatically startsup the server and removed code duplication
 * from the different projects.
 */
public class JettyStartManager {

    private String contextPath;
    private String war;
    private File env;

    public JettyStartManager(final String contextPath, final String war,
            final File env) {
        super();
        this.contextPath = contextPath;
        this.war = war;
        this.env = env;
    }

    /**
     * Starts the server.
     * @throws Exception e
     */
    public void start() throws Exception {
        final Server server = new Server();
        final SocketConnector connector = new SocketConnector();

        //CHECKSTYLE:OFF
        // Set some timeout options to make debugging easier.
        connector.setMaxIdleTime(1000 * 60 * 60);
        connector.setSoLingerTime(-1);
        connector.setPort(8090);
        server.setConnectors(new Connector[] { connector });

        final WebAppContext bb = new WebAppContext();
        bb.setServer(server);
        bb.setContextPath(this.contextPath);
        bb.setWar(this.war);
        final XmlConfiguration configuration = new XmlConfiguration(
                new FileInputStream(this.env));
        configuration.configure(bb);

        // START JMX SERVER
        // MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        // MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
        // server.getContainer().addEventListener(mBeanContainer);
        // mBeanContainer.start();

        server.addHandler(bb);

        try {
            System.out
                    .println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
            server.start();
            System.in.read();
            System.out.println(">>> STOPPING EMBEDDED JETTY SERVER");
            server.stop();
            server.join();
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
        //CHECKSTYLE:ON
    }

}
