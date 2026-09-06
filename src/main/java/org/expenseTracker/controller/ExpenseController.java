package org.expenseTracker.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.expenseTracker.entity.Expense;
import org.expenseTracker.services.ExpenseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public @NonNull Expense addExpense(@RequestBody Expense expense){
        return expenseService.addExpense(expense);
    }
}
