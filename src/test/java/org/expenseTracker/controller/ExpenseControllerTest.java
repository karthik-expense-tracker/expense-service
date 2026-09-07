package org.expenseTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.expenseTracker.entity.Expense;
import org.expenseTracker.exceptionHandling.ExpenseNotFound;
import org.expenseTracker.exceptionHandling.InvalidAmountInExpense;
import org.expenseTracker.exceptionHandling.InvalidDate;
import org.expenseTracker.exceptionHandling.InvalidExpenseType;
import org.expenseTracker.exceptionHandling.Validator;
import org.expenseTracker.services.ExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
public class ExpenseControllerTest {

    @MockBean
    private ExpenseService expenseService;

    @MockBean
    private Validator validator;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Expense createExpense(Long id, String category, int amount, String date, String type) {
        Expense e = new Expense();
        e.setId(id);
        e.setCategory(category);
        e.setAmount(amount);
        e.setDate(date);
        e.setExpenseType(type);
        return e;
    }

    @Test
    public void getExpenses_success() throws Exception {
        Expense e1 = createExpense(1L, "Food", 500, "01-01-2026", "debit");
        Expense e2 = createExpense(2L, "Travel", 1200, "15-02-2026", "credit");
        when(expenseService.getExpenses()).thenReturn(Arrays.asList(e1, e2));

        mockMvc.perform(get("/all-expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Food"))
                .andExpect(jsonPath("$[0].amount").value(500))
                .andExpect(jsonPath("$[1].category").value("Travel"))
                .andExpect(jsonPath("$[1].amount").value(1200));
    }

    @Test
    public void getExpenses_error() throws Exception {
        when(expenseService.getExpenses()).thenThrow(new RuntimeException("db error"));

        mockMvc.perform(get("/all-expenses"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void addExpense_success() throws Exception {
        Expense input = createExpense(null, "Food", 500, "01-01-2026", "debit");
        Expense saved = createExpense(1L, "Food", 500, "01-01-2026", "debit");
        when(expenseService.addExpense(any())).thenReturn(saved);

        mockMvc.perform(post("/create-expense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.category").value("Food"))
                .andExpect(jsonPath("$.amount").value(500));
    }

    @Test
    public void addExpense_invalidAmount() throws Exception {
        doThrow(new InvalidAmountInExpense("Invalid amount in expense"))
                .when(validator).validateExpenseDetails(any());

        mockMvc.perform(post("/create-expense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid amount in expense"));
    }

    @Test
    public void addExpense_invalidDate() throws Exception {
        doThrow(new InvalidDate("Future dates are not allowed"))
                .when(validator).validateExpenseDetails(any());

        mockMvc.perform(post("/create-expense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Future dates are not allowed"));
    }

    @Test
    public void addExpense_malformedDate_returns400Not500() throws Exception {
        doThrow(new InvalidDate("Invalid date, expected format dd-MM-yyyy"))
                .when(validator).validateExpenseDetails(any());

        mockMvc.perform(post("/create-expense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid date, expected format dd-MM-yyyy"));
    }

    @Test
    public void addExpense_invalidType() throws Exception {
        doThrow(new InvalidExpenseType("Invalid expense type accepts only credit or debit"))
                .when(validator).validateExpenseDetails(any());

        mockMvc.perform(post("/create-expense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid expense type accepts only credit or debit"));
    }

    @Test
    public void addExpense_error() throws Exception {
        when(expenseService.addExpense(any())).thenThrow(new RuntimeException("save failed"));

        mockMvc.perform(post("/create-expense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void updateExpense_success() throws Exception {
        Expense input = createExpense(null, "Transport", 300, "07-09-2026", "debit");
        Expense updated = createExpense(1L, "Transport", 300, "07-09-2026", "debit");
        when(expenseService.updateExpense(anyLong(), any())).thenReturn(updated);

        mockMvc.perform(post("/update-expense/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("Transport"))
                .andExpect(jsonPath("$.amount").value(300));
    }

    @Test
    public void updateExpense_notFound() throws Exception {
        when(expenseService.updateExpense(anyLong(), any()))
                .thenThrow(new ExpenseNotFound("Expense not found"));

        mockMvc.perform(post("/update-expense/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Expense not found"));
    }

    @Test
    public void updateExpense_invalidAmount() throws Exception {
        doThrow(new InvalidAmountInExpense("Invalid amount in expense"))
                .when(validator).validateExpenseDetails(any());

        mockMvc.perform(post("/update-expense/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateExpense_serverError() throws Exception {
        when(expenseService.updateExpense(anyLong(), any()))
                .thenThrow(new RuntimeException("db error"));

        mockMvc.perform(post("/update-expense/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("server error"));
    }

    @Test
    public void deleteExpense_success() throws Exception {
        doNothing().when(expenseService).deleteExpense(anyLong());

        mockMvc.perform(delete("/delete-expense/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Expense deleted successfully"));
    }

    @Test
    public void deleteExpense_error() throws Exception {
        doThrow(new RuntimeException("db error")).when(expenseService).deleteExpense(anyLong());

        mockMvc.perform(delete("/delete-expense/{id}", 1L))
                .andExpect(status().isInternalServerError());
    }
}
