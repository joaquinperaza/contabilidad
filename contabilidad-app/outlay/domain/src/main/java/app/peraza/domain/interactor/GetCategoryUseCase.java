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

public class GetCategoryUseCase extends UseCase<String, Category> {
    private CategoryRepository categoryRepository;

    @Inject
    public GetCategoryUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            CategoryRepository categoryRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.categoryRepository = categoryRepository;
    }

    @Override
    protected Observable<Category> buildUseCaseObservable(String categoryId) {
        return categoryRepository.getById(categoryId);
    }
}
