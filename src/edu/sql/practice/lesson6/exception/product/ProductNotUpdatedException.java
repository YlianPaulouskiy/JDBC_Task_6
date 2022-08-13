package edu.sql.practice.lesson6.exception.product;

public class ProductNotUpdatedException extends RuntimeException {

    public ProductNotUpdatedException() {
        this("Product not updated");
    }

    public ProductNotUpdatedException(String message) {
        super(message);
    }
}
