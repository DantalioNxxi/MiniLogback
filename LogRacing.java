
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import myminilogback.*;
import myminilogback.Logger;
import org.junit.Test;

public class LogRacing {
//    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yy HH:mm:ss,SSS");
    public LogRacing() {
    }

     @Test
     public void racing() {
         System.out.println("TEST RACING START:");
         
         myminilogback.Logger l1 = myminilogback.LoggerManager.getLogger("LogRacing");
         myminilogback.Logger l2 = myminilogback.LoggerManager.getLogger("Logger2");
         myminilogback.LoggerManager.showInfo();
            l1.info("Попытка логгера с пер. 1 номер 1");
            l2.info("Попытка логгера с пер. 2 номер 2");
            l2.info("Попытка логгера с пер. 2 номер 3");
            l2.info("Попытка логгера с пер. 2 номер 4");
//            LoggerManager.showInfo();
        for (int i = 1; i <= 10; i++) {
            Thread t = new Thread(new RacerOut("Racer "+i), "Racer"+i);
            t.start();
            l1.info("Попытка логгера 1 номер 1");
            l2.info("Попытка логгера 2 номер 2");
            l1.info("Попытка логгера 1 номер 3");
            l2.info("Попытка логгера 2 номер 4");
        }
         
         for(int i=1;i<30;i++){
             l1.info("Попытка логгера 1 номер 2");
            l1.fatal("fatal");
            l2.info("Попытка логгера 2 номер 1");
            l2.info("Попытка логгера 2 номер 2");
         }
         
         l1.info("Попытка логгера 1 номер 1");
         l1.info("Попытка логгера 1 номер 2");
         l1.fatal("fatal");
         l2.info("Попытка логгера 2 номер 1");
         l2.info("Попытка логгера 2 номер 2");
         
            l2.info("Попытка логгера с пер. 2 номер 5");
            l1.info("Попытка логгера с пер. 2 номер 2");
            l2.info("Попытка логгера с пер. 2 номер 3");
            l1.info("Попытка логгера с пер. 2 номер 4");
            l2.info("Попытка логгера с пер. 2 номер 5");
            l1.info("Попытка логгера с пер. 2 номер 2");
            l2.info("Попытка логгера с пер. 2 номер 3");
            l1.info("Попытка логгера с пер. 2 номер 4");
            l2.info("Попытка логгера с пер. 2 номер 5");
            l1.info("Попытка логгера с пер. 2 номер 2");
            l2.info("Попытка логгера с пер. 2 номер 3");
            l1.info("Попытка логгера с пер. 2 номер 4");
            l2.info("Попытка логгера с пер. 2 номер 5");

         l1.showInfo();
         l2.showInfo();
         
         try{
             Thread.sleep(800);
         } catch (InterruptedException ex){
             System.out.println("ГЛАВНЫЙ ПОТОК ПРЕРВАН "+ex.getMessage());
         } finally {
             l2.warning("Message",
                     new IOException("Сообщение Исключения",
                             new InvocationTargetException(new FileNotFoundException())));
        }
         l2.setLevel(Logger.LogLevel.WARN);
         l2.info("Попытка логгера 2 номер 3");
     }
}

class RacerOut implements Runnable{
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yy HH:mm:ss,SSS");
    private static int kilometers = 10;
    private static Random r = new Random();
    
    private String name;
    private int kilometrage;
    private int speed;
    
    RacerOut(String name){
        this.name = name;
//        speed = r.nextInt(299);
    }

    @Override
    public void run() {
        Logger lr = LoggerManager.getLogger("root");
        Logger lrace = LoggerManager.getLogger("LogRacing");
        Logger l2 = LoggerManager.getLogger("Logger2");
        Logger l4 = LoggerManager.getLogger("Logger2");
//        Thread.currentThread().setName(name);
        for(int i =1; i<=kilometers; i++){
//            try {
//                Thread.sleep(10);
//            } catch(InterruptedException ex){
//                System.out.println("Error at way: "+ex.getMessage());
//            }
//            System.out.println("Гонщик "+name+" на "+i+" километре");
            l2.warning("warning");
            l4.info("Гонщик "+name+" на "+i+" километре");
            l2.fatal("fatal");
//            l4.info("Гонщик "+name+" на "+i+" километре");
//            int p = r.nextInt(20);
//            if (p<2) {
//                l3.fatal("Гонщик "+name+" попал в аварию", new Error("Фатальная ошибка"));
//                break;
//            }
//            kilometrage+=1;
        }
        
        Thread supp = new Thread(new Supporter(name), "Support for "+name);
        supp.start();
        try{
                
                supp.join();
            } catch (InterruptedException ex){
                System.out.println("Ошибка в выполнении supp.join()");
            } 
            catch (IllegalThreadStateException ex){
                System.out.println("Ошибка в выполнении supp.start() и supp.join(): "+ex.getMessage());
            }
        temp(l2);
    }
    
    private void temp(Logger l){
        System.out.println("Это приватный метод");
        l.info("Сообщение в приватном методе.");
        
    }
}

class Supporter implements Runnable{
    String racer;
//    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yy HH:mm:ss,SSS");
    public Supporter(String racer) {
        this.racer = racer;
    }
    
    @Override
    public void run() {
        Logger supporter = LoggerManager.getLogger("LogRacing");
        Logger looker = LoggerManager.getLogger("Logger2");
        
        supporter.info("Пит-Стоп для "+racer);
        looker.fatal(racer+" вперёд!");
        supporter.fatal("Пит-Стоп для "+racer);
        looker.fatal(racer+" вперёд!");
    }
}
