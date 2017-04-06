package com.app.archirayan.teamon.Model;

/**
 * Created by Ravi archi on 12/30/2016.
 */

public class ProductOrdersDetails {
    String OrderProName;
    String OrderId;
    String OrderProPrice;
    String OrderProSalerName;
    String OrderProImage;
    String OrderStatus;
    String OrderQty;
    String OrderPostDate;
    String OrderKey;
    String OrderCompletionDate;

    public String getOrderPostDate() {
        return OrderPostDate;
    }

    public void setOrderPostDate(String orderPostDate) {
        OrderPostDate = orderPostDate;
    }

    public String getOrderCompletionDate() {
        return OrderCompletionDate;
    }

    public void setOrderCompletionDate(String orderCompletionDate) {
        OrderCompletionDate = orderCompletionDate;
    }

    public String getOrderKey() {
        return OrderKey;
    }

    public void setOrderKey(String orderKey) {
        OrderKey = orderKey;
    }


    public String getOrderQty() {
        return OrderQty;
    }

    public void setOrderQty(String orderQty) {
        OrderQty = orderQty;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getOrderProImage() {
        return OrderProImage;
    }

    public void setOrderProImage(String orderProImage) {
        OrderProImage = orderProImage;
    }

    public String getOrderProSalerName() {
        return OrderProSalerName;
    }

    public void setOrderProSalerName(String orderProSalerName) {
        OrderProSalerName = orderProSalerName;
    }

    public String getOrderProPrice() {
        return OrderProPrice;
    }

    public void setOrderProPrice(String orderProPrice) {
        OrderProPrice = orderProPrice;
    }

    public String getOrderProName() {
        return OrderProName;
    }

    public void setOrderProName(String orderProName) {
        OrderProName = orderProName;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }
}
