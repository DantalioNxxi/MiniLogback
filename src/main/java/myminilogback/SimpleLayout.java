
package myminilogback;

import java.text.SimpleDateFormat;

/**
 *
 * @author DantalioNxxi
 */
public class SimpleLayout implements Layout{

    private String sequence; 
    private LogEvent le;
    
    /**Settings for the date*/
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yy HH:mm:ss,SSS");
    
    SimpleLayout(){
        sequence = "%d %m %n"; //pattern by default
    }
    
    SimpleLayout(String seq){
        this.sequence = seq;
    }
    
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
                return le.getCausingClassName();
            }
            case "%L": {
                return "Строка:"+Long.toString(le.getNumberOfLine());
            }
            case "%m": {
                return le.getMessage();
            }
            case "throwable": {
                return le.getThrow().getMessage();
            }
            case "%n": {
                return "\n";
            }
            default: return " ";
        }
    }
    
}
