package app.peraza.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;

import app.peraza.core.utils.DateUtils;
import app.peraza.domain.model.Category;
import app.peraza.domain.model.Expense;
import app.peraza.domain.model.Report;
import app.peraza.mvp.presenter.ExpensesListPresenter;
import app.peraza.mvp.view.ExpensesView;
import app.peraza.utils.IconUtils;
import app.peraza.view.Navigator;
import app.peraza.view.adapter.ExpenseAdapter;
import app.peraza.view.adapter.ListExpensesAdapter;
import app.peraza.view.fragment.base.BaseMvpFragment;
import app.peraza.impl.AppPreferences;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by Bogdan Melnychuk on 1/20/16.
 */
public class ExpensesListFragment extends BaseMvpFragment<ExpensesView, ExpensesListPresenter> implements ExpensesView {
    public static final String ARG_CATEGORY_ID = "_argCategoryId";
    public static final String ARG_DATE_FROM = "_argDateFrom";
    public static final String ARG_DATE_TO = "_argDateTo";
    public static final String ARG_USR = "_argusr";

    private static final int MODE_LIST = 0;
    private static final int MODE_GRID = 1;

    @Bind(app.peraza.R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(app.peraza.R.id.toolbar)
    Toolbar toolbar;

    @Bind(app.peraza.R.id.fab)
    FloatingActionButton fab;

    @Bind(app.peraza.R.id.noResults)
    View noResults;

    @Bind(app.peraza.R.id.filterCategoryIcon)
    PrintView filterCategoryIcon;

    @Bind(app.peraza.R.id.filterCategoryName)
    TextView filterCategoryName;

    @Bind(app.peraza.R.id.filterDateLabel)
    TextView filterDateLabel;

    @Inject
    ExpensesListPresenter presenter;

    private ExpenseAdapter adapter;
    private Date dateFrom;
    private Date dateTo;
    private String categoryId;
    private String user;

    private int mode = MODE_LIST;

    @Override
    public ExpensesListPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApp().getUserComponent().inject(this);

        user= getArguments().getString(ARG_USR);
        if (user==null){
            user = getArguments().getString("primaryU");

        }
//        Log.e("ondetoy",user);

        long dateFromMillis = getArguments().getLong(ARG_DATE_FROM);
        long dateToMillis = getArguments().getLong(ARG_DATE_TO);

        dateFrom = new Date(dateFromMillis);
        dateTo = new Date(dateToMillis);
        if (getArguments().containsKey(ARG_CATEGORY_ID)) {
            categoryId = getArguments().getString(ARG_CATEGORY_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(app.peraza.R.layout.fragment_expenses_list, null, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolbar(toolbar);
        setDisplayHomeAsUpEnabled(true);
        setTitle(getString(app.peraza.R.string.caption_expenses));

        filterDateLabel.setText(getDateLabel());
        fab.setImageDrawable(getResourceHelper().getFabIcon(app.peraza.R.string.ic_material_add));
        fab.setOnClickListener(v -> Navigator.goToExpenseDetails(getActivity(), null));
        recyclerView.setHasFixedSize(true);

//            adapter = new GridExpensesAdapter();
//            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            
        adapter = new ListExpensesAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnExpenseClickListener(expense -> Navigator.goToExpenseDetails(getActivity(), expense));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.findExpenses(dateFrom, dateTo, categoryId,user);
    }

    private void displayExpenses(List<Expense> expenses) {
        if (expenses.isEmpty()) {
            noResults.setVisibility(View.VISIBLE);
        } else {
            noResults.setVisibility(View.GONE);
            adapter.setItems(expenses);
        }
    }

    private void displayCategory(Category category) {
        if (category != null) {
            IconUtils.loadCategoryIcon(category, filterCategoryIcon);
            filterCategoryName.setText(category.getTitle());
        } else {
            filterCategoryName.setVisibility(View.GONE);
            filterCategoryIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public void showReport(Report report) {
        displayExpenses(report.getExpenses());
        Map<Category, Report> grouped = report.groupByCategory();
        if (grouped.size() == 1) {
            displayCategory(report.getExpenses().get(0).getCategory());
        }
    }

    private String getDateLabel() {
        String dateFromStr = DateUtils.toShortString(dateFrom);
        String dateToStr = DateUtils.toShortString(dateTo);
        String result = dateFromStr;
        if (!dateFromStr.equals(dateToStr)) {
            result += " - " + dateToStr;
        }
        return result;
    }
}
