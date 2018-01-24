
package myminilogback;

import java.util.Date;

/**
 * Class for one log's instance
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
    
    LogEvent(Logger.LogLevel level, String nameLogger, String message){
        dateEvent = new Date();
        threadName = Thread.currentThread().getName();
        priority = level;
        this.nameLogger = nameLogger;
        this.message = message;
        createCausingNames();
    }
    
    LogEvent(Logger.LogLevel level, String nameLogger, String message, Throwable ex){
        dateEvent = new Date();
        threadName = Thread.currentThread().getName();
        priority = level;
        this.nameLogger = nameLogger;
        this.message = message;
        this.ex = ex; //it is need at modifayed
        createCausingNames();
    }
    
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

    Throwable getThrow() {
        return ex;
    }
    
}

