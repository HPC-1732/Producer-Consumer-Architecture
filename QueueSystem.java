import Producer.QueueSimulator;

public class QueueSystem {
    public static void main(String[] args) throws InterruptedException {
        int numTellers = 3;
        int maxQueueLength = 5;
        int numCashiers = 3; 
        int maxGroceryQueueLength = 2;
        int simulationMinutes = 2; // 2 hours

        QueueSimulator simulator = new QueueSimulator(numTellers, maxQueueLength, numCashiers, maxGroceryQueueLength, simulationMinutes);
        simulator.simulate();
        simulator.printStatistics();
    }
}
