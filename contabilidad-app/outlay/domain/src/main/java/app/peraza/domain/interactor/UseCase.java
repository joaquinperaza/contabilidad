package app.peraza.domain.interactor;

import app.peraza.core.executor.PostExecutionThread;
import app.peraza.core.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by bmelnychuk on 5/10/16.
 */
public abstract class UseCase<INPUT, OUTPUT> {
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;

    private Subscription subscription = Subscriptions.unsubscribed();

    protected UseCase(ThreadExecutor threadExecutor,
                      PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    protected abstract Observable<OUTPUT> buildUseCaseObservable(INPUT input);

    public void execute(INPUT input, Subscriber useCaseSubscriber) {
        this.unsubscribe();

        this.subscription = this.buildUseCaseObservable(input)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(useCaseSubscriber);
    }

    public void execute(Subscriber useCaseSubscriber) {
        execute(null, useCaseSubscriber);
    }

    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public boolean isUnsubscribed() {
        return this.subscription.isUnsubscribed();
    }
}
