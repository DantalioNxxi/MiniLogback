
package myminilogback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Clas of the Rolling File Appender.
 * He lets to setting rules of rotation files.
 * @author DantalioNxxi
 */
public class RollingFileAppender extends AbstractAppender{
    
    /**
     * Where will write logs.
     */
    private File file;
    /**
     * There is needs in reference to XML-config, because at enabled changedXML,
     * TextContext of node 'file' (Path) will has changing.
     */
    private File config;
    /**
     * If true, then rotation will has happened. By dafult false.
     */
    private boolean isRotation = false;
    /**
     * If true, then after each rotate checking of filling file will has happened.
     * If file was filled, then will create a new file with name at format 'name(numberrotation).extension'.
     * By dafult false.
     */
    private boolean changeXML = false;
    /**
     * Max size log-file. By default equals one megabyte.
     */
    private long maxSize = 1_048_576L;
    /**
     * During log-process this variable will has updated after each rotation.
     */
    private int lastCountRotation;
    
    /**
     * Temporary
     */
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yy HH:mm:ss,SSS");
    
    public RollingFileAppender(File f, File config){
        file = f;
        this.config = config;
        layout = new SimpleLayout();
    }
    
    public RollingFileAppender(File f, File config, Layout l){
        file = f;
        this.config = config;
        layout = l;
    }
    
    public RollingFileAppender(File f, File config, boolean isRotation){
        file = f;
        this.config = config;
        this.isRotation = isRotation;
        layout = new SimpleLayout();
    }
    
    public RollingFileAppender(File f, File config, long maxSize, boolean changeXML){
        file = f;
        this.config = config;
        this.maxSize = maxSize;
        this.changeXML = changeXML;
        layout = new SimpleLayout();
    }
    
    public RollingFileAppender(File f, File config, boolean isRotation, long maxSize){
        file = f;
        this.config = config;
        this.isRotation = isRotation;
        this.maxSize = maxSize;
        layout = new SimpleLayout();
    }
    
    public RollingFileAppender(File f, File config, boolean isRotation, long maxSize, Layout l){
        file = f;
        this.config = config;
        this.isRotation = isRotation;
        this.maxSize = maxSize;
        layout = l;
    }
    
    /**
     * Constructor, which uses by LoggerManager at creatRollingFileAppender.
     * @param f Where will write logs.
     * @param config reference to XML-config
     * @param isRotation If true, then rotation will has happened. By dafult false.
     * @param maxSize Max size log-file. By default equals one megabyte.
     * @param changeXML If true, then after each rotate checking of filling file will has happened.
     * @see LoggerManager#createRollingFileAppender(org.w3c.dom.Node) 
     */
    public RollingFileAppender(File f, File config, boolean isRotation, long maxSize, boolean changeXML){
        file = f;
        this.config = config;
        this.isRotation = isRotation;
        this.maxSize = maxSize;
        this.changeXML = changeXML;
        layout = new SimpleLayout();
    }
    
    /**
     * Records LogEvent to file.
     * @param le object of LogEvent
     * @see LogEvent
     * @see RollingFileAppender#changeXML(java.nio.file.Path, java.io.File, java.lang.String) 
     */
    @Override
    public synchronized void record(LogEvent le) {   //Is will be synchronized without post-set-date
        String message = layout.getMessage(le);
        
//        //may be here to set Data???!!!
//        System.out.println("Записывается в "+name+" лог с датой: "+SDF.format(le.getDateEvent()));
        
        if (isRotation){
            try {
                if (Files.size(file.toPath())>=maxSize){
                    System.out.println("Размер файла" +file.getName()+" был превышен!");
                    file = getRotationFile(file);
                    
                    if (changeXML){
                        changeXML(file.toPath(), config, name);
                    }
                }
            } catch (FileNotFoundException f){
                System.out.println("Ошибка ввода-вывода в RollingFileAppender");
                System.out.println("Файд не найден! "+f.getMessage());
            } catch (IOException ex){
                System.out.println("Ошибка ввода-вывода в RollingFileAppender");
                System.out.println("Ошибка в проверке размера файла: "+ex.getMessage());
            }
        }
        
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
    
    /**
     * Changes XML-config file - at node 'appender' with attribute 'name'
     * sets new TextContext into node 'file'(path).
     * Synchronized by all class,
     * because XML-config is will have been chainging by different RollingFileAppender-s.
     * @param filepath new TextContext into node 'file'(path).
     * @param conf reference to XML-config file
     * @param name of thi RollingFileAppender.
     * @see RollingFileAppender#isMyRollingFileAppender(org.w3c.dom.Node, java.lang.String) 
     */
    private static synchronized void changeXML(Path filepath, File conf, String name){
        try {
            // Строим объектную модель исходного XML файла
            DocumentBuilder db = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = db.parse(conf);
            doc.normalize();
            
            NodeList appenders = doc.getElementsByTagName("appender");
            for (int i = 0; i < appenders.getLength(); i++) {
                Node appender = appenders.item(i);
                //if this node is suitable appender, then change TextContext 'file'
                if (isMyRollingFileAppender(appender, name)) {
                    NodeList childNodes = appender.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node nextChild = childNodes.item(j);
                        if (nextChild.getNodeName().equals("file")) {
                            nextChild.setTextContent(filepath.toString());
                            break;
                        }
                    }
                    break;
                }
            }
            // Write changes to XML-file
            Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();
            DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(conf);
                transformer.transform(source, result);
                
        } catch (SAXException | IOException | ParserConfigurationException
                | TransformerConfigurationException ex) {
            System.out.println("Ошибка ввода-вывода при изменении XML-файла: "+ex.getMessage());
        } catch (TransformerException ex) {
            System.out.println("Ошибка в траносформации XML "+ex.getMessage());
        }
    }
    
    /**
     * Checks appender is a RollingFileAppender and his name equals 'n'.
     * @param appender assumed RollingFileAppender with name equals 'n'.
     * @param n name of assumed suitable appender.
     * @return true, if such appender matchs to such name.
     * @see RollingFileAppender#changeXML(java.nio.file.Path, java.io.File, java.lang.String) 
     */
    private static boolean isMyRollingFileAppender(Node appender, String n){
        Element element = (Element) appender;
        if (element.getAttribute("class").equals("RollingFileAppender")
                && element.getAttribute("name").equals(n)){
            System.out.println("Найден наш с именем "+element.getAttribute("name")
                    +" и классом "+element.getAttribute("class"));
            return true;
        }
        return false;
    }
    
    /**
     * Computes reference to new file with new rotate number,
     * because old file was filled.
     * @param file old filed, was filled.
     * @return reference to new file with new rotate number.
     * @see RollingFileAppender#isContainsCountBrackets(java.lang.String) 
     * @see RollingFileAppender#getWithoutExtension(java.lang.String) 
     * @see RollingFileAppender#getExtension(java.lang.String) 
     * @see RollingFileAppender#countRotation(java.lang.String, java.lang.String, java.nio.file.Path, int) 
     * @see RollingFileAppender#existsRotationFile(java.lang.String, int, java.lang.String, java.nio.file.Path) 
     * @see RollingFileAppender#getPathRotationFile(java.lang.String, int, java.lang.String, java.nio.file.Path) 
     * @see RollingFileAppender#createNewRotationFile(java.lang.String, java.lang.String, java.nio.file.Path) 
     */
    private File getRotationFile(File file){
        System.out.println("Начата ротация: ");
        
        File parent = file.getParentFile();
        File ret;
        String withoutExtension = getWithoutExtension(file.getName());
        String extension = getExtension(file.getName());
        
        if (isContainsCountBrackets(withoutExtension)){
            int openBracket = withoutExtension.lastIndexOf("(");
            int closeBracket = withoutExtension.lastIndexOf(")");
            String clearName = withoutExtension.substring(0, openBracket);
            String numbers = withoutExtension.substring(openBracket+1, closeBracket);
            //then it is getting his number
            try {
                int number = Integer.parseInt(numbers);
                int newlc = countRotation(clearName, extension, parent.toPath(), number);
                //check for append in old file
                if (existsRotationFile(clearName, newlc, extension, parent.toPath())
                        && Files.size(getPathRotationFile(clearName, newlc, extension, parent.toPath()))>=maxSize)
                    ret = createNewRotationFile(clearName, extension, parent.toPath());
                else {//size of file fits
                    ret = getPathRotationFile(clearName, newlc, extension, parent.toPath()).toFile();
                }
                return ret;
            } catch (NumberFormatException ex){
                System.out.println("При парсинге number"
                        +numbers+" ошибка: "
                        +ex.getMessage());
                //if it is the simply name
                int newlc = countRotation(withoutExtension,  extension, parent.toPath(), 0);
                //check for append in old file
                try{
                    if (existsRotationFile(withoutExtension, newlc, extension, parent.toPath())
                            && Files.size(getPathRotationFile(withoutExtension, newlc, extension, parent.toPath()))>=maxSize)
                        ret = createNewRotationFile(withoutExtension, extension, parent.toPath());
                    else {//size of file fits
                        ret = getPathRotationFile(withoutExtension, newlc, extension, parent.toPath()).toFile();
                    }
                } catch (IOException e){
                    ret = getPathRotationFile(withoutExtension, newlc, extension, parent.toPath()).toFile();
                    System.out.println("Ошибка в получении пути файла: "+e.getMessage());
                }
                return ret;
            }  catch (IOException ex){
                ret = getPathRotationFile(withoutExtension, lastCountRotation, extension, parent.toPath()).toFile();
                System.out.println("Ошибка в получении пути файла: "+ex.getMessage());
                return ret;
            }
        } else {//if it is the simply name
            int newlc = countRotation(withoutExtension, extension, parent.toPath(), 0);
            //check for append in old file
            try{
                if (existsRotationFile(withoutExtension, newlc, extension, parent.toPath())
                        && Files.size(getPathRotationFile(withoutExtension, newlc, extension, parent.toPath()))>=maxSize)
                    ret = createNewRotationFile(withoutExtension, extension, parent.toPath());
                else {//size of file fits
                    ret = getPathRotationFile(withoutExtension, newlc, extension, parent.toPath()).toFile();
                }
            } catch (IOException e){
                ret = getPathRotationFile(withoutExtension, newlc, extension, parent.toPath()).toFile();
                System.out.println("Ошибка в получении пути файла: "+e.getMessage());
            }
            return ret;
        }
    }
    
    /**
     * Checks containing of open and closed rotate's breackets.
     * @param withoutExtension name of file without his extension.
     * @return true, if such brackets exists.
     */
    private boolean isContainsCountBrackets(String withoutExtension){
        if (withoutExtension.contains("(") && withoutExtension.contains(")")){
            int openBracket = withoutExtension.lastIndexOf("(");
            int closeBracket = withoutExtension.lastIndexOf(")");
            if (closeBracket==withoutExtension.length()-1 && openBracket<closeBracket){
                try{
                    Integer.parseInt(withoutExtension.substring(openBracket+1, closeBracket));
                    return true;
                } catch (NumberFormatException ex){
                    return false;
                }
            }
        }
        return false;
    }
    
    /**
     * Computes name of file without his extension.
     * @param withExtension whole name of file.
     * @return name of file without his extension.
     */
    private String getWithoutExtension(String withExtension){
        if (withExtension.contains(".")){
            int dot = withExtension.lastIndexOf(".");
            return withExtension.substring(0, dot);
        } else {
            return withExtension;
        }
    }
    
    /**
     * Computes extension for name of file.
     * @param path whole name of file.
     * @return extension for name of file.
     */
    private String getExtension(String path){
        System.out.println("Начато получение расширения: ");
        if (path.contains(".")){
            int dot = path.lastIndexOf(".");
            System.out.println("return "+path.substring(dot, path.length()));
            return path.substring(dot, path.length());
        } else {
//            System.out.println("return \"\"");
            return "";
        }
    }
    
    /**
     * Searchs suitable filename into parent-directory.
     * @param clearName with it filename must starts.
     * @param extension suitable extension of searching filename.
     * @param parent path to directory, where will has searching.
     * @param assumedLastCount assumed number of last rotation.
     * @return new number of last rotation.
     */
    private int countRotation(String clearName, String extension, Path parent, int assumedLastCount){
        lastCountRotation = assumedLastCount;
        
        try (DirectoryStream<Path>
                stream = Files.newDirectoryStream(parent,
                    (x)->!Files.isDirectory(x, LinkOption.NOFOLLOW_LINKS)
                        &&getWithoutExtension(x.getFileName().toString()).startsWith(clearName)
                        &&x.getFileName().toString().charAt(clearName.length())=='('
                        &&getExtension(x.getFileName().toString()).equals(extension))) 
        {
            for (Path entry: stream) {
                
                String clearEntry = getWithoutExtension(entry.getFileName().toString());
                
                if (isContainsCountBrackets(clearEntry) 
                        && clearEntry.startsWith(clearName)){
                    int openBracket = clearEntry.lastIndexOf("(");
                    int closeBracket = clearEntry.lastIndexOf(")");
                    String numbers = clearEntry.substring(openBracket+1, closeBracket);
                    try{
                        int number = Integer.parseInt(numbers);
                        if (number>lastCountRotation){
                            lastCountRotation = number;
                        }
                    } catch (NumberFormatException ex){
                        System.out.println("Ошибка при парсинге "+ex.getMessage());
                    } 
                    
                }
            }
            if (lastCountRotation > assumedLastCount){
                return lastCountRotation;//non-increment
            }
        } catch (IOException x) {
            System.out.println("Ошибка в countRotation "+x.getMessage());
        }
        return lastCountRotation;//non-increment
    }
    
    /**
     * Checks existing of the pathTo + whole name of file.
     * @param clearName name file without rotation part and extension.
     * @param numberRotation number, which will has included into brackets.
     * @param extension part of extension filename.
     * @param parent relative path to filename
     * @return true, is such filename with setted rotate-number exists.
     */
    private boolean existsRotationFile(String clearName, int numberRotation, String extension, Path parent){
        Path newFile = getPathRotationFile(clearName, numberRotation, extension, parent);
        return Files.exists(newFile, LinkOption.NOFOLLOW_LINKS);
    }
    
    /**
     * Computes pathTo + whole name of file (complete absolute path for rotation file).
     * @param clearName name file without rotation part and extension.
     * @param numberRotation number, which will has included into brackets.
     * @param extension part of extension filename.
     * @param parent relative path to filename
     * @return whole path to filename with setted rotate-number.
     */
    private Path getPathRotationFile (String clearName, int numberRotation, String extension, Path parent){
        Path newFile = Paths.get(clearName+"("+numberRotation+")"+extension);
        return parent.resolve(newFile);
    }
    
    /**
     * Try to create new File with whole path to filename with setted rotate-number.
     * @param clearName name file without rotation part and extension.
     * @param extension part of extension filename.
     * @param parent part of extension filename.
     * @return reference to new rotate-file.
     */
    private File createNewRotationFile(String clearName, String extension, Path parent){

        Path newPath = getPathRotationFile(clearName, lastCountRotation+1, extension, parent);
        try{
            File ret = Files.createFile(newPath).toFile();
            return ret;
        } catch (IOException ex){
            System.out.println("Ошибка в создании файла: "+ex.getMessage());
        }
        System.out.println("Ошибка в создании файла: "+newPath);
        return file;
    }
    
    /**
     * Changes new rules for file's rotation at this rolling file appender.
     * @param rot new condition of rotate.
     * @param maxS new maxsize for rotation.
     */
    public void setRotation(boolean rot, long maxS){
        this.isRotation = rot;
        this.maxSize = maxS;
    }
    
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
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
        return "RollingFileAppender{" + "name=" + name + ", classname=" + classname + ", layout=" + layout
                + ", rotation="+isRotation+", maxSize="+maxSize+", changeXML="+changeXML+'}';
    }
    //End of the class
}
