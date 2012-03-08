package org.jrebirth.core.concurent;

/**
 * 
 * The class <strong>RunIntoType</strong>.
 * 
 * The different ways to run a task.
 *  
 * @author Sébastien Bordes
 */
public enum RunIntoType {
	
	/** Run into Java Application Thread. */
    JAT,
    
    /** Run into JRebirth Engine Thread. */
    JET,
    
    /** Queue in a Thread Pool and run it sequentially. */
    THREAD_POOL;
    
}