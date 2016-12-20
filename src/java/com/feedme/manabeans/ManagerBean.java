/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.service.PropertyDTO;
import com.feedme.ws.Methods;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * For manager's management and configuration sections
 *
 * @author Giang
 */
@ManagedBean
@RequestScoped
public class ManagerBean {

    private String contextTitle, contextDelivery, contextIntro, contextTerm,
            contextTrack, contxtReview, contextClosed, contextSecure,
            contextEmptyCart, contactManager, contactAdmin;
    private String infoEmployee, infoManager, infoProduct, fmtDate, fmtTime, fmtNumber, fmtCurrency;
    private String storeName, storeOpen, storeClose, storeAddress, storePhone, storeLogo, storeFavico;
    private String currencyBefore, currencyAfter;
    private int numberDecimals, numberGroup;
    private boolean isFilled = false;

    public ManagerBean() {
    }

    public void doUpdateContext() {
        PropertyDTO p = new PropertyDTO();
        p.setKey("context_title");
        p.setValue(contextTitle);
        Methods.updateProperties(p);
        p.setKey("context_delivery");
        p.setValue(contextDelivery);
        Methods.updateProperties(p);
        p.setKey("context_introduction");
        p.setValue(contextIntro);
        Methods.updateProperties(p);
        p.setKey("context_term");
        p.setValue(contextTerm);
        Methods.updateProperties(p);
        p.setKey("context_track");
        p.setValue(contextTrack);
        Methods.updateProperties(p);
        p.setKey("context_review");
        p.setValue(contxtReview);
        Methods.updateProperties(p);
        p.setKey("context_closed");
        p.setValue(contextClosed);
        Methods.updateProperties(p);
        p.setKey("context_secure");
        p.setValue(contextSecure);
        Methods.updateProperties(p);
        p.setKey("context_empty_cart");
        p.setValue(contextEmptyCart);
        Methods.updateProperties(p);
        p.setKey("contact_manager");
        p.setValue(contactManager);
        Methods.updateProperties(p);
        p.setKey("contact_admin");
        p.setValue(contactAdmin);
        Methods.updateProperties(p);
    }

    public void doUpdateFormat() {
        PropertyDTO p = new PropertyDTO();
        p.setKey("info_employee");
        p.setValue(infoEmployee);
        Methods.updateProperties(p);
        p.setKey("info_manager");
        p.setValue(infoManager);
        Methods.updateProperties(p);
        p.setKey("info_product");
        p.setValue(infoProduct);
        Methods.updateProperties(p);
        p.setKey("format_date");
        p.setValue(fmtDate);
        Methods.updateProperties(p);
        p.setKey("format_time");
        p.setValue(fmtTime);
        Methods.updateProperties(p);
        p.setKey("format_number");
        fmtNumber = numberGroup > 0 ? "#," : "#";
        for (int i = 0; i < numberGroup; i++) {
            fmtNumber += "#";
        }
        if (numberDecimals > 0) {
            fmtNumber += ".";
            for (int i = 0; i < numberDecimals; i++) {
                fmtNumber += isFilled ? "0" : "#";
            }
        }
        p.setValue(fmtNumber);
        Methods.updateProperties(p);
        p.setKey("format_currency");
        fmtCurrency = currencyBefore + "?" + currencyAfter;
        p.setValue(fmtCurrency);
        Methods.updateProperties(p);
    }

    public String getContextTitle() {
        contextTitle = Methods.fetchPropertyByKey("context_title").getValue();
        return contextTitle;
    }

    public void setContextTitle(String contextTitle) {
        this.contextTitle = contextTitle;
    }

    public String getContextDelivery() {
        contextDelivery = Methods.fetchPropertyByKey("context_delivery").getValue();
        return contextDelivery;
    }

    public void setContextDelivery(String contextDelivery) {
        this.contextDelivery = contextDelivery;
    }

    public String getContextIntro() {
        contextIntro = Methods.fetchPropertyByKey("context_introduction").getValue();
        return contextIntro;
    }

    public void setContextIntro(String contextIntro) {
        this.contextIntro = contextIntro;
    }

    public String getContextTerm() {
        contextTerm = Methods.fetchPropertyByKey("context_term").getValue();
        return contextTerm;
    }

    public void setContextTerm(String contextTerm) {
        this.contextTerm = contextTerm;
    }

    public String getContextTrack() {
        contextTrack = Methods.fetchPropertyByKey("context_track").getValue();
        return contextTrack;
    }

    public void setContextTrack(String contextTrack) {
        this.contextTrack = contextTrack;
    }

    public String getContxtReview() {
        contxtReview = Methods.fetchPropertyByKey("context_review").getValue();
        return contxtReview;
    }

    public void setContxtReview(String contxtReview) {
        this.contxtReview = contxtReview;
    }

    public String getContextClosed() {
        contextClosed = Methods.fetchPropertyByKey("context_closed").getValue();
        return contextClosed;
    }

    public void setContextClosed(String contextClosed) {
        this.contextClosed = contextClosed;
    }

    public String getContextSecure() {
        contextSecure = Methods.fetchPropertyByKey("context_secure").getValue();
        return contextSecure;
    }

    public void setContextSecure(String contextSecure) {
        this.contextSecure = contextSecure;
    }

    public String getContextEmptyCart() {
        contextEmptyCart = Methods.fetchPropertyByKey("context_empty_cart").getValue();
        return contextEmptyCart;
    }

    public void setContextEmptyCart(String contextEmptyCart) {
        this.contextEmptyCart = contextEmptyCart;
    }

    public String getContactManager() {
        contactManager = Methods.fetchPropertyByKey("contact_manager").getValue();
        return contactManager;
    }

    public void setContactManager(String contactManager) {
        this.contactManager = contactManager;
    }

    public String getContactAdmin() {
        contactAdmin = Methods.fetchPropertyByKey("contact_admin").getValue();
        return contactAdmin;
    }

    public void setContactAdmin(String contactAdmin) {
        this.contactAdmin = contactAdmin;
    }

    public String getInfoEmployee() {
        infoEmployee = Methods.fetchPropertyByKey("info_employee").getValue();
        return infoEmployee;
    }

    public void setInfoEmployee(String infoEmployee) {
        this.infoEmployee = infoEmployee;
    }

    public String getInfoManager() {
        infoManager = Methods.fetchPropertyByKey("info_manager").getValue();
        return infoManager;
    }

    public void setInfoManager(String infoManager) {
        this.infoManager = infoManager;
    }

    public String getInfoProduct() {
        infoProduct = Methods.fetchPropertyByKey("info_product").getValue();
        return infoProduct;
    }

    public void setInfoProduct(String infoProduct) {
        this.infoProduct = infoProduct;
    }

    public String getFmtDate() {
        fmtDate = Methods.fetchPropertyByKey("format_date").getValue();
        return fmtDate;
    }

    public void setFmtDate(String fmtDate) {
        this.fmtDate = fmtDate;
    }

    public String getFmtTime() {
        fmtTime = Methods.fetchPropertyByKey("format_time").getValue();
        return fmtTime;
    }

    public void setFmtTime(String fmtTime) {
        this.fmtTime = fmtTime;
    }

    public String getFmtNumber() {
        fmtNumber = Methods.fetchPropertyByKey("format_number").getValue();
        return fmtNumber;
    }

    public void setFmtNumber(String fmtNumber) {
        this.fmtNumber = fmtNumber;
    }

    public String getFmtCurrency() {
        fmtCurrency = Methods.fetchPropertyByKey("format_currency").getValue();
        return fmtCurrency;
    }

    public void setFmtCurrency(String fmtCurrency) {
        this.fmtCurrency = fmtCurrency;
    }

    public String getStoreName() {
        storeName = Methods.fetchPropertyByKey("store_name").getValue();
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreOpen() {
        storeOpen = Methods.fetchPropertyByKey("store_open").getValue();
        return storeOpen;
    }

    public void setStoreOpen(String storeOpen) {
        this.storeOpen = storeOpen;
    }

    public String getStoreClose() {
        storeClose = Methods.fetchPropertyByKey("store_close").getValue();
        return storeClose;
    }

    public void setStoreClose(String storeClose) {
        this.storeClose = storeClose;
    }

    public String getStoreAddress() {
        storeAddress = Methods.fetchPropertyByKey("store_address").getValue();
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStorePhone() {
        storePhone = Methods.fetchPropertyByKey("store_phone").getValue();
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreLogo() {
        storeLogo = Methods.fetchPropertyByKey("store_logo").getValue();
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getStoreFavico() {
        Methods.fetchPropertyByKey("store_favico").getValue();
        return storeFavico;
    }

    public void setStoreFavico(String storeFavico) {
        this.storeFavico = storeFavico;
    }

    public String getCurrencyBefore() {
        String c = getFmtCurrency();
        try {
            currencyBefore = c.split("::")[0];
        } catch (Exception ex) {
        }
        return currencyBefore;
    }

    public void setCurrencyBefore(String currencyBefore) {
        this.currencyBefore = currencyBefore;
    }

    public String getCurrencyAfter() {
        String c = getFmtCurrency();
        try {
            currencyBefore = c.split("::")[1];
        } catch (Exception ex) {
        }
        return currencyAfter;
    }

    public void setCurrencyAfter(String currencyAfter) {
        this.currencyAfter = currencyAfter;
    }

    public int getNumberDecimals() {
        String s = getFmtNumber();
        try {
            numberDecimals = s.split(".")[1].length();
        } catch (Exception ex) {
        }
        return numberDecimals;
    }

    public void setNumberDecimals(int numberDecimals) {
        this.numberDecimals = numberDecimals;
    }

    public int getNumberGroup() {
        String s = getFmtNumber();
        try {
            s = s.split(".")[0];
            numberGroup = 0;
            for (String str : s.split(",")) {
                if (str.length() > numberGroup) {
                    numberGroup = str.length();
                }
            }
        } catch (Exception ex) {
        }
        return numberGroup;
    }

    public void setNumberGroup(int numberGroup) {
        this.numberGroup = numberGroup;
    }

    public boolean isIsFilled() {
        String s = getFmtNumber();
        if (s.contains("0")) {
            isFilled = true;
        }
        return isFilled;
    }

    public void setIsFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }
}
