
package myminilogback;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.*;
import java.text.SimpleDateFormat;

/**
 * Clas of the Simple File Appender.
 * It is records logs(LogEvents) to Text File.
 * Uses NIO.2
 * @author DantalioNxxi
 * @see Appendable
 * @see AbstractAppender
 */
public class FileAppender extends AbstractAppender{

    /**
     * Where will write logs.
     */
    private File file;
    
    public FileAppender(File f){
        file = f;
    }
    
    public FileAppender(File f, Layout l){
        file = f;
        layout = l;
    }
    
    /**
     * Temporary
     */
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yy HH:mm:ss,SSS");
    
    /**
     * It has the synchronized block, because some loggers can to have the access to this appender concurrently.
     * @param le is logs, which will have write to file.
     * @see OutputStreamWriter
     * @see Files
     */
    @Override
    public void record(LogEvent le) {
        synchronized(file){//as one from options?
            
//            System.out.println("Записывается в "+name+" лог с датой: "+SDF.format(le.getDateEvent()));
            
            //set date to le
            String message = layout.getMessage(le);

            try(OutputStreamWriter writer 
                    = new OutputStreamWriter(Files.newOutputStream(file.toPath(),
                    StandardOpenOption.APPEND, StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE, StandardOpenOption.DSYNC) )){
                String m2 = new String(message.getBytes("UTF-8"));
                writer.append(m2);
            } catch (IOException ex){
                System.out.println("Ошибка ввода-вывода в FileAppender "+ex.getMessage());
            }
        }
    }

    /**
     * Close all resources.
     * Probably, he will appear in later versions.
     */
    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    
    @Override
    public String toString() {
        return "FileAppender{" + "name=" + name + ", classname=" + classname + ", layout=" + layout + '}';
    }
    
}
