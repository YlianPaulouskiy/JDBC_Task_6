package edu.sql.practice.lesson6.exception.order;

public class OrderNotCreatedException extends RuntimeException {

    public OrderNotCreatedException() {
        this("Order not created");
    }

    public OrderNotCreatedException(String message) {
        super(message);
    }
}
