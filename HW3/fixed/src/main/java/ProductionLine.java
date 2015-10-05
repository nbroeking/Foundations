import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProductionLine {

    private List<Product> products;
    private Lock lock;
    private Condition producerCondition;
    private Condition consumerCondition;


    public ProductionLine() {
        products = new LinkedList<Product>();
        lock = new ReentrantLock();
        producerCondition = lock.newCondition();
        consumerCondition = lock.newCondition();
    }

    public int size() {
        lock.lock();
        try{
            return products.size();
        }
        finally {
            lock.unlock();
        }
   }

    public void append(Product p) {

        lock.lock();
        try {
            while (products.size() >= 10) {
                producerCondition.await();
            }

            products.add(p);
            consumerCondition.signalAll();
        }
        catch (InterruptedException e){
            System.err.println("Could not add producer");
        }
        finally {
            lock.unlock();
        }
    }

    public Product retrieve() {
        lock.lock();
        try{
            while( products.size() <= 0){consumerCondition.await();}
            Product p = products.remove(0);
            producerCondition.signalAll();
            return p;
        }
        catch (InterruptedException e){
            System.err.println("Could not retrieve consumer");
        }
        finally {
            lock.unlock();
        }
        return null;
   }
}
