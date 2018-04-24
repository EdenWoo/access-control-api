package com.github.leon.sms.clicksend.xmlbean;

public class Message {
    private String to;

    private String result;

    private String errortext;

    private String price;

    private String currency_symbol;

    private String currency_type;

    private String messageid;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrortext() {
        return errortext;
    }

    public void setErrortext(String errortext) {
        this.errortext = errortext;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    @Override
    public String toString() {
        return "ClassPojo [to = " + to + ", result = " + result + ", errortext = " + errortext + ", price = " + price + ", currency_symbol = " + currency_symbol + ", currency_type = " + currency_type + ", messageid = " + messageid + "]";
    }
}