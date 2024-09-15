package Consumer;
import Model.Customer;
import Queue.BankQueue;

public class Teller implements Runnable {
    private final BankQueue bankQueue;
    private final int id;

    public Teller(BankQueue bankQueue, int id) {
        this.bankQueue = bankQueue;
        this.id = id;
    }

    @Override
    public void run() {
        System.out.printf("Teller %d is starting!\n", id);
        while (true) {
            Customer customer = bankQueue.getCustomerForService();
            if (customer == null) {
                break; // Stop if the queue manager has stopped
            }
            System.out.printf("Teller %d is taking new customer with service time : %d\n\n", id, customer.getServiceTime());
            if (!customer.isDeparted()) {
                customer.setServedTime(System.currentTimeMillis());
                customer.setServed(true);
                try {
                    Thread.sleep(customer.getServiceTime() * 1000); // Simulate service time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                // System.out.printf("Teller %d ends work\n", id);
            }
        }
        System.out.printf("Teller %d ends its works!\n", id);
    }
}
