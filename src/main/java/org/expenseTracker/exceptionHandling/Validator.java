package org.expenseTracker.exceptionHandling;

import org.expenseTracker.entity.Expense;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator{
    private static final DateTimeFormatter EXPENSE_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public void validateExpenseDetails(Expense expense) throws InvalidAmountInExpense, InvalidDate, InvalidExpenseType {
        if(expense.getAmount() <= 0){
            throw new InvalidAmountInExpense("Invalid amount in expense");
        }
        LocalDate expenseDate;
        try {
            expenseDate = LocalDate.parse(expense.getDate(), EXPENSE_DATE_FORMAT);
        } catch (DateTimeParseException | NullPointerException e) {
            throw new InvalidDate("Invalid date, expected format dd-MM-yyyy");
        }
        if(expenseDate.isAfter(LocalDate.now())) {
            throw new InvalidDate("Future dates are not allowed");
        }

        String expenseType = expense.getExpenseType();
        if(!"credit".equals(expenseType) && !"debit".equals(expenseType)) {
            throw new InvalidExpenseType("Invalid expense type accepts only credit or debit");
        }
    }
}
