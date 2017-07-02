package app.peraza.domain.interactor;


        import app.peraza.core.executor.PostExecutionThread;
        import app.peraza.core.executor.ThreadExecutor;
        import app.peraza.core.utils.DateUtils;
        import app.peraza.domain.model.Expense;
        import app.peraza.domain.repository.ExpenseRepository;

        import org.joda.time.DateTime;
        import org.joda.time.LocalTime;

        import javax.inject.Inject;

        import rx.Observable;

/**
 * Created by joaquin on 21/6/17.
 */

public class UpdateExpenseUseCase extends UseCase<Expense, Expense> {
    private ExpenseRepository expenseRepository;

    @Inject
    public UpdateExpenseUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ExpenseRepository expenseRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.expenseRepository = expenseRepository;
    }

    @Override
    protected Observable<Expense> buildUseCaseObservable(Expense expense) {
        if (DateUtils.isToday(expense.getReportedWhen())) {
            DateTime dateTime = new DateTime(expense.getReportedWhen().getTime());
            dateTime = dateTime.withTime(LocalTime.now());
            expense.setReportedWhen(dateTime.toDate());
        }
        return expenseRepository.updateExpense(expense);
    }
}
