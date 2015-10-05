import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Producer implements Runnable {

  private ProductionLine queue;
  private int id;
  private static Lock lock = new ReentrantLock();

  public Producer(int id, ProductionLine queue) {
    this.id    = id;
    this.queue = queue;
  }

  public void run() {
    int count = 0;
      while (count < 20){
        lock.lock();
        Product p = new Product();
        lock.unlock();
        String msg = "Producer %d Produced: %s on iteration %d";
        System.out.println(String.format(msg, id, p, count));
        queue.append(p);
        count++;
      }

    lock.lock();
    Product p = new Product();
    p.productionDone();
    lock.unlock();
    queue.append(p);
    String msg = "Producer %d is done. Shutting down.";
    System.out.println(String.format(msg, id));
  }

}
