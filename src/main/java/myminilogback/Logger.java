
package myminilogback;

import java.util.HashSet;
import java.util.Set;

/**
 * The class of the any Logger.
 * One logger can to has few references to different appenders.
 * To gets logger instance might only through LoggerManager.
 * @author DantalioNxxi
 * @see LoggerManager
 * @see Appendable
 */
public class Logger {
    
    /**
     * If level's priority of a LogEvent less than level's priority of the logger,
     * then LogEvent will have not written to appenders.
     */
    public static enum LogLevel{
        INFO(10), WARN(20), FATAL(30);
        final int priority;
        /**
         * @param priority for compare the levels.
         */
        private LogLevel(int priority){
            this.priority = priority;
        }
        public int getPriority(){return priority;}
    }
    
    /**
     * Equals 'root' by deafult.
     */
    private String name = "root";
    /**
     * Equals 'INFO' by default.
     */
    private LogLevel level = LogLevel.INFO;
    
    private Set<Appendable> appenders = new HashSet<>();
    
    /**
     * This constructor used by LoggerManager only for creating root Logger.
     * @param level of created root Logger
     * @see LoggerManager
     */
    Logger(LogLevel level){
        this.level = level;
    }
    
    /**
     * This constructor used by LoggerManager only for creating any Logger.
     * If Logger with such name does not describe into XML-config,
     * then LoggerManager will have created a new Logger with such name.
     * @param name of future Logger.
     * @see LoggerManager
     */
    Logger(String name){
        this.name = name;
    }
    
    /**
     * This constructor used by LoggerManager only for creating any Logger.
     * If Logger with such name does not describe into XML-config,
     * then LoggerManager will have created a new Logger with such name.
     * @param name of future Logger.
     * @param level of future Logger.
     * @see LoggerManager
     */
    Logger(String name, LogLevel level){
        this.name = name;
        this.level = level;
    }
    
    /**
     * The method writes Log with specified level and message into all appenders.
     * Probably, he will modifyed at later versions.
     * @param level of LogEvent
     * @param message of LogEvent
     * @see LogEvent
     */
    private synchronized void log(LogLevel level, String message){
        //sell this log to all appenders
        if (!appenders.isEmpty()){
            //create the logEvent
            LogEvent le = new LogEvent(level, name, message);
            for (Appendable app : appenders){
                app.record(le);
            }
        }
    }
    
    /**
     * The method writes Log with specified level, message and Exception's Description into all appenders.
     * Probably, he will modifyed at later versions.
     * @param level of LogEvent
     * @param message of LogEvent
     * @param throwable of LogEvent
     * @see LogEvent
     */
    private synchronized void log(LogLevel level, String message, Throwable throwable){
        if (!appenders.isEmpty()){
            LogEvent le = new LogEvent(level, name, message, throwable);
            for (Appendable app : appenders){
                app.record(le);
            }
        }
    }
    
    /**
     * If level's priority is not suitable, then LogEvent will not create
     * @param message of assumed future LogEvent
     * @see LogLevel
     * @see LogEvent
     */
    public void info(String message){
        //checking levels
        if (this.level.priority<=LogLevel.INFO.priority) {
            log(LogLevel.INFO, message);
        }
    }
    
    /**
     * If level's priority is not suitable, then LogEvent will not create
     * @param message of assumed future LogEvent
     * @param throwable of assumed future LogEvent
     * @see LogLevel
     * @see LogEvent
     */
    public void info(String message, Throwable throwable){
        if (this.level.priority<=LogLevel.INFO.priority) {
            log(LogLevel.INFO, message, throwable);
        }
    }
    
    /**
     * If level's priority is not suitable, then LogEvent will not create
     * @param message of assumed future LogEvent
     * @see LogLevel
     * @see LogEvent
     */
    public void warning(String message){
        if (this.level.priority<=LogLevel.WARN.priority) {
            log(LogLevel.WARN, message);
        }
    }
    
    /**
     * If level's priority is not suitable, then LogEvent will not create
     * @param message of assumed future LogEvent
     * @param throwable of assumed future LogEvent
     * @see LogLevel
     * @see LogEvent
     */
    public void warning(String message, Throwable throwable){
        if (this.level.priority<=LogLevel.WARN.priority) {
            log(LogLevel.WARN, message, throwable);
        }
    }
    
    /**
     * If level's priority is not suitable, then LogEvent will not create
     * @param message of assumed future LogEvent
     * @see LogLevel
     * @see LogEvent
     */
    public void fatal(String message){
        if (this.level.priority<=LogLevel.FATAL.priority) {
            log(LogLevel.FATAL, message);
        }
    }
    
    /**
     * If level's priority is not suitable, then LogEvent will not create
     * @param message of assumed future LogEvent
     * @param throwable of assumed future LogEvent
     * @see LogLevel
     * @see LogEvent
     */
    public void fatal(String message, Throwable throwable){
        if (this.level.priority<=LogLevel.FATAL.priority) {
            log(LogLevel.FATAL, message, throwable);
        }
    }

    public Appendable getAppender(String name) {
        for (Appendable a : appenders){
            if (a.getName().equals(name)) return a;
        }
        return null;
    }

    /**
     * Adds a new Appendable Object into hash set collection 'appenders' of the Logger.
     * If such Appender had added, then will show a warning about that.
     * @param app Appendable Object
     */
    public void addAppender(Appendable app) {
        if (!appenders.add(app)) {
            System.out.println("Аппендер "+app.getName()+" уже есть в логере "+name+"!");
        }
    }
    
    /**
     * @return true, if this Logger contains at least one reference to any appender
     */
    boolean isContainsAppenders(){
        if (appenders.isEmpty()) return false;
        return true;
    }

    public String getName() {
        return name;
    }
    
    /**
     * @return Level of the Logger, which was setted early
     * @see LogLevel
     */
    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }
    
    public void showInfo(){
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "Logger{" + "name=" + name + ", level=" + level + ", appenders=" + appenders + '}';
    }
    
}
