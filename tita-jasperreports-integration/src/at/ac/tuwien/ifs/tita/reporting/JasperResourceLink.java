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
