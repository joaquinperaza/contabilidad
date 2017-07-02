package app.peraza.domain.interactor;

import app.peraza.core.executor.PostExecutionThread;
import app.peraza.core.executor.ThreadExecutor;
import app.peraza.domain.model.Category;
import app.peraza.domain.repository.CategoryRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class UpdateCategoriesUseCase extends UseCase<List<Category>, List<Category>> {
    private CategoryRepository categoryRepository;

    @Inject
    public UpdateCategoriesUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            CategoryRepository categoryRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.categoryRepository = categoryRepository;
    }

    @Override
    protected Observable<List<Category>> buildUseCaseObservable(List<Category> categories) {
        return categoryRepository.updateOrder(categories);
    }
}