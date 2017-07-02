package app.peraza.domain.interactor;

import app.peraza.core.executor.PostExecutionThread;
import app.peraza.core.executor.ThreadExecutor;
import app.peraza.domain.model.Category;
import app.peraza.domain.repository.CategoryRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class DeleteCategoryUseCase extends UseCase<Category, Category> {
    private CategoryRepository categoryRepository;

    @Inject
    public DeleteCategoryUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            CategoryRepository categoryRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.categoryRepository = categoryRepository;
    }

    @Override
    protected Observable<Category> buildUseCaseObservable(Category category) {
        return categoryRepository.remove(category);
    }
}
