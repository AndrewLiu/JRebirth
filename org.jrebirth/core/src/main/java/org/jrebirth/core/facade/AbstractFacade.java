package org.jrebirth.core.facade;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.jrebirth.core.command.Command;
import org.jrebirth.core.event.EventType;
import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.exception.CoreRuntimeException;
import org.jrebirth.core.service.Service;
import org.jrebirth.core.ui.Model;
import org.jrebirth.core.ui.impl.AbstractModel;

/**
 * 
 * The class <strong>AbstractFacade</strong>.
 * 
 * An abstract facade can manage singleton of object which implements the FacadeReady interface
 * 
 * @author Sébastien Bordes
 * 
 * @version $Revision: 36 $ $Author: sbordes $
 * @since $Date: 2011-10-10 23:17:57 +0200 (Mon, 10 Oct 2011) $
 * 
 * @param <R> A type that implements FacadeReady
 */
public abstract class AbstractFacade<R extends FacadeReady<R>> extends AbstractGlobalReady implements Facade<R> {

    /** The map that store FacadeReady singletons. */
    private final Map<Class<? extends R>, R> singletonMap = new WeakHashMap<>();

    /** The map that store FacadeReady multitons. */
    private final Map<Class<? extends R>, Map<UniqueKey, R>> multitonMap = new WeakHashMap<>();

    /**
     * Default Constructor.
     * 
     * @param globalFacade the global facade of the application
     */
    public AbstractFacade(final GlobalFacade globalFacade) {
        super(globalFacade);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <E extends R> void register(final E readyObject, final UniqueKey... key) {

        // For Singleton Components, no key is provided
        if (key.length == 0) {
            // Check if the class of the object is already stored into the singleton map
            if (!this.singletonMap.containsKey(readyObject.getClass())) {

                // Attach the facade to allow to retrieve any components
                readyObject.setLocalFacade(this);

                // Store the component into the singleton map
                this.singletonMap.put((Class<E>) readyObject.getClass(), readyObject);
            }

        } else {

            // For Multiton Components
            // Check if the UniqueKey map exists for this component class
            if (!this.singletonMap.containsKey(readyObject.getClass())) {
                this.multitonMap.put((Class<E>) readyObject.getClass(), new HashMap<UniqueKey, R>());
            }
            // Check if the class of the object is already stored into the multitonKey map
            if (!this.multitonMap.get(readyObject.getClass()).containsKey(key[0])) {

                // Attach the facade to allow to retrieve any components
                readyObject.setLocalFacade(this);

                // Store the component into the multitonKey map
                this.multitonMap.get(readyObject.getClass()).put(key[0], readyObject);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends R> void unregister(final E readyObject, final UniqueKey... key) {
        // Release the facade link
        readyObject.setLocalFacade(null);

        if (key.length == 0) {
            // Remove the component from the singleton map
            this.singletonMap.remove(readyObject.getClass());
        } else {
            // Remove the component from the multitonKey map
            this.multitonMap.get(readyObject.getClass()).remove(key[0]);

            // Remove the multitonKey map if nothing is stored
            if (this.multitonMap.get(readyObject.getClass()).size() == 0) {
                this.multitonMap.remove(readyObject.getClass());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <E extends R> E retrieve(final Class<E> clazz, final UniqueKey... key) {

        E readyObject;

        // If the component isn't contained into a map, create and register it
        if (!exists(clazz, key)) {
            try {
                // Build an instance and register it
                register(build(clazz, key), key);
            } catch (final CoreException ce) {
                getGlobalFacade().getLogger().error(ce.getMessage());
                getGlobalFacade().getLogger().error("Error while building " + clazz.getCanonicalName() + " instance");
                throw new CoreRuntimeException("Error while building " + clazz.getCanonicalName() + " instance", ce);
            }
        }
        // retrieve the component from the right map
        if (key.length == 0) {
            // If no key is provided retrieve from the singleton map
            readyObject = (E) this.singletonMap.get(clazz);
        } else {
            // otherwise retrieve from the multiton map (which cannot be null)
            readyObject = (E) this.multitonMap.get(clazz).get(key[0]);
        }

        return readyObject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(final Class<? extends R> clazz, final UniqueKey... key) {
        boolean res;
        if (key.length > 0) {
            // Check from multiton map
            final Map<UniqueKey, R> mMap = this.multitonMap.get(clazz);
            res = mMap != null && mMap.containsKey(key[0]);
        } else {
            // Check from singleton map
            res = this.singletonMap.containsKey(clazz);
        }
        return res;
    }

    /**
     * Build a new instance of the ready object class.
     * 
     * @param clazz the class to build
     * @param key the unique key or null
     * 
     * @return a new instance of the given clazz and key
     * 
     * @throws CoreException if an error occurred
     */
    protected R build(final Class<? extends R> clazz, final UniqueKey... key) throws CoreException {
        try {
            // Build a new instance of the component
            final R readyObject = clazz.newInstance();

            // Retrieve the right event type to track
            EventType type = EventType.NONE;
            if (readyObject instanceof Model) {
                type = EventType.CREATE_MODEL;
            } else if (readyObject instanceof Service) {
                type = EventType.CREATE_SERVICE;
            } else if (readyObject instanceof Command) {
                type = EventType.CREATE_COMMAND;
            }
            // Track this instantiation event
            getGlobalFacade().trackEvent(type, this.getClass(), readyObject.getClass());

            // Attach the local facade
            readyObject.setLocalFacade(this);

            // TODO IMPROVE IT
            if (readyObject instanceof AbstractModel && key.length > 0) {
                // Attach the unique key (if any)
                ((AbstractModel<?, ?>) readyObject).setModelObject(key[0]);
            }

            // Start the component initialisation
            readyObject.ready();
            // Component Ready !
            return readyObject;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
            throw new CoreException("Impossible to create the class " + clazz.getName(), e);
        }
    }

}
