package app.peraza.mvp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import app.peraza.core.executor.DefaultSubscriber;
import app.peraza.domain.interactor.GetCategoriesUseCase;
import app.peraza.domain.interactor.GetExpensesUseCase;
import app.peraza.domain.model.Category;
import app.peraza.domain.model.Report;
import app.peraza.mvp.view.AnalysisView;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by bmelnychuk on 2/10/17.
 */

public class AnalysisPresenter extends MvpBasePresenter<AnalysisView> {
    private GetCategoriesUseCase getCategoriesUseCase;
    private GetExpensesUseCase getExpensesUseCase;

    @Inject
    public AnalysisPresenter(
            GetCategoriesUseCase getCategoriesUseCase,
            GetExpensesUseCase getExpensesUseCase
    ) {
        this.getCategoriesUseCase = getCategoriesUseCase;
        this.getExpensesUseCase = getExpensesUseCase;
    }

    public void getCategories() {
        getCategoriesUseCase.execute(new DefaultSubscriber<List<Category>>() {
            @Override
            public void onNext(List<Category> categories) {
                super.onNext(categories);
                getView().setCategories(categories);
            }
        });
    }

    public void getExpenses(Date startDate, Date endDate, Category category) {
        getExpensesUseCase.execute(
                new GetExpensesUseCase.Input(startDate, endDate, category.getId(),null),
                new DefaultSubscriber<Report>() {
                    @Override
                    public void onNext(Report report) {
                        super.onNext(report);
                        if (isViewAttached()) {
                            getView().showAnalysis(report);
                        }
                    }
                }
        );
    }
}