package app.peraza.mvp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import app.peraza.core.executor.DefaultSubscriber;
import app.peraza.domain.interactor.GetCategoriesUseCase;
import app.peraza.domain.interactor.UpdateCategoriesUseCase;
import app.peraza.domain.model.Category;
import app.peraza.mvp.view.CategoriesView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class CategoriesPresenter extends MvpBasePresenter<CategoriesView> {
    private GetCategoriesUseCase getCategoriesUseCase;
    private UpdateCategoriesUseCase updateCategoriesUseCase;

    @Inject
    public CategoriesPresenter(
            GetCategoriesUseCase getCategoriesUseCase,
            UpdateCategoriesUseCase updateCategoriesUseCase
    ) {
        this.getCategoriesUseCase = getCategoriesUseCase;
        this.updateCategoriesUseCase = updateCategoriesUseCase;
    }

    public void getCategories() {
        getCategoriesUseCase.execute(new DefaultSubscriber<List<Category>>() {
            @Override
            public void onNext(List<Category> categories) {
                getView().showCategories(categories);
            }
        });
    }

    public void updateOrder(List<Category> categories) {
        updateCategoriesUseCase.execute(categories, new DefaultSubscriber());
    }
}
