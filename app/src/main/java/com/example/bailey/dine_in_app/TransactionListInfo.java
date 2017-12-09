package com.example.bailey.dine_in_app;

/**
 * Created by Anil Sood on 12/8/2017.
 */

public class TransactionListInfo {
    private int transaction_id;
    private String payment_type;
    private String tip;
    private String total;

    public TransactionListInfo(int transaction_id, String payment_type, String tip, String total) {
        this.transaction_id = transaction_id;
        this.payment_type = payment_type;
        this.tip = tip;
        this.total = total;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public String getTip() {
        return tip;
    }

    public String getTotal() {
        return total;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
