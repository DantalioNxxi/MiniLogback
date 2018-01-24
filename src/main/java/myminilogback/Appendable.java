
package myminilogback;

/**
 *
 * @author DantalioNxxi
 */
public interface Appendable {
    
    /**
     * Records to destination LogEvent's data
     * @param le is instance of one log
     */
    void record(LogEvent le);
    
    String getName();
    String getClassName();
//    Layout getLayout();
     
    void close();//close all resources
}

//public interface Appendable<T extends OutputStream> {
//
//}
