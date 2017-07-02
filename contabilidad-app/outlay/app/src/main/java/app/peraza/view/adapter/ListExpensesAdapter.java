package app.peraza.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.peraza.core.utils.DateUtils;
import app.peraza.core.utils.NumberUtils;
import app.peraza.domain.model.Category;
import app.peraza.domain.model.Expense;
import app.peraza.utils.IconUtils;

import app.peraza.utils.ResourceHelper;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bmelnychuk on 2/10/17.
 */

public class ListExpensesAdapter extends ExpenseAdapter<ListExpensesAdapter.ExpenseListItemViewHolder> {
    @Override
    public ExpenseListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(app.peraza.R.layout.recycler_list_expense, parent, false);
        final ExpenseListItemViewHolder viewHolder = new ExpenseListItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExpenseListItemViewHolder holder, int position) {
        Expense expense = items.get(position);
        Context context = holder.container.getContext();
        holder.container.setOnClickListener(v -> {
            if (onExpenseClickListener != null) {
                onExpenseClickListener.onExpenseClicked(expense);
            }
        });

        holder.amount.setText(NumberUtils.formatAmount(expense.getAmount()));
        holder.date.setText(DateUtils.toShortString(expense.getReportedWhen()));

        Category currentOne = expense.getCategory();
        int iconCodeRes = ResourceHelper.getIntegerResource(context, currentOne.getIcon());
        Drawable categoryIcon = IconUtils.getCategoryIcon(context, iconCodeRes, currentOne.getColor(), app.peraza.R.dimen.report_category_icon);
        holder.icon.setImageDrawable(categoryIcon);
        holder.note.setVisibility(TextUtils.isEmpty(expense.getNote()) ? View.GONE : View.VISIBLE);
        holder.note.setText(expense.getNote());
        holder.title.setText(currentOne.getTitle());
    }

    public class ExpenseListItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(app.peraza.R.id.icon)
        ImageView icon;

        @Bind(app.peraza.R.id.amount)
        TextView amount;

        @Bind(app.peraza.R.id.date)
        TextView date;

        @Bind(app.peraza.R.id.title)
        TextView title;

        @Bind(app.peraza.R.id.note)
        TextView note;

        @Bind(app.peraza.R.id.container)
        View container;

        public ExpenseListItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
