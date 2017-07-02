package app.peraza.mvp.view;

import com.hannesdorfmann.mosby.mvp.MvpView;
import app.peraza.domain.model.Category;

import java.util.List;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public interface CategoriesView extends MvpView {
    void showCategories(List<Category> categoryList);

}
