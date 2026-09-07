package org.expenseTracker.exceptionHandling;

public class InvalidAmountInExpense extends Exception {
    public InvalidAmountInExpense(String message) {
        super(message);
    }
}
