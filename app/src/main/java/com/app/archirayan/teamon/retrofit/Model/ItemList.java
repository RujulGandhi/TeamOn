package com.app.archirayan.teamon.retrofit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemList {

    @SerializedName("items")
    @Expose
    private List<Object> items = null;
    @SerializedName("shipping_address")
    @Expose
    private ShippingAddress_ shippingAddress;

    public List<Object> getItems() {
        return items;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }

    public ShippingAddress_ getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress_ shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

}
