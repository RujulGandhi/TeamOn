package com.app.archirayan.teamon.Model;

/**
 * Created by archirayan on 18-Jan-17.
 */

public class OrderDetails {
    public String orderId, orderStatus, orderDate, sellerName, orderTotal, orderTitle, orderQty, orderPrice, orderImage, sellerShopImage, sellerStoreAddress, sellerShopDescription, orderOriginalPrice, shippingMethod;

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderOriginalPrice() {
        return orderOriginalPrice;
    }

    public void setOrderOriginalPrice(String orderOriginalPrice) {
        this.orderOriginalPrice = orderOriginalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderImage() {
        return orderImage;
    }

    public void setOrderImage(String orderImage) {
        this.orderImage = orderImage;
    }

    public String getSellerShopImage() {
        return sellerShopImage;
    }

    public void setSellerShopImage(String sellerShopImage) {
        this.sellerShopImage = sellerShopImage;
    }

    public String getSellerStoreAddress() {
        return sellerStoreAddress;
    }

    public void setSellerStoreAddress(String sellerStoreAddress) {
        this.sellerStoreAddress = sellerStoreAddress;
    }

    public String getSellerShopDescription() {
        return sellerShopDescription;
    }

    public void setSellerShopDescription(String sellerShopDescription) {
        this.sellerShopDescription = sellerShopDescription;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderId='" + orderId + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", orderTotal='" + orderTotal + '\'' +
                ", orderTitle='" + orderTitle + '\'' +
                ", orderQty='" + orderQty + '\'' +
                ", orderPrice='" + orderPrice + '\'' +
                ", orderImage='" + orderImage + '\'' +
                ", sellerShopImage='" + sellerShopImage + '\'' +
                ", sellerStoreAddress='" + sellerStoreAddress + '\'' +
                ", sellerShopDescription='" + sellerShopDescription + '\'' +
                ", orderOriginalPrice='" + orderOriginalPrice + '\'' +
                ", shippingMethod='" + shippingMethod + '\'' +
                '}';
    }
}
