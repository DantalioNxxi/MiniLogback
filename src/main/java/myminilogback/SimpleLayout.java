
package myminilogback;

import java.text.SimpleDateFormat;

/**
 * Class of Layout, which handles LogEvent and turn him to String line.
 * @author DantalioNxxi
 */
public class SimpleLayout implements Layout{

    /**
     * Sequences of the Log's Pattern.
     */
    private String sequence; 
    private LogEvent le;
    
    /**Settings for the date*/
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yy HH:mm:ss,SSS");
    
    /**
     * By default will has been shown Date, Message and step to next line.
     */
    public SimpleLayout(){
        sequence = "%d %m %n"; //pattern by default
    }
    
    /**
     * @param seq Sequences of the Log's Pattern, which writing through space.
     * %d date and time;
     * %t thread's name;
     * %p Logger.LogLevel;
     * %l logger's name;
     * %C causingClass's neme;
     * %M causingMethod's name;
     * %L number of line, where was called log;
     * %m message;
     * throwable ThrowableInfo;
     * %n - step to next line;
     * @see LogEvent
     */
    public SimpleLayout(String seq){
        this.sequence = seq;
    }
    
    /**
     * Turns LogEvent into String message.
     * @param le logEvent, which will has turned to String message.
     * @return log at the shape - string message.
     * @see LogEvent
     */
    @Override
    public String getMessage(LogEvent le) {
        
        this.le = le;
        
        StringBuilder message = new StringBuilder("");
        String[] str = sequence.split(" ");
        for (String i : str) {
            message.append(getElement(i));
            if (!i.equals("%n")) message.append("\t");
        }
        
        return message.toString();
    }
    
    /**
     * Associates pattern's sequences with LogEvent's fields.
     * @param key pattern's sequence
     * @return LogEvent's field and etc.
     * @see LogEvent
     */
    private String getElement(String key){
        switch (key){
            case "%d": {
                return SDF.format(le.getDateEvent());
            }
            case "%t": {
                return "["+le.getThreadName()+"]";
            }
            case "%p": {
                return le.getPriority().toString();
            }
            case "%l": {
                return le.getNameLogger();
            }
            case "%C": {
                return "CallClass: "+le.getCausingClassName();
            }
            case "%M": {
                return "CallMethod: "+le.getCausingMethodName();
            }
            case "%L": {
                return "Line:"+Long.toString(le.getNumberOfLine());
            }
            case "%m": {
                return le.getMessage();
            }
            case "throwable": {
                if (le.getThrow() != null){
                    Throwable thr = le.getThrow();
                    return "Throw: "
                        +(thr.getCause()==null?thr.toString():thr.getCause().toString())
                        +": "
                        +le.getThrow().getMessage();
                }
                
                else return "";
            }
            case "%n": {
                return "\n";
            }
            default: return " ";
        }
    }

    @Override
    public String toString() {
        return "SimpleLayout{" + "sequence=" + sequence + '}';
    }
    
}
