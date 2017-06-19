package app.outlay.view.model;

import android.util.Log;

import app.outlay.R;
import app.outlay.domain.model.Category;
import app.outlay.domain.model.Report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

        List<Category> aa= new ArrayList<>();
        List<Category> bb= new ArrayList<>();

        for(Category expenseCategory:categories){


        if (expenseCategory.getColor()==-769226) {

            if(aa.size()>0){
                BigDecimal now=getReport(expenseCategory).getTotalAmount();
                BigDecimal max=getReport(aa.get(0)).getTotalAmount();
                BigDecimal min=getReport(aa.get(aa.size()-1)).getTotalAmount();
                if(now.doubleValue()>max.doubleValue()){
                    aa.add(0,expenseCategory);
                }else if(now.doubleValue()<min.doubleValue()){
                    aa.add(expenseCategory);
                } else {
                    int i = 0;
                    while (max.doubleValue() > now.doubleValue()) {
                        i++;
                        max = getReport(aa.get(i)).getTotalAmount();
                    }
                    aa.add(i, expenseCategory);


                }
            } else {aa.add(expenseCategory);}

        } else {
            if(bb.size()>0){
                BigDecimal now=getReport(expenseCategory).getTotalAmount();
                BigDecimal max=getReport(bb.get(0)).getTotalAmount();
                BigDecimal min=getReport(bb.get(bb.size()-1)).getTotalAmount();
                if(now.doubleValue()>max.doubleValue()){
                    bb.add(0,expenseCategory);
                }else if(now.doubleValue()<min.doubleValue()){
                    bb.add(expenseCategory);
                } else {
                    int i = 0;
                    while (max.doubleValue() > now.doubleValue()) {
                        i++;
                        max = getReport(bb.get(i)).getTotalAmount();
                    }
                    bb.add(i, expenseCategory);


                }
            } else {bb.add(expenseCategory);}


        }}



        aa.addAll(bb);
        categories=aa;

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