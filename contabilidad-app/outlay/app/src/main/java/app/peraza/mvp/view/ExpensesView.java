package app.peraza.mvp.view;

import com.hannesdorfmann.mosby.mvp.MvpView;
import app.peraza.domain.model.Report;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public interface ExpensesView extends MvpView {
    void showReport(Report report);
}
