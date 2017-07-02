package app.peraza.mvp.view;

import com.hannesdorfmann.mosby.mvp.MvpView;
import app.peraza.domain.model.Category;
import app.peraza.domain.model.Expense;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public interface EnterExpenseView extends MvpView {
    void showCategories(List<Category> categoryList);
    void showTimeline(List<Expense> expenses);
    void setAmount(BigDecimal amount);
    void alertExpenseSuccess(Expense expense);
}
