package edu.sql.practice.lesson6.exception.order;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException() {
        this("Order not found");
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
