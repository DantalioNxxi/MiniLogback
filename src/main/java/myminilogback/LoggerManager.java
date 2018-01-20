
package myminilogback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
 * At the static block the reading XML-document happens
 * @author DantalioNxxi
 */
public class LoggerManager {
    
    //    private Document doc;
    private static Map<String, Logger> loggers;
    private static Map<String, Appendable> appenders;
    //    private static Map<String, Layout> layouts;
    
    static {
        
        loggers = new HashMap<>();
        appenders = new HashMap<>();
        
        try (FileInputStream fis = new FileInputStream("src/main/resources/logback.xml"))
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); // still here
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(fis); //still so //Document doc = db.parse(newFile); //still so
            doc.getDocumentElement().normalize();
            
            NodeList apps = doc.getElementsByTagName("appender");
            getAppenders(apps);
            
            NodeList roots = doc.getElementsByTagName("root");
            if (roots.getLength()>1) throw new XMLLoggerException("Может быть только один корневой логгер!");
            //Документ XML некорректно заполнен или повреждён
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
            System.out.println(ex.getMessage());
        }
    }
    
    private static void getAppenders(NodeList apps){
        for (int a = 0; a < apps.getLength(); a++) {
            Node node = apps.item(a);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element appElement = (Element) node;
                String name = appElement.getAttribute("name");
                String classname = appElement.getAttribute("class");
                
                Layout newLayout = getLayout(appElement.getElementsByTagName("layout"));
                
                AbstractAppender newAppender;
                if (classname.contains("File")){
                    File logfile = new File(appElement.getElementsByTagName("file").item(0).getTextContent());
                    newAppender = new FileAppender(logfile);
                    newAppender.setLayout(newLayout);
                    appenders.put(name, newAppender);//!later there is need to create instance class classname
                } else if (classname.contains("Console")){
                    newAppender = new ConsoleAppender();
                    newAppender.setLayout(newLayout);
                    appenders.put(name, newAppender);//!later there is need to create instance class classname
                    //rewrite repeatable code!
                }
            }
        }
    }
    
    private static Layout getLayout(NodeList lays){
        for (int l = 0; l < lays.getLength(); l++) {
            Node laynode = lays.item(l);
            if (laynode.getNodeType() == Node.ELEMENT_NODE) {
                Element layElement = (Element) laynode;
                NodeList patterns = layElement.getElementsByTagName("Pattern");
                    for(int p = 0; p < patterns.getLength(); p++){
                        Node patternNode = patterns.item(p);
                        if (patternNode.getNodeType() == Node.ELEMENT_NODE){ //if pattern is exist
                            System.out.println("Будет создан лейаут с seq: "+patternNode.getTextContent());
                            return new SimpleLayout(patternNode.getTextContent());
                        }
                    }
            }
        } return new SimpleLayout();
    }
    
    private static String[] getRefAppenders(NodeList apps){
        List<String> refs = new ArrayList<>();
        for (int r=0; r<apps.getLength();r++){
            Element ref = (Element)apps.item(r);
            refs.add(ref.getAttribute("ref"));
        }
        return refs.toArray(new String[refs.size()]);
    }
    
    private static Logger getLogger(Node node){
        Element loggerElement = (Element) node;
        String name = loggerElement.getAttribute("name");
        Logger.LogLevel level = Logger.LogLevel.valueOf(loggerElement.getAttribute("level"));
        
        NodeList arefs = loggerElement.getElementsByTagName("appender-ref");
        String[] refs = getRefAppenders(arefs);
        
        Logger newLogger = new Logger(name, level);
        for(String ref : refs){
            System.out.println("К логгеру "+name+" добавляем аппендер "+ref);
            newLogger.addAppender(appenders.get(ref));
        }
        return newLogger;
    }
    
    private static void getRootLogger(Node root){
        if (root.getNodeType() == Node.ELEMENT_NODE) {
            Element rootElement = (Element) root;
            Logger.LogLevel level = Logger.LogLevel.valueOf(rootElement.getAttribute("level"));

            NodeList arefs = rootElement.getElementsByTagName("appender-ref");
            String[] refs = getRefAppenders(arefs);

            Logger rootLogger = new Logger(level);
            for(String ref : refs){
                System.out.println("К логгеру root "+" добавляем аппендер "+ref);
                rootLogger.addAppender(appenders.get(ref));
            }
            loggers.put("root", rootLogger);
        }
    }
        
    private static void getLoggers(NodeList loggs){
        for (int r = 0; r < loggs.getLength(); r++) {
            Node node = loggs.item(r);
            if (node.getNodeType() == Node.ELEMENT_NODE){
                Logger newLogger = getLogger(node);
                loggers.put(newLogger.getName(), newLogger);
            }
        }
    }
    
    private LoggerManager(){
        
    }
    
    public static void showInfo(){
        
        System.out.println("Было добавлено "+appenders.size()+" аппендеров:\n"+appenders);
        
        System.out.println("Было добавлено "+loggers.size()+" логгеров\n"+loggers);
        
    }
    
    public static Logger getLogger(String name){
        if (loggers.containsKey(name)){
            return loggers.get(name);
        } else {
            System.out.println("Для логгера с именем "+name+" не задана конфигурация");
            Logger newLogger = new Logger(name);
            loggers.put(name, newLogger);
            return newLogger;
        }
    }
    
//    public <T> Logger getLogger(Class<T> cl){
//        //проверка на наличие классов в XML
//    }
    
    
    
}