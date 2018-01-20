
package myminilogback;

import java.io.PrintStream;

/**
 *
 * @author DantalioNxxi
 */
public class ConsoleAppender extends AbstractAppender{

    @Override
    public void record(LogEvent le) {
        String message = layout.getMessage(le);
        try (PrintStream ps = new PrintStream(System.out)){
            ps.append(message);
        } 
//        catch (IOException ex){
//            System.out.println("Ошибка ввода-вывода в: "+ex.getCause());
//        }
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
