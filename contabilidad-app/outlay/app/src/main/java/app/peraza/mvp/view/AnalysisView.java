package app.peraza.mvp.view;

import com.hannesdorfmann.mosby.mvp.MvpView;
import app.peraza.domain.model.Category;
import app.peraza.domain.model.Report;

import java.util.List;

/**
 * Created by bmelnychuk on 2/10/17.
 */

public interface AnalysisView extends MvpView {
    void showAnalysis(Report report);

    void setCategories(List<Category> categories);
}
