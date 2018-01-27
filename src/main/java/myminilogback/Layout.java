
package myminilogback;

/**
 * Interface of prototypes for setting log's format(layout).
 * @author DantalioNxxi
 * @see SimpleLayout
 */
public interface Layout {
    /**
     * 
     * @param le instance of one log
     * @return finally string version of LogEvent at defined format
     */
    String getMessage(LogEvent le);
}
