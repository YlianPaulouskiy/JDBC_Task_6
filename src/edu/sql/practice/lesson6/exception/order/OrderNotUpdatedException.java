package edu.sql.practice.lesson6.exception.order;

public class OrderNotUpdatedException extends RuntimeException {

    public OrderNotUpdatedException() {
        this("Order not updated");
    }

    public OrderNotUpdatedException(String message) {
        super(message);
    }
}
