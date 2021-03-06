package org.jrebirth.presentation.ui.template;

import org.jrebirth.presentation.ui.base.AbstractSlideModel;
import org.jrebirth.presentation.ui.base.SlideStep;

/**
 * The class <strong>AbstractTemplateModel</strong>.
 * 
 * @author Sébastien Bordes
 * 
 * @param <M> the TemplateModel class
 * @param <V> the TemplateView class
 */
public abstract class AbstractTemplateModel<M extends AbstractTemplateModel<M, V, S>, V extends AbstractTemplateView<?, ?, ?>, S extends SlideStep> extends AbstractSlideModel<M, V, S> {

    /**
     * Return the title string from the slide object.
     * 
     * @return the slide title
     */
    protected String getTitle() {
        return getSlide().getTitle();
    }
}
