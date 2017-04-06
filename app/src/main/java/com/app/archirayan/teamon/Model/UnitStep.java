package com.app.archirayan.teamon.Model;

/**
 * Created by archirayan on 19-Dec-16.
 */

public class UnitStep {
    public String name, fromUnitStep, toUnit, perUnitPrice, userId, userImage, userAddress, aboutStore;

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getAboutStore() {
        return aboutStore;
    }

    public void setAboutStore(String aboutStore) {
        this.aboutStore = aboutStore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getFromUnitStep() {
        return fromUnitStep;
    }

    public void setFromUnitStep(String fromUnitStep) {
        this.fromUnitStep = fromUnitStep;
    }

    public String getToUnit() {
        return toUnit;
    }

    public void setToUnit(String toUnit) {
        this.toUnit = toUnit;
    }

    public String getPerUnitPrice() {
        return perUnitPrice;
    }

    public void setPerUnitPrice(String perUnitPrice) {
        this.perUnitPrice = perUnitPrice;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
