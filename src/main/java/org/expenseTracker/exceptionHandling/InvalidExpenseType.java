package org.expenseTracker.exceptionHandling;

public class InvalidExpenseType extends Exception {
    public InvalidExpenseType(String message) {
        super(message);
    }
}
