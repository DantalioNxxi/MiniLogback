
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
    private long numberOfLine;
    private String message;
    private Throwable ex;
    
    LogEvent(Logger.LogLevel level, String message){
        dateEvent = new Date();
        threadName = "имя вызывающего потока";
        priority = level;
        nameLogger = "имя логгера";
        causingClassName = "имя вызывающего класса";
        numberOfLine = 33;
        this.message = message;
    }
    
    LogEvent(Logger.LogLevel level, String message, Throwable ex){
        dateEvent = new Date();
        threadName = "имя вызывающего потока";
        priority = level;
        nameLogger = "имя логгера";
        causingClassName = "имя вызывающего класса";
        numberOfLine = 33;
        this.message = message;
        this.ex = ex; //it is need at modifayed
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

