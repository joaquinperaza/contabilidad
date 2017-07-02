package app.peraza.data.repository;

import app.peraza.data.source.ExpenseDataSource;
import app.peraza.domain.model.Expense;
import app.peraza.domain.repository.ExpenseRepository;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class ExpenseRepositoryImpl implements ExpenseRepository {
    private ExpenseDataSource firebaseSource;

    @Inject
    public ExpenseRepositoryImpl(
            ExpenseDataSource firebaseSource
    ) {
        this.firebaseSource = firebaseSource;
    }

    private ExpenseDataSource getDataSource() {
        return firebaseSource;
    }

    @Override
    public Observable<Expense> saveExpense(Expense expense) {
        return getDataSource().saveExpense(expense);
    }
    @Override
    public Observable<Expense> updateExpense(Expense expense) {
        return getDataSource().updateExpense(expense);
    }


    @Override
    public Observable<Expense> remove(Expense expense) {
        return getDataSource().remove(expense);
    }

    @Override
    public Observable<Expense> findExpense(String expenseId, Date date) {
        return getDataSource().findExpense(expenseId, date);
    }

    @Override
    public Observable<List<Expense>> getExpenses(
            Date startDate,
            Date endDate

    ) {
        return getExpenses(startDate, endDate);
    }
    @Override
    public Observable<List<Expense>> getExpenses(
            Date startDate,
            Date endDate,
            String categoryId
    ) {
        return getDataSource().getExpenses(startDate, endDate, categoryId,null);
    }
    @Override
    public Observable<List<Expense>> getExpenses(
            Date startDate,
            Date endDate,
            String categoryId,
            String user
    ) {
        return getDataSource().getExpenses(startDate, endDate, categoryId,user);
    }
}