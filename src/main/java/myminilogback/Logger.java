
package myminilogback;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author DantalioNxxi
 */
public class Logger {
    
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
    
    private String name = "root";
    
//    private boolean additivity = true;
    
    private LogLevel level = LogLevel.INFO; //by default
    
    private Set<Appendable> appenders = new HashSet<>();
    
    Logger(LogLevel level){ // for root logger
        this.level = level;
    }
    
    Logger(String name){ //Will required configuration
        this.name = name;
    }
    
    Logger(String name, LogLevel level){
        this.name = name;
        this.level = level;
    }
    
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
     * @param message 
     */
    public void info(String message){
        //checking levels
        if (this.level.priority<=LogLevel.INFO.priority) {
            log(LogLevel.INFO, message);
        }
    }
    public void info(String message, Throwable throwable){
        if (this.level.priority<=LogLevel.INFO.priority) {
            log(LogLevel.INFO, message, throwable);
        }
    }
    
    public void warning(String message){
        if (this.level.priority<=LogLevel.WARN.priority) {
            log(LogLevel.WARN, message);
        }
    }
    public void warning(String message, Throwable throwable){
        if (this.level.priority<=LogLevel.WARN.priority) {
            log(LogLevel.WARN, message, throwable);
        }
    }
    
    public void fatal(String message){
        if (this.level.priority<=LogLevel.FATAL.priority) {
            log(LogLevel.FATAL, message);
        }
    }
    public void fatal(String message, Throwable throwable){
        if (this.level.priority<=LogLevel.FATAL.priority) {
            log(LogLevel.FATAL, message, throwable);
        }
    }

//    public Appendable getAppender(String name) {
//        
//        return appenders;
//    }

    void addAppender(Appendable app) {
        appenders.add(app);
    }
    
    boolean isContainsAppender(){
        if (appenders.isEmpty()) return false;
        return true;
    }

    public String getName() {
        return name;
    }
    
    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }
    
    //    public boolean isAdditivity() {
//        return additivity;
//    }
//
//    public void setAdditivity(boolean additivity) {
//        this.additivity = additivity;
//    }
    
    public void showInfo(){
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "Logger{" + "name=" + name + ", level=" + level + ", appenders=" + appenders + '}';
    }
    
}
