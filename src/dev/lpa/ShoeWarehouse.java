package dev.lpa;

import java.util.ArrayList;
import java.util.List;

public class ShoeWarehouse {

    private List<Order> shippingItems;
    public final static String[] PRODUCT_LIST =
            {"Running Shoes", "Sandals", "Boots", "Slippers", "High Tops"};

    public ShoeWarehouse(){
        this.shippingItems = new ArrayList<>();
    }

    public synchronized void receiveOrder (Order item){
        while(shippingItems.size() > 20){
            try{
                // Wait until more orders are processed before sending
                // Putting current thread in a wait queue and letting other threads access this lock
                wait();
            }
            catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }
        shippingItems.add(item);
        System.out.println("Incoming: " + item);
        notifyAll();
    }

    public synchronized Order fulfillOrder(){
        while (shippingItems.isEmpty()){
            try{
                wait();
            }
            catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }
        // Both retrieves first el and removes it from the list
        Order item = shippingItems.remove(0);
        System.out.println(Thread.currentThread().getName() + " Fulfilled: " + item);
        notifyAll();
        // item is an Order
        return item;
    }

}
