package app.peraza.domain.interactor;

import app.peraza.core.executor.PostExecutionThread;
import app.peraza.core.executor.ThreadExecutor;
import app.peraza.domain.model.Credentials;
import app.peraza.domain.model.User;
import app.peraza.domain.repository.AuthService;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class UserSignUpUseCase extends UseCase<Credentials, User> {
    private AuthService authService;

    @Inject
    public UserSignUpUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            AuthService authService
    ) {
        super(threadExecutor, postExecutionThread);
        this.authService = authService;
    }

    @Override
    protected Observable<User> buildUseCaseObservable(Credentials credentials) {
        return authService.signUp(credentials);
    }
}
