package com.cowork.bean;
import java.util.Date;

public class Bill {

    private int billID;
    private String memberID;
    private Date billingPeriodFrom;
    private Date billingPeriodTo;
    private double totalHours;
    private double amount;
    private String status; // PENDING / PAID / CANCELLED

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public Date getBillingPeriodFrom() {
        return billingPeriodFrom;
    }

    public void setBillingPeriodFrom(Date billingPeriodFrom) {
        this.billingPeriodFrom = billingPeriodFrom;
    }

    public Date getBillingPeriodTo() {
        return billingPeriodTo;
    }

    public void setBillingPeriodTo(Date billingPeriodTo) {
        this.billingPeriodTo = billingPeriodTo;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
