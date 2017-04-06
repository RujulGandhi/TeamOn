package com.app.archirayan.teamon.Model;

import java.util.ArrayList;

/**
 * Created by archirayan on 14-Dec-16.
 */

public class ProductStoreSaleDetail {

    public String title, proDescr, excerpet, proImage, regularPrice, price, auctSrtPrice, auctReservedPrice, isWhishList, proId, soldBy, rating, stepWinnerName;
    public Long time, saleQty;
    public int selectedStep;
    public Double stepWinnerPrice;
    public ArrayList<UnitStep> unitStep;
    public ArrayList<String> galleryImages;

    public String getStepWinnerName() {
        return stepWinnerName;
    }

    public void setStepWinnerName(String stepWinnerName) {
        this.stepWinnerName = stepWinnerName;
    }

    public Double getStepWinnerPrice() {
        return stepWinnerPrice;
    }

    public void setStepWinnerPrice(Double stepWinnerPrice) {
        this.stepWinnerPrice = stepWinnerPrice;
    }

    public int getSelectedStep() {

        return selectedStep;
    }

    public void setSelectedStep(int selectedStep) {
        this.selectedStep = selectedStep;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public ArrayList<UnitStep> getUnitStep() {
        return unitStep;
    }

    public void setUnitStep(ArrayList<UnitStep> unitStep) {
        this.unitStep = unitStep;
    }

    public String getProDescr() {
        return proDescr;
    }

    public void setProDescr(String proDescr) {
        this.proDescr = proDescr;
    }

    public String getIsWhishList() {
        return isWhishList;
    }

    public void setIsWhishList(String isWhishList) {
        this.isWhishList = isWhishList;
    }

    public String getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(String soldBy) {
        this.soldBy = soldBy;
    }

    public Long getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(Long saleQty) {
        this.saleQty = saleQty;
    }

    public ArrayList<String> getGalleryImages() {
        return galleryImages;
    }

    public void setGalleryImages(ArrayList<String> galleryImages) {
        this.galleryImages = galleryImages;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getExcerpet() {
        return excerpet;
    }

    public void setExcerpet(String excerpet) {
        this.excerpet = excerpet;
    }

    public String getProImage() {
        return proImage;
    }

    public void setProImage(String proImage) {
        this.proImage = proImage;
    }

    public String getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(String regularPrice) {
        this.regularPrice = regularPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAuctSrtPrice() {
        return auctSrtPrice;
    }

    public void setAuctSrtPrice(String auctSrtPrice) {
        this.auctSrtPrice = auctSrtPrice;
    }

    public String getAuctReservedPrice() {
        return auctReservedPrice;
    }

    public void setAuctReservedPrice(String auctReservedPrice) {
        this.auctReservedPrice = auctReservedPrice;
    }


}
