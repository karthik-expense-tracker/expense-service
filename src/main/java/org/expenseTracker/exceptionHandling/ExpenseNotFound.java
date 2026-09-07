package org.expenseTracker.exceptionHandling;

public class ExpenseNotFound extends Exception {
    public ExpenseNotFound(String expenseNotFound) {
        super(expenseNotFound);
    }
}
