package edu.sql.practice.lesson6.exception.product;

public class ProductNotCreatedException extends RuntimeException {

    public ProductNotCreatedException() {
        this("Product not created");
    }

    public ProductNotCreatedException(String message) {
        super(message);
    }
}
