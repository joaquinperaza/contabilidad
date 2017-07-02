package app.peraza.mvp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import app.peraza.core.executor.DefaultSubscriber;
import app.peraza.domain.interactor.DeleteExpenseUseCase;
import app.peraza.domain.interactor.GetCategoriesUseCase;
import app.peraza.domain.interactor.GetExpensesUseCase;
import app.peraza.domain.interactor.SaveExpenseUseCase;
import app.peraza.domain.model.Category;
import app.peraza.domain.model.Expense;
import app.peraza.domain.model.Report;
import app.peraza.mvp.view.EnterExpenseView;

/**
 * Created by Bogdan Melnychuk on 1/25/16.
 */
public class EnterExpensePresenter extends MvpBasePresenter<EnterExpenseView> {
    private GetCategoriesUseCase getCategoriesUseCase;
    private SaveExpenseUseCase createExpenseUseCase;
    private DeleteExpenseUseCase deleteExpenseUseCase;
    private GetExpensesUseCase getExpensesUseCase;

    @Inject
    public EnterExpensePresenter(
            GetCategoriesUseCase getCategoriesUseCase,
            SaveExpenseUseCase createExpenseUseCase,
            DeleteExpenseUseCase deleteExpenseUseCase,
            GetExpensesUseCase getExpensesUseCase
    ) {
        this.getCategoriesUseCase = getCategoriesUseCase;
        this.createExpenseUseCase = createExpenseUseCase;
        this.deleteExpenseUseCase = deleteExpenseUseCase;
        this.getExpensesUseCase = getExpensesUseCase;
    }

    public void getCategories() {
        getCategoriesUseCase.execute(new DefaultSubscriber<List<Category>>() {
            @Override
            public void onNext(List<Category> categories) {
                getView().showCategories(categories);
            }
        });
    }


    public void createExpense(Expense expense) {
        createExpenseUseCase.execute(expense, new DefaultSubscriber<Expense>() {
            @Override
            public void onNext(Expense expense) {
                getView().alertExpenseSuccess(expense);

            }
        });
    }

    public void deleteExpense(Expense expense) {
        deleteExpenseUseCase.execute(expense, new DefaultSubscriber<Expense>() {
            @Override
            public void onCompleted() {
            }
        });
    }

    public void loadTimeline() {
        GetExpensesUseCase.Input input = new GetExpensesUseCase.Input(
                new LocalDate().minusMonths(6).toDate(),
                new Date(),
                null,
                null

        );

        getExpensesUseCase.execute(input, new DefaultSubscriber<Report>() {
            @Override
            public void onNext(Report report) {
                Collections.sort(report.getExpenses(), new Comparator<Expense>() {
                    @Override
                    public int compare(Expense e1, Expense e2) {
                        if (e1.getReportedWhen().getTime() > e2.getReportedWhen().getTime()) {
                            return -1;
                        } else if (e1.getReportedWhen().getTime() < e2.getReportedWhen().getTime()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
                getView().showTimeline(report.getExpenses());
            }
        });
    }
}