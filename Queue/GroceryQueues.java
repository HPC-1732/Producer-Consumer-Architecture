package Queue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import Consumer.Cashier;
import Model.Customer;
import Producer.QueueSimulator;

public class GroceryQueues {
    private final List<Cashier> cashiers;
    private final int numCashiers;
    private final ReentrantLock lock = new ReentrantLock();
    Thread[] CashierThread;

    public GroceryQueues(int numCashiers, int maxQueueLength, QueueSimulator simulator) {
        System.out.println("GroceryQueue starts.\n");
        this.cashiers = new ArrayList<>();
        this.numCashiers = numCashiers;
        this.CashierThread = new Thread[numCashiers];
        for (int i = 0; i < numCashiers; i++) {
            Cashier cashier = new Cashier(maxQueueLength, simulator, i);
            cashiers.add(cashier);
            CashierThread[i] = new Thread(cashier); // Start each Cashier thread
        }
    }

    public void start() {
        for (int i = 0; i < numCashiers; i++) {
            CashierThread[i].start();
        }
    }

    public void addCustomer(Customer customer) {
        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            boolean added = false;

            while (System.currentTimeMillis() - startTime < 10000 && !added) { // Wait for up to 10 seconds
                lock.lock();
                try {
                    Cashier bestCashier = null;

                    // Find the cashier with the fewest number of people waiting
                    for (Cashier cashier : cashiers) {
                        if (bestCashier == null || cashier.getQueueSize() < bestCashier.getQueueSize()) {
                            bestCashier = cashier;
                        }
                    }

                    // Check if the best cashier's queue has space
                    if (bestCashier != null && bestCashier.getQueueSize() < bestCashier.getMaxQueueLength()) {
                        bestCashier.addCustomer(customer);
                        added = true;
                    }
                } finally {
                    lock.unlock();
                }

                try {
                    Thread.sleep(100); // Check every 100 milliseconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (!added) {
                System.out.printf("Customer departed from GroceryQueue which arrived at %d\n\n", customer.getArrivalTime());
                customer.setDeparted(true);
            }
        }).start();
    }

    public void stopAllCashiers() {
        for (Cashier cashier : cashiers) {
            cashier.stop();
        }
        for(int i = 0; i < numCashiers; i++) {
            try {
                CashierThread[i].interrupt();
                CashierThread[i].join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
