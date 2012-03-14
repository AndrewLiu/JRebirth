package org.jrebirth.core.service;

import org.jrebirth.core.event.EventType;
import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.link.AbstractWaveReady;
import org.jrebirth.core.link.Wave;

/**
 * 
 * The class <strong>AsyncServiceImpl</strong>.
 * 
 * Base implementation of the service.
 * 
 * @author Sébastien Bordes
 * 
 * @version $Revision: 254 $ $Author: sbordes $
 * @since $Date: 2011-12-18 22:59:18 +0100 (dim., 18 déc. 2011) $
 */
public class AsyncServiceImpl extends AbstractWaveReady<Service> implements Service {

    /**
     * {@inheritDoc}
     */
    @Override
    public void ready() throws CoreException {
        // Nothing to do yet by the AsyncServiceImpl, must be overridden
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processAction(final Wave wave) {
        // Nothing to do yet by the AsyncServiceImpl, must be overridden
        // Must be overridden to manage action handling by service
        // Asynchronously
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable {
        getLocalFacade().getGlobalFacade().trackEvent(EventType.DESTROY_SERVICE, null, this.getClass());
        super.finalize();
    }
}
