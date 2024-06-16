package com.example.selectionofperformers.Entity;

public class Deal {
    private String idDeal;
    private String dealName;
    private String dealDate;
    private String dealAmount;
    private String paymentStatus;

    public Deal(String idDeal, String dealName, String dealDate, String dealAmount, String paymentStatus) {
        this.idDeal = idDeal;
        this.dealName = dealName;
        this.dealDate = dealDate;
        this.dealAmount = dealAmount;
        this.paymentStatus = paymentStatus;
    }

    public String getDealId() {
        return idDeal ;
    }

    public String getDealName() {
        return dealName;
    }

    public String getDealDate() {
        return dealDate;
    }

    public String getDealAmount() {
        return dealAmount;
    }
    public String getPaymentStatus() {
        return paymentStatus;
    }


    public void setDealId() {
        this.idDeal = idDeal;
    }

    public void setDealName() {
        this.dealName = dealName;
    }

    public void setDealDate() {
        this.dealDate = dealDate;
    }

    public void setDealAmount() {
        this.dealAmount = dealAmount;
    }
    public void setPaymentStatus() {
        this.paymentStatus = paymentStatus;
    }
}
