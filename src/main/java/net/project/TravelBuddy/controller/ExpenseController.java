package net.project.TravelBuddy.controller;

import net.project.TravelBuddy.payload.ExpenseDto;
import net.project.TravelBuddy.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips/{tripId}/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDto> addExpense(@PathVariable Long tripId, @RequestBody ExpenseDto expenseDto) {
        ExpenseDto expense = expenseService.addExpense(tripId, expenseDto);
        return new ResponseEntity<>(expense, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDto>> getExpenses(@PathVariable Long tripId) {
        List<ExpenseDto> expenses = expenseService.getExpensesByTripId(tripId);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseDto> getExpenseById(@PathVariable Long tripId, @PathVariable Long expenseId) {
        ExpenseDto expense = expenseService.getExpenseById(tripId, expenseId);
        return new ResponseEntity<>(expense, HttpStatus.OK);
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseDto> updateExpense(@PathVariable Long tripId, @PathVariable Long expenseId, @RequestBody ExpenseDto expenseDto) {
        ExpenseDto updatedExpense = expenseService.updateExpense(tripId, expenseId, expenseDto);
        return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long tripId, @PathVariable Long expenseId) {
        String status = expenseService.deleteExpense(tripId, expenseId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
