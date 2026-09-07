package org.expenseTracker.exceptionHandling;

public class InvalidDate extends Exception {
    public InvalidDate(String message) {
        super(message);
    }
}
