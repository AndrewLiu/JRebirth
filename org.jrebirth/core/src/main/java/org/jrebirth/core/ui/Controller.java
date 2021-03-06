/**
 * Copyright JRebirth.org © 2011-2012 
 * Contact : sebastien.bordes@jrebirth.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jrebirth.core.ui;

import javafx.scene.Node;

import org.jrebirth.core.exception.CoreException;

/**
 * 
 * The interface <strong>Controller</strong>.
 * 
 * The contract for the controller layer.
 * 
 * @author Sébastien Bordes
 * 
 * @param <M> class type which will be the model of the view controlled, it must implements the #Model interface
 * @param <V> class type which will be control this controller, it must implements the #View interface
 */
public interface Controller<M extends Model, V extends View<M, ?, ?>> {

    /**
     * @return Returns the view.
     */
    V getView();

    /**
     * @return Returns the root node.
     */
    Node getRootNode();

    /**
     * @return Returns the model.
     */
    M getModel();

    /**
     * Activate the controller by calling the initializeEventHandler method.
     * 
     * Must be called after children initialization.
     * 
     * @throws CoreException if activation fails
     */
    void activate() throws CoreException;

}
