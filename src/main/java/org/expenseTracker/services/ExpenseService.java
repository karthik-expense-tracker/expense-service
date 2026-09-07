package org.expenseTracker.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.expenseTracker.entity.Expense;
import org.expenseTracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    public final @NonNull ExpenseRepository expenseRepository;

    public @NonNull List<Expense> getExpenses() {
        return expenseRepository.findAll();
    }

    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public Expense updateExpense(Long id, Expense expense) {
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        existingExpense.setAmount(expense.getAmount());
        existingExpense.setCategory(expense.getCategory());
        existingExpense.setDate(expense.getDate());
        existingExpense.setExpenseType(expense.getExpenseType());

        return expenseRepository.save(existingExpense);
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}
