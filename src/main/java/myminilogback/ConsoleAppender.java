
package myminilogback;

import java.io.PrintStream;

/**
 *
 * @author DantalioNxxi
 */
public class ConsoleAppender extends AbstractAppender{

    @Override
    public synchronized void record(LogEvent le) {
        if (layout==null) layout = new SimpleLayout();//by default uses SimpleLayout
        String message = layout.getMessage(le);
        try (PrintStream ps = new PrintStream(System.out)){
            ps.append(message);
        } 
//        System.out.println(message);
//        catch (IOException ex){
//            System.out.println("Ошибка ввода-вывода в: "+ex.getCause());
//        }
    }

    ConsoleAppender() {
    }
    
    ConsoleAppender(Layout layout) {
        this.layout = layout;
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return "ConsoleAppender{" + "name=" + name + ", classname=" + classname + ", layout=" + layout + '}';
    }
    
    
}
