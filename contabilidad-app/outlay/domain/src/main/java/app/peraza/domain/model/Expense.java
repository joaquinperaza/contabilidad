package app.peraza.domain.model;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class Expense {
    public static Comparator<Expense> DATE_COMPARATOR = (o1, o2) -> (int) (o1.getReportedWhen().getTime() - o2.getReportedWhen().getTime());

    private String id;
    private String note;
    private BigDecimal amount;
    private Date reportedWhen;
    private Category category;
    private String moneda;
    private String cotizado;
    private BigDecimal amountO;

    public String getId() {
        return id;
    }

    public Expense setId(String id) {
        this.id = id;
        return this;
    }

    public String getNote() {
        return note;
    }

    public Expense setNote(String note) {
        this.note = note;
        return this;
    }

    public String getMoneda() {
        return moneda;
    }
    public String getCotizado() {
        return cotizado;
    }
    public Expense setCotizado(String mon) {
        this.cotizado = mon;
        return this;
    }

    public Expense setMoneda(String mon) {
        this.moneda = mon;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Expense setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }
    public BigDecimal getAmountO() {
        if (amountO!=null){
        return amountO;}
        else{
            return amount;
        }
    }

    public Expense setAmountO(BigDecimal amountO) {
        this.amountO = amount;
        return this;
    }




    public Date getReportedWhen() {
        return reportedWhen;
    }

    public Expense setReportedWhen(Date reportedWhen) {
        this.reportedWhen = reportedWhen;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Expense setCategory(Category category) {
        this.category = category;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expense expense = (Expense) o;

        return id != null ? id.equals(expense.id) : expense.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}