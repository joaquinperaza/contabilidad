package app.peraza.domain.interactor;

import app.peraza.core.executor.PostExecutionThread;
import app.peraza.core.executor.ThreadExecutor;
import app.peraza.domain.model.User;
import app.peraza.domain.repository.AuthService;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class ResetPasswordUseCase extends UseCase<User, Void> {
    private AuthService authService;

    @Inject
    public ResetPasswordUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            AuthService authService
    ) {
        super(threadExecutor, postExecutionThread);
        this.authService = authService;
    }

    @Override
    protected Observable<Void> buildUseCaseObservable(User user) {
        return authService.resetPassword(user);
    }
}
