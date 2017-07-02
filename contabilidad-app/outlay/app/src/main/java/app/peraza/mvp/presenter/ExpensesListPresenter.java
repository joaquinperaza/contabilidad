package app.peraza.mvp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import app.peraza.core.executor.DefaultSubscriber;
import app.peraza.domain.interactor.GetExpensesUseCase;
import app.peraza.domain.model.Report;
import app.peraza.mvp.view.ExpensesView;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class ExpensesListPresenter extends MvpBasePresenter<ExpensesView> {
    private GetExpensesUseCase loadReportUseCase;

    @Inject
    public ExpensesListPresenter(
            GetExpensesUseCase loadReportUseCase
    ) {
        this.loadReportUseCase = loadReportUseCase;
    }

    public void findExpenses(Date dateFrom, Date dateTo, String categoryId,String user) {
        GetExpensesUseCase.Input input = new GetExpensesUseCase.Input(dateFrom, dateTo, categoryId,user);
        loadReportUseCase.execute(input, new DefaultSubscriber<Report>() {
            @Override
            public void onNext(Report report) {
                getView().showReport(report);
            }
        });
    }
}
