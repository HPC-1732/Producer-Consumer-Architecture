package Queue;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import Model.Customer;
import java.util.concurrent.locks.Condition;

public class BankQueue implements Runnable {
    private final int maxQueueLength;
    private final Queue<Customer> queue;
    private final Lock lock;
    private final Condition queueNotEmpty;
    private volatile boolean running;

    public BankQueue(int maxQueueLength) {
        this.maxQueueLength = maxQueueLength;
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.queueNotEmpty = lock.newCondition();
        this.running = true;
    }

    public boolean addCustomer(Customer customer) {
        lock.lock();
        try {
            if (queue.size() < maxQueueLength) {
                queue.offer(customer);
                queueNotEmpty.signal(); // Signal tellers that a customer is available
                return true;
            } else {
                customer.setDeparted(true);
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    public Customer getCustomerForService() {
        lock.lock();
        try {
            while (queue.isEmpty() && running) {
                queueNotEmpty.await(); // Wait until a customer is available
            }
            if(running == false) return null;
            return queue.poll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlock();
        }
    }

    public void stop() {
        lock.lock();
        try {
            running = false;
            queueNotEmpty.signalAll(); // Wake up all waiting tellers
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        // System.out.println("Thread BankQueue is running!");
        // This thread just manages the queue and waits for new customers
        while (running) {
            try {
                Thread.sleep(1000); // Just keep this thread alive
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("BankQueue stops!\n");   
    }
}
