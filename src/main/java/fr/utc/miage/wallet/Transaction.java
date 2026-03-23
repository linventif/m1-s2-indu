package fr.utc.miage.wallet;

import java.time.LocalDate;

class Transaction {

    enum TypeTransaction {
        BUY,
        SELL;
    }
    
    private TypeTransaction type;
    private Action action;
    private Double amount;
    private Integer quantity;
    private LocalDate date;

    public Transaction(final TypeTransaction type, final Action action, final Double amount, final Integer quantity) {
        this.type = type;
        this.action = action;
        this.amount = amount;
        this.quantity = quantity;
        this.date = LocalDate.now();
    }

    public TypeTransaction getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public Action getAction() {
        return action;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getCurrentValue() {
        //TO DO - Quantity * action.currentValue().
        return 0.0;
    }

    public Double getBenefice() {
        return this.quantity * this.amount - this.getCurrentValue() ;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((action == null) ? 0 : action.hashCode());
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transaction other = (Transaction) obj;
        if (type != other.type)
            return false;
        if (action == null) {
            if (other.action != null)
                return false;
        } else if (!action.equals(other.action))
            return false;
        if (amount == null) {
            if (other.amount != null)
                return false;
        } else if (!amount.equals(other.amount))
            return false;
        if (quantity == null) {
            if (other.quantity != null)
                return false;
        } else if (!quantity.equals(other.quantity))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Transaction [getType()=" + getType() + ", getAmount()=" + getAmount() + ", getAction()=" + getAction()
                + ", getQuantity()=" + getQuantity() + ", getDate()=" + getDate() + "]";
    }

}
