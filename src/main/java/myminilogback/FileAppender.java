
package myminilogback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.*;

/**
 *
 * @author DantalioNxxi
 */
public class FileAppender extends AbstractAppender{

    private File file;
    
    FileAppender(File f){
        file = f;
    }
    
    FileAppender(File f, Layout l){
        file = f;
        layout = l;
    }
     
    @Override
    public void record(LogEvent le) {   //Is will be synchronized?
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

//        try(FileOutputStream fos = new FileOutputStream(file, true)){
//                    String m2 = new String(message.getBytes("UTF-8"));
//                    fos.write(m2.getBytes());//is synchronized???? he is native
//                    //writer.append(m2);
//                } catch (IOException ex){
//                    System.out.println("Ошибка ввода-вывода в FileAppender "+ex.getMessage());
//                }
        
        //create new rotate file 
    }

    @Override
    public void close() {
        
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
