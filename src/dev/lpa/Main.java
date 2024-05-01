package dev.lpa;

import java.util.Random;

record Order(long orderId, String item, int qty) {
};

// Add an ES to main to send orders to warehouse
// add ES to SW which will fulfill orders
// Create and fulfill 15 shoe orders

public class Main {

    private static final Random random = new Random();

    public static void main(String[] args) {

        ShoeWarehouse warehouse = new ShoeWarehouse();


    }


    private static Order generateOrder(){
        return new Order(
                random.nextLong(1000000, 9999999),
                ShoeWarehouse.PRODUCT_LIST[random.nextInt(0, 5)],
                // bw 1-3
                random.nextInt(1, 4));
    }
}