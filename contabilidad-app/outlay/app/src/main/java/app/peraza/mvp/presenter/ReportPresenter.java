package app.peraza.mvp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import app.peraza.core.executor.DefaultSubscriber;
import app.peraza.core.utils.DateUtils;
import app.peraza.domain.interactor.GetExpensesUseCase;
import app.peraza.domain.model.Report;
import app.peraza.mvp.view.StatisticView;
import app.peraza.view.fragment.ReportFragment;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class ReportPresenter extends MvpBasePresenter<StatisticView> {
    private GetExpensesUseCase loadReportUseCase;

    @Inject
    public ReportPresenter(
            GetExpensesUseCase loadReportUseCase
    ) {
        this.loadReportUseCase = loadReportUseCase;
    }

    public void getExpenses(Date date, int period,String category, String user) {
        Date startDate = date;
        Date endDate = date;

        switch (period) {
            case ReportFragment.PERIOD_DAY:
                startDate = DateUtils.getDayStart(date);
                endDate = DateUtils.getDayEnd(date);
                break;
            case ReportFragment.PERIOD_WEEK:
                startDate = DateUtils.getWeekStart(date);
                endDate = DateUtils.getWeekEnd(date);
                break;
            case ReportFragment.PERIOD_MONTH:
                startDate = DateUtils.getMonthStart(date);
                endDate = DateUtils.getMonthEnd(date);
                break;
        }


        loadReportUseCase.execute(new GetExpensesUseCase.Input(startDate, endDate, category, user), new DefaultSubscriber<Report>() {
            @Override
            public void onNext(Report report) {
                super.onNext(report);
                getView().showReport(report);

            }
        });
    }
}