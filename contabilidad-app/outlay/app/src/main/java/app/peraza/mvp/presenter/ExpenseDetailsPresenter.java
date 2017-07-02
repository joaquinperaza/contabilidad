package app.peraza.mvp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import app.peraza.core.executor.DefaultSubscriber;
import app.peraza.domain.interactor.DeleteExpenseUseCase;
import app.peraza.domain.interactor.GetCategoriesUseCase;
import app.peraza.domain.interactor.GetExpenseUseCase;
import app.peraza.domain.interactor.SaveExpenseUseCase;
import app.peraza.domain.interactor.UpdateExpenseUseCase;
import app.peraza.domain.model.Category;
import app.peraza.domain.model.Expense;
import app.peraza.mvp.view.ExpenseDetailsView;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class ExpenseDetailsPresenter extends MvpBasePresenter<ExpenseDetailsView> {
    private GetCategoriesUseCase getCategoriesUseCase;
    private GetExpenseUseCase getExpenseUseCase;
    private SaveExpenseUseCase saveExpenseUseCase;
    private UpdateExpenseUseCase updateExpenseUseCase;
    private DeleteExpenseUseCase deleteExpenseUseCase;

    @Inject
    public ExpenseDetailsPresenter(
            GetCategoriesUseCase getCategoriesUseCase,
            GetExpenseUseCase getExpenseUseCase,
            SaveExpenseUseCase saveExpenseUseCase,
            DeleteExpenseUseCase deleteExpenseUseCase,
            UpdateExpenseUseCase updateExpenseUseCase
    ) {
        this.getCategoriesUseCase = getCategoriesUseCase;
        this.getExpenseUseCase = getExpenseUseCase;
        this.saveExpenseUseCase = saveExpenseUseCase;
        this.deleteExpenseUseCase = deleteExpenseUseCase;
        this.updateExpenseUseCase = updateExpenseUseCase;
    }

    public void findExpense(String expenseId, Date date) {
        getExpenseUseCase.execute(new GetExpenseUseCase.Input(expenseId, date), new DefaultSubscriber<Expense>() {
            @Override
            public void onNext(Expense expense) {
                getView().showExpense(expense);
            }
        });

    }

    public void getCategories() {
        getCategoriesUseCase.execute(new DefaultSubscriber<List<Category>>() {
            @Override
            public void onNext(List<Category> categories) {
                super.onNext(categories);
                getView().showCategories(categories);
            }
        });
    }

    public void updateExpense(Expense expense) {
        updateExpenseUseCase.execute(expense, new DefaultSubscriber());
    }

    public void deleteExpense(Expense expense) {
        deleteExpenseUseCase.execute(expense, new DefaultSubscriber());
    }
}