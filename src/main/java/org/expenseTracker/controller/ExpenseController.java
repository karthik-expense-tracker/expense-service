package org.expenseTracker.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.expenseTracker.entity.Expense;
import org.expenseTracker.exceptionHandling.*;
import org.expenseTracker.services.ExpenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExpenseController {
    private static final Logger log = LoggerFactory.getLogger(ExpenseController.class);

    public ExpenseService expenseService;
    public Validator validator;

    public ExpenseController(ExpenseService expenseService, Validator validator){
        this.validator = validator;
        this.expenseService = expenseService;
    }

    @GetMapping("/get-expenses")
    public @NonNull ResponseEntity<?> getExpenses() {
        try {
            List<Expense> expenses = expenseService.getExpenses();
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            log.error("Failed to get expenses", e);
            return ResponseEntity
                    .status(500).body("server error");
        }
    }

    @PostMapping("/add-expense")
    public ResponseEntity<?> addExpense(@RequestBody Expense expense) {
        try {
            validator.validateExpenseDetails(expense);
            Expense savedExpense = expenseService.addExpense(expense);
            return ResponseEntity.ok(savedExpense);
        } catch (InvalidAmountInExpense | InvalidDate | InvalidExpenseType e) {
            log.warn("Invalid expense payload: {}", e.getMessage());
            return ResponseEntity
                    .status(400).body(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to add expense", e);
            return ResponseEntity
                    .status(500).body("server error");
        }
    }

    @PostMapping("/update-expense/{id}")
    public @NonNull ResponseEntity<?> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        try {
            validator.validateExpenseDetails(expense);
            Expense updatedExpense = expenseService.updateExpense(id, expense);
            return ResponseEntity.ok(updatedExpense);
        } catch (InvalidAmountInExpense | InvalidDate | InvalidExpenseType e) {
            log.warn("Invalid expense payload: {}", e.getMessage());
            return ResponseEntity
                    .status(400).body(e.getMessage());
        } catch (ExpenseNotFound e) {
            log.warn("Expense with id {} was not found", id);
            return ResponseEntity
                    .status(404).body(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to update expense with id {}", id, e);
            return ResponseEntity.status(500).body("server error");
        }
    }

    @DeleteMapping("/delete-expense/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {
        try {
            expenseService.deleteExpense(id);
            return ResponseEntity.ok("Expense deleted successfully");
        } catch (Exception e) {
            log.error("Failed to delete expense with id {}", id, e);
            return ResponseEntity
                    .status(500).body("server error");
        }
    }

}
