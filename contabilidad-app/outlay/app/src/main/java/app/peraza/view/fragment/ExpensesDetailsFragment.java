package app.peraza.view.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import app.peraza.core.utils.DateUtils;
import app.peraza.core.utils.NumberUtils;
import app.peraza.domain.model.Category;
import app.peraza.domain.model.Expense;
import app.peraza.mvp.presenter.ExpenseDetailsPresenter;
import app.peraza.mvp.view.ExpenseDetailsView;
import app.peraza.utils.IconUtils;
import app.peraza.view.autocomplete.CategoryAutoCompleteAdapter;
import app.peraza.view.dialog.DatePickerFragment;
import app.peraza.view.fragment.base.BaseMvpFragment;
import app.peraza.view.helper.TextWatcherAdapter;

import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/20/16.
 */
public class ExpensesDetailsFragment extends BaseMvpFragment<ExpenseDetailsView, ExpenseDetailsPresenter> implements ExpenseDetailsView {

    public static final String ARG_EXPENSE_ID = "_argExpenseId";
    public static final String ARG_DATE = "_argDate";

    @Bind(app.peraza.R.id.toolbar)
    Toolbar toolbar;

    @Bind(app.peraza.R.id.categoryTitle)
    MaterialAutoCompleteTextView categoryTitle;

    @Bind(app.peraza.R.id.categoryIcon)
    ImageView categoryIcon;

    @Bind(app.peraza.R.id.amount)
    EditText amount;

    @Bind(app.peraza.R.id.note)
    EditText note;

    @Bind(app.peraza.R.id.cot)
    EditText cot;

    @Bind(app.peraza.R.id.date)
    EditText dateEdit;

    @Bind(app.peraza.R.id.amountInputLayout)
    TextInputLayout amountInputLayout;

    @Inject
    ExpenseDetailsPresenter presenter;
    private CategoryAutoCompleteAdapter autoCompleteAdapter;
    private Expense expense;
    private Category selectedCategory;
    private Date defaultDate;

    @Override
    public ExpenseDetailsPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApp().getUserComponent().inject(this);
        defaultDate = new Date(getArguments().getLong(ARG_DATE, new Date().getTime()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(app.peraza.R.layout.fragment_expense_details, null, false);
        ButterKnife.bind(this, view);
        setToolbar(toolbar);
        setDisplayHomeAsUpEnabled(true);

        Drawable noCategoryIcon = IconUtils.getIconMaterialIcon(
                getContext(),
                MaterialDesignIconic.Icon.gmi_label,
                ContextCompat.getColor(getActivity(), app.peraza.R.color.icon_inactive),
                app.peraza.R.dimen.report_category_icon,
                4
        );
        categoryIcon.setImageDrawable(noCategoryIcon);


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(app.peraza.R.menu.menu_category_details, menu);
        MenuItem saveItem = menu.findItem(app.peraza.R.id.action_save);
        saveItem.setIcon(getResourceHelper().getMaterialToolbarIcon(app.peraza.R.string.ic_material_done));
        if (expense != null && expense.getId() == null) {
            MenuItem deleteItem = menu.findItem(app.peraza.R.id.action_delete);
            deleteItem.setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case app.peraza.R.id.action_save:
                if (validateInput()) {
                    Expense expense = getExpense();
                    if (TextUtils.isEmpty(expense.getId())) {
                        analytics().trackExpenseCreated(expense);
                    } else {
                        analytics().trackExpenseUpdated(expense);
                    }
                    presenter.updateExpense(expense);
                    getActivity().onBackPressed();
                }
                break;
            case app.peraza.R.id.action_delete:
                Expense expense = getExpense();
                analytics().trackExpenseDeleted(expense);
                presenter.deleteExpense(expense);
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadCategoryIcon(Category category) {
        int iconCodeRes = getResourceHelper().getIntegerResource(category.getIcon());
        Drawable categoryIconDrawable = IconUtils.getCategoryIcon(getContext(),
                iconCodeRes,
                category.getColor(),
                app.peraza.R.dimen.report_category_icon
        );
        categoryIcon.setImageDrawable(categoryIconDrawable);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        autoCompleteAdapter = new CategoryAutoCompleteAdapter();
        categoryTitle.setAdapter(autoCompleteAdapter);
        categoryTitle.setOnItemClickListener((parent, view1, position, id) -> {

            Category category = autoCompleteAdapter.getItem(position);
            selectedCategory = category;
            loadCategoryIcon(category);

        });
        presenter.getCategories();
        dateEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePickerDialog();
            }
        });
        dateEdit.setOnClickListener(v -> showDatePickerDialog());

        if (getArguments().containsKey(ARG_EXPENSE_ID)) {
            String expenseId = getArguments().getString(ARG_EXPENSE_ID);
            getActivity().setTitle(getString(app.peraza.R.string.caption_edit_expense));
            presenter.findExpense(expenseId, defaultDate);
        } else {
            getActivity().setTitle(getString(app.peraza.R.string.caption_new_expense));
            showExpense(new Expense());
        }
        amount.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                amountInputLayout.setErrorEnabled(false);
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener((parent, year, monthOfYear, dayOfMonth) -> {
            Calendar c = Calendar.getInstance();
            c.set(year, monthOfYear, dayOfMonth);
            Date selected = c.getTime();
            setDateStr(selected);
            expense.setReportedWhen(selected);
        });
        datePickerFragment.show(getChildFragmentManager(), "datePicker");
    }

    @Override
    public void showCategories(List<Category> categories) {
        autoCompleteAdapter.setItems(categories);
    }

    @Override
    public void showExpense(Expense e) {
        this.expense = e;
        if (e.getId() != null) {
            selectedCategory = e.getCategory();
            loadCategoryIcon(selectedCategory);
            if(e.getCotizado()!=null) {
                String mystring = NumberUtils.formatAmount(e.getAmount().floatValue()*Double.valueOf(e.getCotizado()));


                cot.setText("Monto origen: $"+mystring+" Cotizacion: "+e.getCotizado());}
            categoryTitle.setText(e.getCategory().getTitle());
            BigDecimal var2 =e.getAmount();
            amount.setText(NumberUtils.formatAmount(var2));
            note.setText(e.getNote());

            setDateStr(expense.getReportedWhen());
        } else {
            this.expense.setReportedWhen(defaultDate);
            setDateStr(expense.getReportedWhen());
        }
    }

    public Expense getExpense() {
        if (selectedCategory != null) {
            expense.setCategory(selectedCategory);
        }
        String amountStr = amount.getText().toString().replaceAll(",",".");
        expense.setAmount(new BigDecimal(amountStr));
        expense.setNote(note.getText().toString());
        return expense;
    }

    private void setDateStr(Date date) {
        dateEdit.setText(DateUtils.toLongString(date));
    }

    private boolean validateInput() {
        boolean result = true;
        if (selectedCategory == null) {
            categoryTitle.setError(getString(app.peraza.R.string.error_category_name_empty));
            categoryTitle.requestFocus();
            result = false;
        } else if (!selectedCategory.getTitle().equals(categoryTitle.getText().toString())) {
            categoryTitle.setError(getString(app.peraza.R.string.error_category_name_invalid));
            categoryTitle.requestFocus();
            result = false;
        }

        if (TextUtils.isEmpty(amount.getText())) {
            //TODO validate number
            amountInputLayout.setError(getString(app.peraza.R.string.error_amount_invalid));
            amountInputLayout.requestFocus();
            result = false;
        } else {
            String amountStr = amount.getText().toString().replaceAll(",",".");
            Log.e("bd",amountStr);
            BigDecimal number = new BigDecimal(amountStr);
            if (number.compareTo(BigDecimal.ZERO) <= 0) {
                amountInputLayout.setError(getString(app.peraza.R.string.error_amount_invalid));
                amountInputLayout.requestFocus();
                result = false;
            }
        }

        return result;
    }
}
