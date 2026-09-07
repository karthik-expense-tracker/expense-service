package org.expenseTracker.exceptionHandling;

import org.expenseTracker.entity.Expense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    private final Validator validator = new Validator();

    private Expense validExpense() {
        Expense e = new Expense();
        e.setCategory("Food");
        e.setAmount(500);
        e.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        e.setExpenseType("debit");
        return e;
    }

    @Test
    public void validExpense_passes() throws Exception {
        assertDoesNotThrow(() -> validator.validateExpenseDetails(validExpense()));
    }

    @Test
    public void zeroAmount_throws() {
        Expense e = validExpense();
        e.setAmount(0);

        InvalidAmountInExpense ex = assertThrows(InvalidAmountInExpense.class,
                () -> validator.validateExpenseDetails(e));
        assertEquals("Invalid amount in expense", ex.getMessage());
    }

    @Test
    public void negativeAmount_throws() {
        Expense e = validExpense();
        e.setAmount(-10);

        assertThrows(InvalidAmountInExpense.class,
                () -> validator.validateExpenseDetails(e));
    }

    @Test
    public void futureDate_throws() {
        Expense e = validExpense();
        e.setDate(LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        InvalidDate ex = assertThrows(InvalidDate.class,
                () -> validator.validateExpenseDetails(e));
        assertEquals("Future dates are not allowed", ex.getMessage());
    }

    @Test
    public void todayDate_passes() throws Exception {
        Expense e = validExpense();
        assertDoesNotThrow(() -> validator.validateExpenseDetails(e));
    }

    @Test
    public void malformedDate_throws() {
        Expense e = validExpense();
        e.setDate("2026-01-01");

        InvalidDate ex = assertThrows(InvalidDate.class,
                () -> validator.validateExpenseDetails(e));
        assertEquals("Invalid date, expected format dd-MM-yyyy", ex.getMessage());
    }

    @Test
    public void nullDate_throws() {
        Expense e = validExpense();
        e.setDate(null);

        InvalidDate ex = assertThrows(InvalidDate.class,
                () -> validator.validateExpenseDetails(e));
        assertEquals("Invalid date, expected format dd-MM-yyyy", ex.getMessage());
    }

    @Test
    public void invalidExpenseType_throws() {
        Expense e = validExpense();
        e.setExpenseType("NECES");

        InvalidExpenseType ex = assertThrows(InvalidExpenseType.class,
                () -> validator.validateExpenseDetails(e));
        assertEquals("Invalid expense type accepts only credit or debit", ex.getMessage());
    }

    @Test
    public void nullExpenseType_throws() {
        Expense e = validExpense();
        e.setExpenseType(null);

        assertThrows(InvalidExpenseType.class,
                () -> validator.validateExpenseDetails(e));
    }
}
