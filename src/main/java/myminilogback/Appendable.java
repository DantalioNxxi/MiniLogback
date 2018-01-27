
package myminilogback;

/**
 * Interface of object, which can to record logs (LogEvents).
 * References uses by such collections, as appenders into Logger.class and LoggerManager.class
 * @author DantalioNxxi
 * @see AbstractAppender
 * @see Logger
 * @see LoggerManager
 * @see LogEvent
 */
public interface Appendable {
    
    /**
     * Records to destination LogEvent's data
     * @param le is instance of one log
     */
    void record(LogEvent le);
    
    String getName();
    String getClassName();
    Layout getLayout();
     
    /**
     * Close all resources.
     * Probably, he will appear in later versions.
     */
    void close();//close all resources//
}
