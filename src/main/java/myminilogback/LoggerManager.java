
package myminilogback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * At the static block the reading XML-document is happening.
 * The parsing is happening by DOM-parser.
 * Also through this class is creating and getting loggers. 
 * @author DantalioNxxi
 * @see Logger
 * @see java.nio
 * @see javax.xml.parsers
 * @see org.w3c.dom
 */
public class LoggerManager {
    
    private static Map<String, Logger> loggers;
    private static Map<String, Appendable> appenders;
    
    /**
     * Reference to logback.xml or default-logback.xml into framework's package.
     */
    private static File config;
    
    /**
     * Filter uses by methods, which check DirectoryStream.
     * @see DirectoryStream
     */
    private static DirectoryStream.Filter<Path> filter = (x)->Files.isDirectory(x, LinkOption.NOFOLLOW_LINKS);
    
    /**
     * Static block.
     */
    static {
        //sync is it need here?
        synchronized (LoggerManager.class){
            loggers = new HashMap<>();
            appenders = new HashMap<>();

            //At first founds logback.xml
            if (!new File("logback.xml").exists()) {
                System.out.println("Ошибка ввода вывода: Файл с именем logback.xml не был найден.");
                System.out.println("Будет использован файл по умолчанию:");
                config = getConfigFile();
                System.out.println("config = "+config);
            } else {
                System.out.println("Будет использован logback.xml");
                config = new File("logback.xml");
            }
            
            //Thereafter fills the HashMaps of appenders and loggers.
            try (FileInputStream fis = new FileInputStream(config))
            {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); // still here
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(fis); //still so
                doc.getDocumentElement().normalize();

                int configuration = doc.getElementsByTagName("configuration").getLength();
                if (configuration!=1){
                    throw new XMLLoggerException("Должен быть только один корневой тег <configuration>!");
                }

                NodeList apps = doc.getElementsByTagName("appender");
                getAppenders(apps);

                NodeList roots = doc.getElementsByTagName("root");
                if (roots.getLength()>1) throw new XMLLoggerException("Может быть только один корневой логгер!");
                
                else {
                    getRootLogger(roots.item(0));
                }

                NodeList loggs = doc.getElementsByTagName("logger");
                getLoggers(loggs);

            } catch (ParserConfigurationException ex){
                System.out.println("Не удалось получить DocumentBuilder: "+ex.getMessage());
            } catch (SAXException ex) {
                System.out.println("Не удалось распарсить документ XML: "+ex.getMessage());
            } catch (FileNotFoundException ex) {
                System.out.println("Ошибка ввода-вывода: "+ex.getMessage());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (XMLLoggerException ex){
                System.out.println("Документ XML некорректно заполнен или повреждён!");
                System.out.println(ex.getMessage());
            }
        }
    }
    
    /**
     * At the static block the reading XML-document is happening.
     * If in root directory 'logback.xml' had not found,
     * then searching will have continued in suitable child-directoryes,
     * which must contain myminiloback package. Package includes default-logback.xml.
     * After will happen the parsing of XML-file, and by him result doc
     * will have filling HashMaps 'loggers' and 'appenders'.
     */
    private LoggerManager(){}
    
    /**
     * Searchs default-config.xml file, because logback.xml had not founded.
     * @return reference to default-config.xml file.
     * @see LoggerManager#searchConfig(java.nio.file.Path) 
     * @see Path
     * @see Files
     */
    private static File getConfigFile(){
        Path rootDir = new File(".").toPath();
        try (DirectoryStream<Path> stream =
                Files.newDirectoryStream(rootDir, "*{src, myminilogback, main, java}*")) {
            for (Path entry: stream) {
                if (entry.getFileName().toString().equals("myminilogback")){
                    config = entry.resolve("defaultlogback/logback-default.xml").toFile();
                    break;
                } else {
                    searchConfig(entry);
                }
            }
        } catch (IOException | DirectoryIteratorException x) {
            System.err.println(x);
        }
        return config;
    }
    
    /**
     * Searhes default-config.xml file by recoursive method.
     * @param dir in which will be searching.
     * @see Path
     * @see Files
     */
    private static void searchConfig(Path dir){
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, filter)) {
            for (Path entry: stream) {
                if (entry.getFileName().toString().equals("myminilogback")){
                    config = entry.resolve("defaultlogback/logback-default.xml").toFile();
                    break;
                } else {
                    searchConfig(entry);
                }
            }
        } catch (IOException x) {
            System.out.println("Ошибка в поиске конфигурационного файла default-logback.xml"+x.getMessage());
        }
    }
    
    /**
     * Fills HashMap 'appenders' from NodeList 'apps'.
     * Uses switch-case construction for searching suitable appender's classname.
     * @param apps nodelist with tag 'appender', which had gotten at parsing xml-file.
     * @see Node
     * @see Element
     */
    private static void getAppenders(NodeList apps){
        for (int a = 0; a < apps.getLength(); a++) {
            Node node = apps.item(a);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element appElement = (Element) node;
                try{
                    if (appElement.hasAttribute("class")){
                        String classname = appElement.getAttribute("class");
                        System.out.println("Класс "+a+"-того: "+classname);
                        switch(classname){
                            case "FileAppender":
                                try{
                                    createFileAppender(node);
                                } catch (XMLLoggerException ex){
                                    System.out.println("Ошибка в создании FileAppender: "+ex.getMessage());
                                }
                                break;
                            case "ConsoleAppender":
                                try{
                                    createConsoleAppender(node);
                                } catch (XMLLoggerException ex){
                                    System.out.println("Ошибка в создании ConsoleAppender: "+ex.getMessage());
                                }
                                break;
                            case "RollingFileAppender":
                                try{
                                    createRollingFileAppender(node);
                                } catch (XMLLoggerException ex){
                                    System.out.println("Ошибка в создании RollingFileAppender: "+ex.getMessage());
                                }
                                break;
                            default:
                                break;
                            }
                    } else throw new XMLLoggerException("Не найден атрибут \"class\"");
                } catch (XMLLoggerException ex){
                    System.out.println("Ошибка в обработке "+a+"-го аппендера: "+ex.getMessage());
                }
            }
        }
    }
    
    /**
     * Creates new File Appender from getting node.
     * @param node with tag atrubute 'classname' equals 'FileAppender', which had gotten at parsing xml-file.
     * @throws XMLLoggerException if atrubute 'name' has not founded.
     * @see FileAppender
     */
    private static void createFileAppender(Node node) throws XMLLoggerException{
        System.out.println("Создаём FileAppender");
        Element element = (Element)node;
        String name;
        Layout newLayout;
        File logfile = new File(element.getElementsByTagName("file").item(0).getTextContent());
        FileAppender newAppender = new FileAppender(logfile);
        
        if (element.hasAttribute("name")){
            name = element.getAttribute("name");
        } else throw new XMLLoggerException("Не найден атрибут \"name\"");
        
        if (element.getElementsByTagName("layout").getLength()>0){
            newLayout = getLayout(element.getElementsByTagName("layout").item(0));
            newAppender.setLayout(newLayout);
        } else newAppender.setLayout(new SimpleLayout());
        
        newAppender.classname = "FileAppender";
        newAppender.name = name;
        System.out.println("Создан аппендер: "+newAppender.toString());
        appenders.put(name, newAppender);
    }
    
    /**
     * Creates new Console Appender from getting node.
     * @param node with tag atrubute 'classname' equals 'ConsoleAppender', which had gotten at parsing xml-file.
     * @throws XMLLoggerException if atrubute 'name' has not founded.
     * @see ConsoleAppender
     */
    private static void createConsoleAppender(Node node) throws XMLLoggerException{
        System.out.println("Создаём ConsoleAppender");
        Element element = (Element)node;
        String name;
        Layout newLayout;
        ConsoleAppender newAppender = new ConsoleAppender();
        
        if (element.hasAttribute("name")){
            name = element.getAttribute("name");
        } else throw new XMLLoggerException("Не найден атрибут \"name\"");
        
        if (element.getElementsByTagName("layout").getLength()>0){
            newLayout = getLayout(element.getElementsByTagName("layout").item(0));
            newAppender.setLayout(newLayout);
        } else newAppender.setLayout(new SimpleLayout());
        
        newAppender.classname = "ConsoleAppender";
        newAppender.name = name;
        System.out.println("Создан аппендер: "+newAppender.toString());
        appenders.put(name, newAppender);//!later there is need to create instance class classname
    }
    
    /**
     * Creates new Rolling File Appender from getting node.
     * The method is requering at modification for catching all possible exceptions.
     * @param node with tag atrubute 'classname' equals 'RollingFileAppender', which had gotten at parsing xml-file.
     * @throws XMLLoggerException if atrubute 'name' has not founded.
     * @see RollingFileAppender
     */
    private static void createRollingFileAppender(Node node) throws XMLLoggerException{
        System.out.println("Создаём RollingFileAppender");
        Element element = (Element)node;
        String name;
        Layout newLayout;
        boolean rotate = false;
        boolean changeXML = false;
        long maxsize = 1_048_576L;//by default one megabyte
        File logfile = new File(element.getElementsByTagName("file").item(0).getTextContent());
        RollingFileAppender newAppender;
        
        if (element.hasAttribute("name")){
            name = element.getAttribute("name");
        } else throw new XMLLoggerException("Не найден атрибут \"name\"");
        
        if (element.getElementsByTagName("layout").getLength()>0){
            newLayout = getLayout(element.getElementsByTagName("layout").item(0));
        } else newLayout = new SimpleLayout();
        
        NodeList rotations = element.getElementsByTagName("rotation");
        
        if (rotations.getLength()>0){
            Node rotation = element.getElementsByTagName("rotation").item(0);
            System.out.println("rotation - тег найден хотя бы один!");
            
            System.out.println("Проверяем nodeName у rotation: "+rotation.getNodeName());
            Element erot = (Element)rotation;
            
            if (erot.hasAttributes()){
                System.out.println("Проверяем rot: "+erot.getTagName());
                //try-cathc errors
                rotate = Boolean.parseBoolean(erot.getAttribute("enabled"));
                changeXML = Boolean.parseBoolean(erot.getAttribute("changeXML"));
            }
            
            if (erot.hasChildNodes()){
                System.out.println("Childes is here");
                //here must be a check by try-catch-finally
                System.out.println("Получаем size:");
                maxsize = Long.parseLong(erot.getElementsByTagName("size").item(0).getTextContent());
                System.out.println("maxsize = "+maxsize);
            }
        }
        
        System.out.println("Пытаемся создать аппендер RollingFileAppender:");
        System.out.println("logfile: "+logfile.getPath());
        System.out.println("config: "+config.getPath());
        System.out.println("rotate: "+rotate);
        System.out.println("changeXML: "+changeXML);
        System.out.println("maxsize: "+maxsize);
        System.out.println("layout: "+newLayout);

        newAppender = new RollingFileAppender(logfile, config, rotate, maxsize, changeXML);
        System.out.println("Аппендер создали");
        newAppender.setLayout(newLayout);
        System.out.println("Лэйаут задали");
        newAppender.classname = "RollingFileAppender";
        newAppender.name = name;
        System.out.println("Создан аппендер: "+newAppender.toString());
        appenders.put(name, newAppender);
    }
    
    /**
     * Creates new Layout from Node with tag 'layout'
     * By default creates a new SimpleLayoutDefault.
     * @param laynode with tag 'layout', which had gotten at parsing xml-file.
     * @return reference to new layout.
     * @see Layout
     * @see SimpleLayout
     */
    private static Layout getLayout(Node laynode){
        String pattern;
        if (laynode.getNodeType() == Node.ELEMENT_NODE) {
            Element layElement = (Element) laynode;
            if (layElement.getElementsByTagName("Pattern").getLength()>0){
                pattern = layElement.getElementsByTagName("Pattern").item(0).getTextContent();
                return new SimpleLayout(pattern);
            }
        } return new SimpleLayout();
    }
    
    /**
     * Creates new root Logger from Node with tag 'logger' and atribute 'name' equals 'root'.
     * @param root with tag 'logger' and atribute 'name' equals 'root', which had gotten at parsing xml-file.
     * @see Logger
     */
    private static void getRootLogger(Node root){
        if (root.getNodeType() == Node.ELEMENT_NODE) {
            Element rootElement = (Element) root;

            String strlevel = rootElement.getAttribute("level").toUpperCase();
            Logger.LogLevel level;
            try{
                level = Logger.LogLevel.valueOf(strlevel);
            } catch (NullPointerException | IllegalArgumentException ex) {
                level = Logger.LogLevel.INFO;
            }
            
            NodeList arefs = rootElement.getElementsByTagName("appender-ref");
            String[] refs = getNameOfAppenders(arefs);

            Logger rootLogger = new Logger(level);
            for(String ref : refs){
                System.out.println("К логгеру root "+" добавляем аппендер "+ref);
                rootLogger.addAppender(appenders.get(ref));
            }
            loggers.put("root", rootLogger);
        }
    }
    
    /**
     * Computes String Array with references to appenders from NodeList.
     * @param apps node list of node with tag 'appender-ref'
     * @return String Array with references to appenders from NodeList
     * @see NodeList
     * @see Element
     */
    private static String[] getNameOfAppenders(NodeList apps){
        List<String> refs = new ArrayList<>();
        for (int r=0; r<apps.getLength();r++){
            Element ref = (Element)apps.item(r);
            refs.add(ref.getAttribute("ref"));
        }
        return refs.toArray(new String[refs.size()]);
    }
    
    /**
     * Fills HashMap 'loggers' from NodeList 'loggs'.
     * Fills root Logger in other method.
     * @param loggs nodelist with tag 'logger', which had gotten at parsing xml-file.
     * @see Node
     * @see Element
     * @see LoggerManager#getRootLogger(org.w3c.dom.Node) 
     */
    private static void getLoggers(NodeList loggs){
        for (int r = 0; r < loggs.getLength(); r++) {
            Node node = loggs.item(r);
            if (node.getNodeType() == Node.ELEMENT_NODE){
                Logger newLogger = getLogger(node);
                loggers.put(newLogger.getName(), newLogger);
            }
        }
    }
    
    /**
     * Creates new Logger from getting node.
     * @param node with tag atrubute 'logger', which had gotten at parsing xml-file.
     * @see Element
     * @see Node
     * @return new Logger
     */
    private static Logger getLogger(Node node){
        Element loggerElement = (Element) node;
        String name = loggerElement.getAttribute("name");
        String strlevel = loggerElement.getAttribute("level").toUpperCase();
        Logger.LogLevel level;
        try{
            level = Logger.LogLevel.valueOf(strlevel);
        } catch (NullPointerException | IllegalArgumentException ex) {
            level = Logger.LogLevel.INFO;
        }
        
        NodeList arefs = loggerElement.getElementsByTagName("appender-ref");
        String[] refs = getNameOfAppenders(arefs);
        
        Logger newLogger = new Logger(name, level);
        for(String ref : refs){
            System.out.println("К логгеру "+name+" добавляем аппендер "+ref);
            newLogger.addAppender(appenders.get(ref));
        }
        return newLogger;
    }
    
    /**
     * Gives the reference to logger with "name".
     * If such logger do not exists, manager will create a new logger
     * with reference to ConsoleAppender.
     * @param name of logger, which have to writed into XML-document.
     * @return reference to logger.
     */
    public static synchronized Logger getLogger(String name){
        if (loggers.containsKey(name)){
            return loggers.get(name);
        } else {
            System.out.println("Для логгера с именем "+name+" не задана конфигурация"
                    + ".\nБудет создан новый логгер со ссылкой на консольный аппендер.");
            Logger newLogger = new Logger(name);
            loggers.put(name, newLogger);
            getConsolAppenderToNewLogger(newLogger);
            return newLogger;
        }
    }
    
    /**
     * If new logger do not contains appenders,
     * then he will've get a new ConsoleAppender with name defaultConsoleAppender.
     * This method will not necessary, if the method for add appender by java code will has been setted.
     * @param logger new logger
     */
    private static void getConsolAppenderToNewLogger(Logger logger){
        for(Map.Entry<String, Appendable> a : appenders.entrySet()){
            if (a.getValue().getClassName().equals("ConsoleAppender")) {
                logger.addAppender(a.getValue());
            }
        }
        if (!logger.isContainsAppenders()) {
            ConsoleAppender ca = new ConsoleAppender(new SimpleLayout());
            ca.name = "defaultConsoleAppender";
            ca.classname = "ConsoleAppender";
            appenders.put(ca.name, ca);
            //здесь потом исправить на получение класса через рефлексию
            logger.addAppender(appenders.get(ca.name));
        }
    }
    
    public static synchronized void showInfo(){
        
        System.out.println("Было добавлено "+appenders.size()+" аппендеров:");
        for(Map.Entry<String, Appendable> a : appenders.entrySet()){
            System.out.println(a.getValue().toString());
        }
        
        System.out.println("Было добавлено "+loggers.size()+" логгеров:");
        for(Map.Entry<String, Logger> a : loggers.entrySet()){
            System.out.println(a.getValue().toString());
        }
        
        System.out.println("Ссылка на конфиг: "+config.getPath());
    }
    //End of the class
}
