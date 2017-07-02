package app.peraza.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;

import app.peraza.core.utils.DateUtils;
import app.peraza.core.utils.NumberUtils;
import app.peraza.domain.model.Expense;
import app.peraza.utils.IconUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bmelnychuk on 2/10/17.
 */

public class GridExpensesAdapter extends ExpenseAdapter<GridExpensesAdapter.ExpenseGridItemViewHolder> {
    @Override
    public ExpenseGridItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(app.peraza.R.layout.recycler_grid_expense, parent, false);
        final ExpenseGridItemViewHolder viewHolder = new ExpenseGridItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExpenseGridItemViewHolder holder, int position) {
        Expense expense = items.get(position);
        holder.note.setText(expense.getNote());
        holder.root.setOnClickListener(v -> {
            if (onExpenseClickListener != null) {
                onExpenseClickListener.onExpenseClicked(expense);
            }
        });
        holder.categoryAmount.setText(NumberUtils.formatAmount(expense.getAmount()));
        holder.categoryDate.setText(DateUtils.toShortString(expense.getReportedWhen()));
        holder.categoryTitle.setText(expense.getCategory().getTitle());
        IconUtils.loadCategoryIcon(expense.getCategory(), holder.categoryIcon);
    }

    public class ExpenseGridItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(app.peraza.R.id.categoryNote)
        TextView note;

        @Bind(app.peraza.R.id.expenseContainer)
        View root;

        @Bind(app.peraza.R.id.categoryIcon)
        PrintView categoryIcon;

        @Bind(app.peraza.R.id.categoryTitle)
        TextView categoryTitle;

        @Bind(app.peraza.R.id.categoryDate)
        TextView categoryDate;

        @Bind(app.peraza.R.id.categoryAmount)
        TextView categoryAmount;

        public ExpenseGridItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
