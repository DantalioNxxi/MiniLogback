
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import myminilogback.*;
import myminilogback.Logger;
import org.junit.Test;

/**
 *
 * @author DantalioNxxi
 */
public class LogRacing {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yy HH:mm:ss,SSS");
    public LogRacing() {
    }

     @Test
     public void racing() {
         System.out.println("TEST RACING START:");
         
         myminilogback.Logger l1 = myminilogback.LoggerManager.getLogger("LogRacing");
         myminilogback.Logger l2 = myminilogback.LoggerManager.getLogger("Logger2");
         
         
         
//         myminilogback.LoggerManager.showInfo();

//tryings for rotations
            l1.info("Попытка логгера с пер. 1 номер 1");
            l2.info("Попытка логгера с пер. 2 номер 2");
            l2.info("Попытка логгера с пер. 2 номер 3");
            l2.info("Попытка логгера с пер. 2 номер 4");
//            l2.info("Попытка логгера с пер. 2 номер 5");
//            l1.info("Попытка логгера с пер. 2 номер 2");
//            l2.info("Попытка логгера с пер. 2 номер 3");
//            l1.info("Попытка логгера с пер. 2 номер 4");
//            l2.info("Попытка логгера с пер. 2 номер 5");
//            l1.info("Попытка логгера с пер. 2 номер 2");
//            l2.info("Попытка логгера с пер. 2 номер 3");
//            l1.info("Попытка логгера с пер. 2 номер 4");
//            l2.info("Попытка логгера с пер. 2 номер 5");
//            l1.info("Попытка логгера с пер. 2 номер 2");
//            l2.info("Попытка логгера с пер. 2 номер 3");
//            l1.info("Попытка логгера с пер. 2 номер 4");
//            l2.info("Попытка логгера с пер. 2 номер 5");

            LoggerManager.showInfo();
//        for (int i = 1; i <= 10; i++) {
//            Thread t = new Thread(new RacerOut("Racer "+i), "Racer"+i);
//            t.start();
//            l1.info("Попытка логгера 1 номер 1");
//            l2.info("Попытка логгера 2 номер 2");
//            l1.info("Попытка логгера 1 номер 3");
//            l2.info("Попытка логгера 2 номер 4");

//            System.out.println(SDF.format(new Date())+"Попытка потока "+Thread.currentThread().getName()+" номер 1");
//            System.out.println(SDF.format(new Date())+"Попытка потока "+Thread.currentThread().getName()+" номер 2");
//            System.out.println(SDF.format(new Date())+"Попытка потока "+Thread.currentThread().getName()+" номер 3");
//            System.out.println(SDF.format(new Date())+"Попытка потока "+Thread.currentThread().getName()+" номер 4");
//        }
//         Thread t1 = new Thread(new RacerOut("Jochan"), "Jochan");
//         t1.start();
//         Thread t2 = new Thread(new RacerOut("Michael"), "Michael");
//         t2.start();
//         Thread t3 = new Thread(new RacerOut("Stiven"), "Stiven");
//         t3.start();
         
//         try{
//             Thread.sleep(100);
//         } catch (InterruptedException ex){
//             System.out.println("Главный поток прерван раньше завершения! "+ex.getMessage());
//         }
         
//         for(int i=1;i<30;i++){
//             l1.info("Попытка логгера 1 номер 2");
//            l1.fatal("fatal");
//            l2.info("Попытка логгера 2 номер 1");
//            l2.info("Попытка логгера 2 номер 2");
//         }
         
//         l1.info("Попытка логгера 1 номер 1");
//         l1.info("Попытка логгера 1 номер 2");
//         l1.fatal("fatal");
//         l2.info("Попытка логгера 2 номер 1");
//         l2.info("Попытка логгера 2 номер 2");
         
//         l1.showInfo();
//         l2.showInfo();
         
         try{
             Thread.sleep(800);
         } catch (InterruptedException ex){
             System.out.println("ГЛАВНЫЙ ПОТОК ПРЕРВАН");
         } finally {
//             l2.warning("Message",
//                     new IOException("Сообщение Исключения",
//                             new InvocationTargetException(new FileNotFoundException())));
//             System.out.println(SDF.format(new Date())+"Исключение потока "+Thread.currentThread().getName());
        }
//         l2.setLevel(Logger.LogLevel.WARN);
//         l2.info("Попытка логгера 2 номер 3");
//         System.out.println(SDF.format(new Date())+"Попытка потока "+Thread.currentThread().getName()+" WARN");
//         System.out.println(SDF.format(new Date())+"Попытка потока "+Thread.currentThread().getName()+" номер 3");
         
         
//         while(true){
//             threads.
//                     stop
//             appender.close();
//             thr.sleep(1000);
//             break;
//         }
         
//         try {
//             System.out.println("Oжидaниe завершения потоков.");
//             t1.join();
//             t2.join();
//             t3.join();
//         } catch (InterruptedException ex) {
//             System.out.println("Глaвный поток прерван " + ex.getMessage());
//         }
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
//        Logger l3 = LoggerManager.getLogger("root");
//        Logger l3 = LoggerManager.getLogger("LogRacing");

//        Logger l4 = LoggerManager.getLogger("Logger2");
//        Logger l4 = LoggerManager.getLogger("l4");
//        System.out.println(SDF.format(new Date())+"Попытка потока "+Thread.currentThread().getName()+" номер 1");
//        System.out.println(SDF.format(new Date())+"Попытка потока "+Thread.currentThread().getName()+" номер 2");

//        Thread.currentThread().setName(name);
        for(int i =1; i<=kilometers; i++){
//            try {
//                Thread.sleep(10);
//            } catch(InterruptedException ex){
//                System.out.println("Error at way: "+ex.getMessage());
//            }
//            System.out.println("Гонщик "+name+" на "+i+" километре");

//            l3.warning("warning");
//            l4.info("Гонщик "+name+" на "+i+" километре");
//            l3.fatal("fatal");
//            System.out.println(SDF.format(new Date())+"Попытка потока "+Thread.currentThread().getName()+" warning");
//            System.out.println(SDF.format(new Date())+"Гонщик "+name+" на "+i+" километре");
//            System.out.println(SDF.format(new Date())+"Попытка потока "+Thread.currentThread().getName()+"fatal");
            
            
                
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
//        try{
//                
//                supp.join();
//            } catch (InterruptedException ex){
//                System.out.println("Ошибка в выполнении supp.join()");
//            } 
//            catch (IllegalThreadStateException ex){
//                System.out.println("Ошибка в выполнении supp.start() и supp.join(): "+ex.getMessage());
//            }
//        temp(l3);
    }
    
    private void temp(Logger l){
        System.out.println("Это приватный метод");
        l.info("Сообщение в приватном методе.");
        
    }
}

class Supporter implements Runnable{
    String racer;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yy HH:mm:ss,SSS");
    
    public Supporter(String racer) {
        this.racer = racer;
    }
    
    @Override
    public void run() {
        Logger supporter = LoggerManager.getLogger("LogRacing");
        Logger looker = LoggerManager.getLogger("Logger2");
        
//        supporter.info("Пит-Стоп для "+racer);
//        looker.fatal(racer+" вперёд!");
//        supporter.fatal("Пит-Стоп для "+racer);
//        looker.fatal(racer+" вперёд!");

//        System.out.println(SDF.format(new Date())+"Пит-Стоп для "+racer);
//        System.out.println(SDF.format(new Date())+"racer+" вперёд!");
//        System.out.println(SDF.format(new Date())+"Пит-Стоп для "+racer);
//        System.out.println(SDF.format(new Date())+"racer+" вперёд!");
    }
    
}
