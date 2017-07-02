package app.peraza.firebase.dto;

/**
 * Created by bmelnychuk on 10/27/16.
 */

public class ExpenseDto {
    private String id;
    private String note;
    private String amount;
    private Long reportedWhen;
    private String categoryId;
    private String moneda;
    private String cotizado;
    private String amountO;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMoneda() {
        return moneda;
    }


    public String getCotizado() {
        return cotizado;
    }
    public void setCotizado(String mon) {
        this.cotizado = mon;
    }


    public void setMoneda(String mon) {
        this.moneda = mon;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountO() {
        return amountO;
    }

    public void setAmountO(String amountO) {
        this.amountO = amountO;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Long getReportedWhen() {
        return reportedWhen;
    }

    public ExpenseDto setReportedWhen(Long reportedWhen) {
        this.reportedWhen = reportedWhen;
        return this;
    }
}
