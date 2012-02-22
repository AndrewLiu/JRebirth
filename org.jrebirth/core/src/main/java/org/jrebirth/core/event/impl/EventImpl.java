package org.jrebirth.core.event.impl;

import java.util.StringTokenizer;

import org.jrebirth.core.event.Event;
import org.jrebirth.core.event.EventType;
import org.jrebirth.core.util.ClassUtility;

/**
 * 
 * The class <strong>EventImpl</strong>.
 * 
 * This Bean is used to store event data.
 * 
 * @author Sébastien Bordes
 * @version $Revision$ $Date$ $Name$
 * 
 * @since org.jrebirth.core 1.0
 */
public final class EventImpl implements Event {

    /** The sequence number. */
    private int sequence;

    /** The type of the event. */
    private EventType eventType;

    /** The source class. */
    private Class<?> source;

    /** The target class. */
    private Class<?> target;

    /** The event data. */
    private String eventData;

    /**
     * Default Constructor with mandatory fields.
     * 
     * @param eventType the type of the event
     * @param source the source class of the event
     * @param target the target of the event
     * @param eventData the data of the event
     */
    public EventImpl(final EventType eventType, final Class<?> source, final Class<?> target, final String... eventData) {
        this.eventType = eventType;
        this.source = source;
        this.target = target;
        if (eventData.length > 0) {
            this.eventData = eventData[0]; // TODO Manage list of event data
        }
    }

    /**
     * Default Constructor used to parse a string.
     * 
     * @param eventSerialized the serialized event
     */
    public EventImpl(final String eventSerialized) {
        parseString(eventSerialized);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSequence() {
        return this.sequence;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSequence(final int sequence) {
        this.sequence = sequence;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventType getEventType() {
        return this.eventType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEventType(final EventType eventType) {
        this.eventType = eventType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getSource() {
        return this.source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSource(final Class<?> source) {
        this.source = source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getTarget() {
        return this.target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTarget(final Class<?> target) {
        this.target = target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEventData() {
        return this.eventData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEventData(final String eventData) {
        this.eventData = eventData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getSequence());
        sb.append(ClassUtility.SEPARATOR);
        sb.append(getEventType());
        sb.append(ClassUtility.SEPARATOR);
        sb.append(getSource() == null ? null : getSource().getCanonicalName());
        sb.append(ClassUtility.SEPARATOR);
        sb.append(getTarget() == null ? null : getTarget().getCanonicalName());
        sb.append(ClassUtility.SEPARATOR);
        sb.append(getEventData());
        return sb.toString();
    }

    /**
     * Parse the serialized string.
     * 
     * @param eventSerialized the serialized string
     */
    private void parseString(final String eventSerialized) {
        final StringTokenizer st = new StringTokenizer(eventSerialized, ClassUtility.SEPARATOR);
        setSequence(Integer.valueOf(st.nextToken()));
        setEventType(EventType.valueOf(st.nextToken()));
        setSource(getClass(st.nextToken()));
        setTarget(getClass(st.nextToken()));
        setEventData(st.nextToken());
    }

    /**
     * Return the class object or null.
     * 
     * @param className the class name to convert into class object
     * 
     * @return a class or null
     */
    private Class<?> getClass(final String className) {
        Class<?> cls = null;
        try {
            if (!"null".equals(className)) {
                cls = Class.forName(className);
            }
        } catch (final ClassNotFoundException e) {
            cls = Object.class;
        }
        return cls;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUniqueKey() {
        return toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue() {
        // TODO CHECK IT
        return null;
    }

}
