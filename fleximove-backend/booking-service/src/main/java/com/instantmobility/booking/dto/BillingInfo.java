package com.instantmobility.booking.dto;

import java.math.BigDecimal;

public class BillingInfo {
    private String billingModel;
    private BigDecimal rate;
    // Getters, setters
    public String getBillingModel() {
        return billingModel;
    }

    public void setBillingModel(String billingModel) {
        this.billingModel = billingModel;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}



