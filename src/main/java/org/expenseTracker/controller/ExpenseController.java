package org.expenseTracker.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.expenseTracker.entity.Expense;
import org.expenseTracker.services.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExpenseController {
    @NonNull
    ExpenseService expenseService;

    @GetMapping("/get-expenses")
    public @NonNull List<Expense> getExpenses() {
        return expenseService.getExpenses();
    }

    @PostMapping("/add-expense")
    public @NonNull Expense addExpense(@RequestBody Expense expense) {
        return expenseService.addExpense(expense);
    }

    @PostMapping("/update-expense/{id}")
    public @NonNull Expense updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        return expenseService.updateExpense(id, expense);
    }

    @DeleteMapping("/delete-expense/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok("Expense deleted successfully");
    }

}
