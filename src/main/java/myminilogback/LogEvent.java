
package myminilogback;

import java.util.Date;

/**
 * Class for one log's instance.
 * @author DantalioNxxi
 */
public class LogEvent {
    
    private Date dateEvent;
    private String threadName;
    private Logger.LogLevel priority;
    private String nameLogger;
    private String causingClassName;
    private String causingMethodName;
    private long numberOfLine;
    private String message;
    private Throwable ex;
    
    /**
     * To create a new Log without exception's description.
     * @param level assumed permit level
     * @param nameLogger through that, which accepted message
     * @param message just message
     */
    LogEvent(Logger.LogLevel level, String nameLogger, String message){
        dateEvent = new Date();
        threadName = Thread.currentThread().getName();
        priority = level;
        this.nameLogger = nameLogger;
        this.message = message;
        createCausingNames();
    }
    
    /**
     * To create a new Log with exception's description.
     * @param level assumed permit level
     * @param nameLogger through that, which accepted message
     * @param message just message
     * @param ex exception, whose description will show into the Log
     */
    LogEvent(Logger.LogLevel level, String nameLogger, String message, Throwable ex){
        dateEvent = new Date();
        threadName = Thread.currentThread().getName();
        priority = level;
        this.nameLogger = nameLogger;
        this.message = message;
        this.ex = ex; //it is need at modifayed
        createCausingNames();
    }
    
    /**
     * The method for filling such fields, as causingClassName, causingMethodName and numberOfLine.
     * He uses dummy exception and his StackTrace for this case.
     * @see StackTraceElement
     * @see Throwable
     */
    private void createCausingNames(){
        Throwable thr = new Throwable();
            StackTraceElement[] ste = thr.getStackTrace();
            for (int i = 0; i < ste.length; i++) {
                if (ste[i].toString().contains("Logger.info")
                    ||ste[i].toString().contains("Logger.warning")
                        ||ste[i].toString().contains("Logger.fatal"))
                {
                    StackTraceElement e = ste[i+1];
                    this.causingClassName = e.getClassName();
                    this.causingMethodName = e.getMethodName();
                    this.numberOfLine = e.getLineNumber();
                    break;
                }
            }
    }

    String getNameLogger() {
        return nameLogger;
    }

    Date getDateEvent() {
        return dateEvent;
    }

    String getThreadName() {
        return threadName;
    }

    /**
     * Givens assumed level of future Log.
     * @return assumed level
     */
    Logger.LogLevel getPriority() {
        return priority;
    }

    String getCausingClassName() {
        return causingClassName;
    }

    String getCausingMethodName() {
        return causingMethodName;
    }
    
    long getNumberOfLine() {
        return numberOfLine;
    }

    String getMessage() {
        return message;
    }

    /**
     * Returns reference to eception instance, which will have written into future Log Instance.
     * @return eception instance, which will have written into future Log Instance
     */
    Throwable getThrow() {
        return ex;
    }
    
}

