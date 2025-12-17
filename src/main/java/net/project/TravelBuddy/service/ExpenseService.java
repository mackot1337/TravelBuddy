package net.project.TravelBuddy.service;

import net.project.TravelBuddy.payload.ExpenseDto;

import java.util.List;

public interface ExpenseService {
    ExpenseDto addExpense(Long tripId, ExpenseDto expenseDto);

    List<ExpenseDto> getExpensesByTripId(Long tripId);

    ExpenseDto getExpenseById(Long tripId, Long expenseId);

    ExpenseDto updateExpense(Long tripId, Long expenseId, ExpenseDto expenseDto);

    String deleteExpense(Long tripId, Long expenseId);
}
