package app.peraza.mvp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import app.peraza.core.executor.DefaultSubscriber;
import app.peraza.domain.interactor.DeleteCategoryUseCase;
import app.peraza.domain.interactor.GetCategoryUseCase;
import app.peraza.domain.interactor.SaveCategoryUseCase;
import app.peraza.domain.model.Category;
import app.peraza.mvp.view.CategoryDetailsView;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class CategoryDetailsPresenter extends MvpBasePresenter<CategoryDetailsView> {
    private SaveCategoryUseCase updateCategoryUseCase;
    private DeleteCategoryUseCase deleteCategoryUseCase;
    private GetCategoryUseCase getCategoryUseCase;

    @Inject
    public CategoryDetailsPresenter(
            SaveCategoryUseCase updateCategoryUseCase,
            DeleteCategoryUseCase deleteCategoryUseCase,
            GetCategoryUseCase getCategoryUseCase
    ) {
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
        this.getCategoryUseCase = getCategoryUseCase;
    }

    public void getCategory(String id) {
        getCategoryUseCase.execute(id, new DefaultSubscriber<Category>() {
            @Override
            public void onNext(Category category) {
                getView().showCategory(category);
            }
        });
    }

    public void updateCategory(Category category) {
        updateCategoryUseCase.execute(category, new DefaultSubscriber<Category>() {
            @Override
            public void onCompleted() {
                getView().finish();
            }
        });

    }

    public void deleteCategory(Category category) {
        deleteCategoryUseCase.execute(category, new DefaultSubscriber<Category>() {
            @Override
            public void onCompleted() {
                getView().finish();
            }
        });
    }
}
