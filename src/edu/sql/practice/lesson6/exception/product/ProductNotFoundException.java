package edu.sql.practice.lesson6.exception.product;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
        this("Product not found");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
