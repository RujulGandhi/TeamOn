package com.app.archirayan.teamon.retrofit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Details_ {

    @SerializedName("subtotal")
    @Expose
    private String subtotal;

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

}
