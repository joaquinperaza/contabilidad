package app.outlay.view.model;

import android.util.Log;

import app.outlay.R;
import app.outlay.domain.model.Category;
import app.outlay.domain.model.Report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bmelnychuk on 2/8/17.
 */

public class CategorizedExpenses {
    private Map<Category, Report> grouped;
    private List<Category> categories;

    public CategorizedExpenses(Report report) {
        grouped = report.groupByCategory();
        categories = new ArrayList<>(grouped.keySet());
        List<Category> cato= new ArrayList<>();

        for(Category expenseCategory:categories){



        if (expenseCategory.getColor()==-769226) {
            cato.add(0, expenseCategory);

        } else {

            cato.add(expenseCategory);

        }}
        categories=cato;



    }

    public CategorizedExpenses() {
        grouped = new HashMap<>();
        categories = new ArrayList<>();
    }

    public int getCategoriesSize() {
        return grouped.size();
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Category getCategory(int index) {
        return categories.get(index);
    }

    public Report getReport(Category category) {
        return grouped.get(category);
    }

    public Report getReport(int index) {
        Category c = categories.get(index);
        return getReport(c);
    }
}