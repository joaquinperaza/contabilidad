package app.peraza.domain.interactor;

import app.peraza.core.executor.PostExecutionThread;
import app.peraza.core.executor.ThreadExecutor;
import app.peraza.domain.model.Report;
import app.peraza.domain.repository.CategoryRepository;
import app.peraza.domain.repository.ExpenseRepository;

import java.util.Date;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class GetExpensesUseCase extends UseCase<GetExpensesUseCase.Input, Report> {
    private ExpenseRepository expenseRepository;
    private CategoryRepository categoryRepository;

    @Inject
    public GetExpensesUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ExpenseRepository expenseRepository,
            CategoryRepository categoryRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    protected Observable<Report> buildUseCaseObservable(GetExpensesUseCase.Input input) {
//        if (input.categoryId != null) {
//            return Observable.zip(
//                    categoryRepository.getById(input.categoryId),
//                    expenseRepository.getExpenses(input.startDate, input.endDate, input.categoryId),
//                    (category, expenses) -> {
//                        Report report = new Report();
//                        report.setEndDate(input.endDate);
//                        report.setStartDate(input.startDate);
//                        report.setExpenses(expenses);
//                        report.setCategory(category);
//                        return report;
//                    }
//            );
//        } else {
            return expenseRepository.getExpenses(input.startDate, input.endDate, input.categoryId, input.user)
                    .map(expenses -> {
                        Report report = new Report();
                        report.setEndDate(input.endDate);
                        report.setStartDate(input.startDate);
                        report.setExpenses(expenses);


                        return report;
                    });
//        }
    }

    public static class Input {
        private final Date startDate;
        private final Date endDate;
        private final String categoryId;
        private final String user;

        public Input(Date startDate, Date endDate, String categoryId, String user) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.categoryId = categoryId;
            this.user=user;
        }
    }
}
