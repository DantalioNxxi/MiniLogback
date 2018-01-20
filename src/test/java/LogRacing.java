
import java.util.Random;
import myminilogback.*;
import org.junit.Test;

/**
 *
 * @author DantalioNxxi
 */
public class LogRacing {
    
    public LogRacing() {
    }

     @Test
     public void racing() {
         System.out.println("TEST RACING START:");
         
         myminilogback.Logger l1 = myminilogback.LoggerManager.getLogger("root");
         myminilogback.Logger l2 = myminilogback.LoggerManager.getLogger("src.test.java.LogRacing");
         
         myminilogback.LoggerManager.showInfo();
         
         Thread t1 = new Thread(new Racer("John"));
         t1.start();
         
         try{
             Thread.sleep(1000);
         } catch (InterruptedException ex){
             System.out.println("Главный поток прерван раньше завершения! "+ex.getMessage());
         }
         
         l1.info("Попытка логгера 1 номер 1");
         l1.info("Попытка логгера 1 номер 2");
         
         l2.info("Попытка логгера 2 номер 1");
         l2.info("Попытка логгера 2 номер 2");
         
         l1.showInfo();
         l2.showInfo();
         
         try {
             System.out.println("Oжидaниe завершения потоков.");
             t1.join();
         } catch (InterruptedException ex) {
             System.out.println("Глaвный поток прерван " + ex.getMessage());
         }
     }
}

class Racer implements Runnable{
    private static int kilometers = 10;
    private static Random r = new Random();
    
    private String name;
    private int kilometrage;
    private int speed;
    
    Racer(String name){
        this.name = name;
        speed = r.nextInt(99);
    }

    @Override
    public void run() {
        for(int i =0; i<kilometers; i++){
            try {
                Thread.sleep(100 - speed);
            } catch(InterruptedException ex){
                System.out.println("Error at way: "+ex.getMessage());
            }
            System.out.println("Гонщик "+name+" на "+i+" километре");
            kilometrage+=1;
        }
    }
}
