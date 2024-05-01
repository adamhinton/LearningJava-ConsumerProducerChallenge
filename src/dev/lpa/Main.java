package dev.lpa;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

record Order(long orderId, String item, int qty) {
};

// Add an ES to main to send orders to warehouse
// add ES to SW which will fulfill orders
// Create and fulfill 15 shoe orders

public class Main {

    private static final Random random = new Random();

    public static void main(String[] args) {

        ShoeWarehouse warehouse = new ShoeWarehouse();

        ExecutorService orderingService = Executors.newCachedThreadPool();

        Callable<Order> orderingTask = () ->{
            {
                Order newOrder = generateOrder();
                try{
                    Thread.sleep(random.nextInt(500, 5000));
                    warehouse.receiveOrder(newOrder);
                }
                catch(InterruptedException e){
                    throw new RuntimeException(e);
                }
                return newOrder;
            }
        };

        List<Callable<Order>> tasks = Collections.nCopies(15, orderingTask);
        try {
            orderingService.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        orderingService.shutdown();
        try {
            orderingService.awaitTermination(7, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        warehouse.shutDown();

    }


    private static Order generateOrder(){
        return new Order(
                random.nextLong(1000000, 9999999),
                ShoeWarehouse.PRODUCT_LIST[random.nextInt(0, 5)],
                // bw 1-3
                random.nextInt(1, 4));
    }
}