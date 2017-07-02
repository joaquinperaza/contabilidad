package app.peraza.firebase.dto.adapter;

import app.peraza.firebase.dto.ExpenseDto;
import app.peraza.domain.model.Category;
import app.peraza.domain.model.Expense;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class ExpenseAdapter {

    public Expense toExpense(ExpenseDto expenseDto) {
        Expense expense = new Expense();
        expense.setNote(expenseDto.getNote());
        expense.setAmount(new BigDecimal(expenseDto.getAmount()));
        expense.setId(expenseDto.getId());
        expense.setReportedWhen(new Date(expenseDto.getReportedWhen()));

        Category category = new Category();
        category.setId(expenseDto.getCategoryId());
        expense.setCategory(category);
        expense.setMoneda(expenseDto.getMoneda());
        expense.setCotizado(expenseDto.getCotizado());
        expense.setAmountO(new BigDecimal(expenseDto.getAmount()));

        return expense;
    }

    public ExpenseDto fromExpense(Expense expense) {
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setNote(expense.getNote());
        expenseDto.setAmount(expense.getAmount().toString());
        expenseDto.setId(expense.getId());
        expenseDto.setReportedWhen(expense.getReportedWhen().getTime());
        expenseDto.setCategoryId(expense.getCategory().getId());
        expenseDto.setMoneda(expense.getMoneda());
        expenseDto.setCotizado(expense.getCotizado());
        expenseDto.setAmountO(expense.getAmountO().toString());

        return expenseDto;
    }
}