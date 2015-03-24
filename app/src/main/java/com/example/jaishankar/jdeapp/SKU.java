package com.example.jaishankar.jdeapp;

/**
 * Created by jaishankar on 18/3/15.
 */
public class SKU {

    private String name;
    private Double amount;
    private String currency;

    public SKU(String name, Double amount, String currency){
        this.name = name;
        this.amount = amount;
        this.currency = currency;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
