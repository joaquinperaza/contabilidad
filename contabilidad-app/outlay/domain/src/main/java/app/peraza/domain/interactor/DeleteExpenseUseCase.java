package app.peraza.domain.interactor;

import app.peraza.core.executor.PostExecutionThread;
import app.peraza.core.executor.ThreadExecutor;
import app.peraza.domain.model.Expense;
import app.peraza.domain.repository.ExpenseRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class DeleteExpenseUseCase extends UseCase<Expense, Expense> {
    private ExpenseRepository expenseRepository;

    @Inject
    public DeleteExpenseUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ExpenseRepository expenseRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.expenseRepository = expenseRepository;
    }

    @Override
    protected Observable<Expense> buildUseCaseObservable(Expense expense) {
        return expenseRepository.remove(expense);
    }
}
