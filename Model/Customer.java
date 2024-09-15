package Model;
import java.util.concurrent.ThreadLocalRandom;

public class Customer {
    private final long arrivalTime;
    private final int serviceTime;
    private long servedTime;
    private boolean served;
    private boolean departed;

    public Customer(long arrivalTime) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = ThreadLocalRandom.current().nextInt(60, 300);
        this.servedTime = arrivalTime;
        this.served = false;
        this.departed = false;
    }

    public Customer(Customer copy) {
        this.arrivalTime = copy.getArrivalTime();
        this.serviceTime = copy.getServiceTime();
        this.servedTime = copy.getServedTime();
        this.served = false;
        this.departed = false;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServedTime(long servedTime) {
        this.servedTime = servedTime;
    }
    public long getServedTime() {
        return servedTime;
    }

    public boolean isServed() {
        return served;
    }

    public void setServed(boolean served) {
        this.served = served;
    }

    public boolean isDeparted() {
        return departed;
    }

    public void setDeparted(boolean departed) {
        this.departed = departed;
    }
}
