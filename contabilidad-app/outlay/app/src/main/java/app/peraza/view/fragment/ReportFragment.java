package app.peraza.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import app.peraza.core.utils.DateUtils;
import app.peraza.domain.model.Report;
import app.peraza.mvp.presenter.ReportPresenter;
import app.peraza.mvp.view.StatisticView;
import app.peraza.view.Navigator;
import app.peraza.view.adapter.ReportAdapter;
import app.peraza.view.dialog.DatePickerFragment;
import app.peraza.view.fragment.base.BaseMvpFragment;
import app.peraza.view.helper.OnTabSelectedListenerAdapter;
import app.peraza.view.model.CategorizedExpenses;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by Bogdan Melnychuk on 1/20/16.
 */
public class ReportFragment extends BaseMvpFragment<StatisticView, ReportPresenter> implements StatisticView {
    public static final String ARG_DATE = "_argDate";

    public static final int PERIOD_DAY = 0;
    public static final int PERIOD_WEEK = 1;
    public static final int PERIOD_MONTH = 2;
    public static final String setUser = "customUser";

    @Bind(app.peraza.R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(app.peraza.R.id.tabs)
    TabLayout tabLayout;

    @Bind(app.peraza.R.id.toolbar)
    Toolbar toolbar;

    @Bind(app.peraza.R.id.noResults)
    View noResults;

    @Inject
    ReportPresenter presenter;

    private int selectedPeriod;
    private Date selectedDate;
    private ReportAdapter adapter;
    String user;

    @Override
    public ReportPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getApp().getUserComponent().inject(this);

        selectedDate = new Date(getArguments().getLong(ARG_DATE, new Date().getTime()));
        user= getArguments().getString(setUser);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(app.peraza.R.layout.fragment_report, null, false);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(app.peraza.R.menu.menu_report, menu);
        MenuItem dateItem = menu.findItem(app.peraza.R.id.action_date);
        dateItem.setIcon(getResourceHelper().getMaterialToolbarIcon(app.peraza.R.string.ic_material_today));

        MenuItem listItem = menu.findItem(app.peraza.R.id.action_list);
        listItem.setIcon(getResourceHelper().getMaterialToolbarIcon(app.peraza.R.string.ic_material_list));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case app.peraza.R.id.action_date:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setOnDateSetListener((view, year, monthOfYear, dayOfMonth) -> {
                    Calendar c = Calendar.getInstance();
                    c.set(year, monthOfYear, dayOfMonth);
                    Date selected = c.getTime();
                    analytics().trackExpensesViewDateChange(selectedDate, selected);
                    selectedDate = selected;
                    ReportFragment.this.setTitle(DateUtils.toShortString(selected));
                    updateTitle();

                    presenter.getExpenses(selectedDate, selectedPeriod,null,user);
                });
                datePickerFragment.show(getChildFragmentManager(), "datePicker");
                break;
            case app.peraza.R.id.action_list:
                analytics().trackViewExpensesList();

                goToExpensesList(selectedDate, selectedPeriod,null,user);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolbar(toolbar);
        setDisplayHomeAsUpEnabled(true);
        updateTitle();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        tabLayout.addTab(tabLayout.newTab().setText(app.peraza.R.string.label_day));
        tabLayout.addTab(tabLayout.newTab().setText(app.peraza.R.string.label_week));
        tabLayout.addTab(tabLayout.newTab().setText(app.peraza.R.string.label_month));
        tabLayout.getTabAt(selectedPeriod).select();
        tabLayout.addOnTabSelectedListener(new OnTabSelectedListenerAdapter() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedPeriod = tab.getPosition();
                switch (selectedPeriod) {
                    case ReportFragment.PERIOD_DAY:
                        analytics().trackViewDailyExpenses();
                        break;
                    case ReportFragment.PERIOD_WEEK:
                        analytics().trackViewWeeklyExpenses();
                        break;
                    case ReportFragment.PERIOD_MONTH:
                        analytics().trackViewMonthlyExpenses();
                        break;
                }
                updateTitle();

                presenter.getExpenses(selectedDate, selectedPeriod,null,user);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReportAdapter();

        recyclerView.setAdapter(adapter);
        presenter.getExpenses(selectedDate, selectedPeriod,null,user);
        String not;
        if (user==null) {
        not="nada";
        } else {
            not=user;
        }
        Log.e("reportfor",not);

        adapter.setOnItemClickListener((category, report) -> goToExpensesList(selectedDate, selectedPeriod, category.getId(),user));

        ////////////
        ////////////
    }


    @Override
    public void showReport(Report report) {
        if (report.isEmpty()) {

            noResults.setVisibility(View.VISIBLE);
            Log.e("norep","norep");
        } else {
            noResults.setVisibility(View.GONE);
            adapter.setItems(new CategorizedExpenses(report));
            Log.e("norep", new CategorizedExpenses(report).getCategories().toString());
        }
    }

    private void updateTitle() {
        switch (selectedPeriod) {
            case PERIOD_DAY:
                setTitle(DateUtils.toShortString(selectedDate));
                break;
            case PERIOD_WEEK:
                Date startDate = DateUtils.getWeekStart(selectedDate);
                Date endDate = DateUtils.getWeekEnd(selectedDate);
                setTitle(DateUtils.toShortString(startDate) + " - " + DateUtils.toShortString(endDate));
                break;
            case PERIOD_MONTH:
                startDate = DateUtils.getMonthStart(selectedDate);
                endDate = DateUtils.getMonthEnd(selectedDate);
                setTitle(DateUtils.toShortString(startDate) + " - " + DateUtils.toShortString(endDate));
                break;
        }
    }

    public void goToExpensesList(Date date, int selectedPeriod) {
        this.goToExpensesList(date, selectedPeriod, null,null);
    }

    public void goToExpensesList(Date date, int selectedPeriod, String category, String user) {
        date = DateUtils.fillCurrentTime(date);
        Date startDate = date;
        Date endDate = date;


        switch (selectedPeriod) {
            case ReportFragment.PERIOD_DAY:
                startDate = DateUtils.getDayStart(date);
                endDate = DateUtils.getDayEnd(date);
                break;
            case ReportFragment.PERIOD_WEEK:
                startDate = DateUtils.getWeekStart(date);
                endDate = DateUtils.getWeekEnd(date);
                break;
            case ReportFragment.PERIOD_MONTH:
                startDate = DateUtils.getMonthStart(date);
                endDate = DateUtils.getMonthEnd(date);
                break;
        }
        if (user==null){getArguments().getString("primaryU");}
        Navigator.goToExpensesList(getActivity(), startDate, endDate, category, user);
    }
}