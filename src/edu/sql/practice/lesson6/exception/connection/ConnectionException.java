package edu.sql.practice.lesson6.exception.connection;

public class ConnectionException extends RuntimeException {

    public ConnectionException() {
        this("Connection is failed");
    }

    public ConnectionException(String message) {
        super(message);
    }
}
