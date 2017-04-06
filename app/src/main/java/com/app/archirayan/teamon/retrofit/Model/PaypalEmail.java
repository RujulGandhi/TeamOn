package com.app.archirayan.teamon.retrofit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaypalEmail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("intent")
    @Expose
    private String intent;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("cart")
    @Expose
    private String cart;
    @SerializedName("payer")
    @Expose
    private Payer payer;
    @SerializedName("transactions")
    @Expose
    private List<Transaction> transactions = null;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("links")
    @Expose
    private List<Link_> links = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<Link_> getLinks() {
        return links;
    }

    public void setLinks(List<Link_> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "PaypalEmail{" +
                "id='" + id + '\'' +
                ", intent='" + intent + '\'' +
                ", state='" + state + '\'' +
                ", cart='" + cart + '\'' +
                ", payer=" + payer +
                ", transactions=" + transactions +
                ", createTime='" + createTime + '\'' +
                ", links=" + links +
                '}';
    }
}
