package net.project.TravelBuddy.service.Impl;

import net.project.TravelBuddy.entity.Expense;
import net.project.TravelBuddy.entity.Trip;
import net.project.TravelBuddy.exception.ApiException;
import net.project.TravelBuddy.exception.ResourceNotFoundException;
import net.project.TravelBuddy.payload.ExpenseDto;
import net.project.TravelBuddy.repository.ExpenseRepository;
import net.project.TravelBuddy.repository.TripRepository;
import net.project.TravelBuddy.service.ExpenseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ExpenseDto addExpense(Long tripId, ExpenseDto expenseDto) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", tripId));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!trip.getUser().getUsername().equals(username)) {
            throw new ApiException("You are not authorized to add expenses to this trip");
        }
        Expense expense = new Expense();
        expense.setTrip(trip);
        expense.setAmount(expenseDto.getAmount());
        expense.setCurrency(expenseDto.getCurrency());
        expense.setDescription(expenseDto.getDescription());
        expense.setCategory(expenseDto.getCategory());
        expense.setExpenseDate(expenseDto.getExpenseDate());
        trip.getExpenses().add(expense);
        Expense savedExpense = expenseRepository.save(expense);
        return modelMapper.map(savedExpense, ExpenseDto.class);
    }

    @Override
    public List<ExpenseDto> getExpensesByTripId(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", tripId));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!trip.getUser().getUsername().equals(username)) {
            throw new ApiException("You are not authorized to view expenses of this trip");
        }
        List<Expense> expenses = trip.getExpenses();
        return expenses.stream()
                .map(expense -> modelMapper.map(expense, ExpenseDto.class))
                .toList();
    }

    @Override
    public ExpenseDto getExpenseById(Long tripId, Long expenseId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", tripId));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!trip.getUser().getUsername().equals(username)) {
            throw new ApiException("You are not authorized to view expenses of this trip");
        }
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", "id", expenseId));
        if (!expense.getTrip().getTripId().equals(tripId)) {
            throw new ApiException("Expense does not belong to the specified trip");
        }
        return modelMapper.map(expense, ExpenseDto.class);
    }

    @Override
    public ExpenseDto updateExpense(Long tripId, Long expenseId, ExpenseDto expenseDto) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", tripId));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!trip.getUser().getUsername().equals(username)) {
            throw new ApiException("You are not authorized to update expenses of this trip");
        }
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", "id", expenseId));
        if (!expense.getTrip().getTripId().equals(tripId)) {
            throw new ApiException("Expense does not belong to the specified trip");
        }
        expense.setAmount(expenseDto.getAmount());
        expense.setCurrency(expenseDto.getCurrency());
        expense.setDescription(expenseDto.getDescription());
        expense.setCategory(expenseDto.getCategory());
        expense.setExpenseDate(expenseDto.getExpenseDate());
        Expense updatedExpense = expenseRepository.save(expense);
        return modelMapper.map(updatedExpense, ExpenseDto.class);
    }

    @Override
    public String deleteExpense(Long tripId, Long expenseId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", tripId));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!trip.getUser().getUsername().equals(username)) {
            throw new ApiException("You are not authorized to delete expenses of this trip");
        }
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", "id", expenseId));
        if (!expense.getTrip().getTripId().equals(tripId)) {
            throw new ApiException("Expense does not belong to the specified trip");
        }
        expenseRepository.delete(expense);
        return "Expense deleted successfully";
    }
}
