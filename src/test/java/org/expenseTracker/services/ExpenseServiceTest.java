package org.expenseTracker.services;

import org.expenseTracker.entity.Expense;
import org.expenseTracker.exceptionHandling.ExpenseNotFound;
import org.expenseTracker.repository.ExpenseRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExpenseServiceTest {

    private final ExpenseRepository expenseRepository = mock(ExpenseRepository.class);
    private final ExpenseService expenseService = new ExpenseService(expenseRepository);

    @Test
    public void testGetExpenses() {
        List<Expense> expenses = new ArrayList<>();
        Expense e = new Expense();
        e.setId(1L);
        expenses.add(e);
        when(expenseRepository.findAll()).thenReturn(expenses);

        List<Expense> result = expenseService.getExpenses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    public void testGetExpensesEmptyList() {
        when(expenseRepository.findAll()).thenReturn(Collections.emptyList());

        List<Expense> result = expenseService.getExpenses();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAddExpense() {
        Expense expense = new Expense();
        expense.setCategory("Food");
        expense.setAmount(500);
        expense.setDate("2026-01-01");
        expense.setExpenseType("NECES");
        when(expenseRepository.save(any())).thenReturn(expense);

        Expense savedExpense = expenseService.addExpense(expense);

        assertNotNull(savedExpense);
        verify(expenseRepository).save(expense);
    }

    @Test
    public void testUpdateExpenseSuccess() throws ExpenseNotFound {
        Long id = 1L;
        Expense existingExpense = new Expense();
        existingExpense.setId(id);
        existingExpense.setCategory("Old");
        existingExpense.setAmount(100);
        existingExpense.setDate("2025-01-01");
        existingExpense.setExpenseType("OLD");

        Expense updatedFields = new Expense();
        updatedFields.setCategory("Transport");
        updatedFields.setAmount(300);
        updatedFields.setDate("2026-09-07");
        updatedFields.setExpenseType("NECES");

        when(expenseRepository.findById(id)).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Expense result = expenseService.updateExpense(id, updatedFields);

        assertNotNull(result);
        assertEquals("Transport", result.getCategory());
        assertEquals(300, result.getAmount());
        assertEquals("2026-09-07", result.getDate());
        assertEquals("NECES", result.getExpenseType());
        verify(expenseRepository).save(existingExpense);
    }

    @Test
    public void testUpdateExpenseNotFound() throws ExpenseNotFound {
        Long id = 99L;
        when(expenseRepository.findById(id)).thenReturn(Optional.empty());

        ExpenseNotFound ex = assertThrows(ExpenseNotFound.class,
                () -> expenseService.updateExpense(id, new Expense()));
        assertEquals("Expense not found", ex.getMessage());
    }

    @Test
    public void testDeleteExpense() {
        Long id = 1L;

        expenseService.deleteExpense(id);

        verify(expenseRepository).deleteById(id);
    }
}
