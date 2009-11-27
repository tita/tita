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

package at.ac.tuwien.ifs.tita.reporting;

import org.apache.wicket.IResourceListener;
import org.apache.wicket.Resource;
import org.apache.wicket.markup.html.link.Link;

/**
 * Class for adding a JasperReports ResourceLink to a Webpage.
 */
public class JasperResourceLink extends Link<Object> implements IResourceListener {
    /** resource. */
    private final transient Resource resource;

    /**
     * Constructs.
     * 
     * @param id id of Component
     * @param resource resource
     */
    public JasperResourceLink(String id, Resource resource) {
        super(id);
        this.resource = resource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick() {
    }

    /**
     * {@inheritDoc}
     */
    public void onResourceRequested() {
        resource.onResourceRequested();
        onClick();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final CharSequence getURL() {
        return urlFor(IResourceListener.INTERFACE);
    }
}
