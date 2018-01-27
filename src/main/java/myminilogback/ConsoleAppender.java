
package myminilogback;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clas of the Console Appender.
 * It is records logs(LogEvents) to Console.
 * Uses PrintStream().
 * @author DantalioNxxi
 * @see Appendable
 * @see AbstractAppender
 */
public class ConsoleAppender extends AbstractAppender{

    /**
     * Temporary date format, which uses by method record(le).
     */
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yy HH:mm:ss,SSS");
    
    
    /**
     * Is synchronized, because some loggers can to have the access to this appender concurrently.
     * @param le is logs, which will have show at console.
     */
    @Override
    public synchronized void record(LogEvent le) {
//        if (layout==null) layout = new SimpleLayout();//by default uses SimpleLayout
        
        le.setDateEvent(new Date());
        System.out.println("Записывается в "+name+" лог с датой: "+SDF.format(le.getDateEvent()));

        //set date to le
        String message = layout.getMessage(le);
        try (PrintStream ps = new PrintStream(System.out)){
            ps.append(message);
        } 
//        System.out.println(message);
//        catch (IOException ex){
//            System.out.println("Ошибка ввода-вывода в: "+ex.getCause());
//        }
    }

    /**
     * By default uses SimpleLayout.
     */
    public ConsoleAppender() {
        layout = new SimpleLayout();
    }
    
    public ConsoleAppender(Layout layout) {
        this.layout = layout;
    }

    /**
     * Close all resources.
     * Probably, he will appear in later versions.
     */
    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return "ConsoleAppender{" + "name=" + name + ", classname=" + classname + ", layout=" + layout + '}';
    }
    
    
}
