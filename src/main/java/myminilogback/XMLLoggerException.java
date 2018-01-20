
package myminilogback;

/**
 *
 * @author DantalioNxxi
 */
public class XMLLoggerException extends Exception{

    public XMLLoggerException() {
    }

    public XMLLoggerException(String message) {
        super(message);
    }

    public XMLLoggerException(String message, Throwable cause) {
        super(message, cause);
    }

    public XMLLoggerException(Throwable cause) {
        super(cause);
    }

    public XMLLoggerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
