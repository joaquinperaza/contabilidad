package app.peraza.domain.interactor;

import app.peraza.core.executor.PostExecutionThread;
import app.peraza.core.executor.ThreadExecutor;
import app.peraza.domain.model.Expense;
import app.peraza.domain.repository.ExpenseRepository;

import java.util.Date;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class GetExpenseUseCase extends UseCase<GetExpenseUseCase.Input, Expense> {
    private ExpenseRepository expenseRepository;

    @Inject
    public GetExpenseUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ExpenseRepository expenseRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.expenseRepository = expenseRepository;
    }

    @Override
    protected Observable<Expense> buildUseCaseObservable(GetExpenseUseCase.Input input) {
        return expenseRepository.findExpense(input.id, input.date);
    }

    public static class Input {
        private final String id;
        private final Date date;

        public Input(String id, Date date) {
            this.id = id;
            this.date = date;
        }
    }

}
