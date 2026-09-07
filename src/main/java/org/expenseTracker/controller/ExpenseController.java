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
    public @NonNull ResponseEntity<?> getExpenses() {
        try {
            List<Expense> expenses = expenseService.getExpenses();
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity
                    .status(500).body("server error");
        }
    }

    @PostMapping("/add-expense")
    public ResponseEntity<?> addExpense(@RequestBody Expense expense) {
        try {
            Expense savedExpense = expenseService.addExpense(expense);
            return ResponseEntity.ok(savedExpense);
        } catch (Exception e) {
            return ResponseEntity
                    .status(500).body("server error");
        }
    }

    @PostMapping("/update-expense/{id}")
    public @NonNull ResponseEntity<?> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        try {
            Expense updatedExpense = expenseService.updateExpense(id, expense);
            return ResponseEntity.ok(updatedExpense);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Expense not found");
        }
    }

    @DeleteMapping("/delete-expense/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {
        try {
            expenseService.deleteExpense(id);
            return ResponseEntity.ok("Expense deleted successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .status(500).body("server error");
        }
    }

}
