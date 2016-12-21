/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.service.PropertyDTO;
import com.feedme.ws.Methods;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;

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
    private String numberDecimalsStr, numberGroupStr;
    private boolean isFilled = false;
    private Part fileLogo, fileFavico;
    private HashMap<String, String> properties;

    public ManagerBean() {
        doFetchData();
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
        doFetchData();
    }

    public void doUpdateFormat() {
        PropertyDTO p = new PropertyDTO();
        p.setKey("info_employee");
        p.setValue(infoEmployee.replace("\n", ";"));
        Methods.updateProperties(p);
        p.setKey("info_manager");
        p.setValue(infoManager.replace("\n", ";"));
        Methods.updateProperties(p);
        p.setKey("info_product");
        p.setValue(infoProduct.replace("\n", ";"));
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
        fmtCurrency = currencyBefore + "::" + currencyAfter;
        p.setValue(fmtCurrency);
        System.out.println(fmtCurrency);
        Methods.updateProperties(p);
        doFetchData();
    }

    public void doUpdateStore() {
        PropertyDTO p = new PropertyDTO();
        p.setKey("store_name");
        p.setValue(storeName);
        Methods.updateProperties(p);
        p.setKey("store_open");
        p.setValue(storeOpen);
        Methods.updateProperties(p);
        p.setKey("store_close");
        p.setValue(storeClose);
        Methods.updateProperties(p);
        p.setKey("store_address");
        p.setValue(storeAddress);
        Methods.updateProperties(p);
        p.setKey("store_phone");
        p.setValue(storePhone);
        Methods.updateProperties(p);
        p.setKey("store_logo");
        p.setValue(storeLogo == null ? "" : storeLogo);
        Methods.updateProperties(p);
        p.setKey("store_favico");
        p.setValue(storeFavico == null ? "" : storeFavico);
        Methods.updateProperties(p);
        doFetchData();
    }

    public String doUploadLogo() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (validateFile(fileLogo)) {
            try (InputStream input = fileLogo.getInputStream()) {
                String realPath = ((ServletContext) ctx.getExternalContext().getContext()).getRealPath("/");
                System.out.println(realPath);
                Files.copy(input, new File(realPath + "/resources/uploads/store-logo.png").toPath(), StandardCopyOption.REPLACE_EXISTING);
                ctx.addMessage("", new FacesMessage("Upload thành công", "Đã cập nhật logo cửa hàng"));
                PropertyDTO p = new PropertyDTO();
                p.setKey("store_logo");
                p.setValue("./javax.faces.resource/uploads/store-logo.png.xhtml");
                Methods.updateProperties(p);
            } catch (IOException e) {
                ctx.addMessage("", new FacesMessage("Upload thất bại", e.getMessage()));
                e.printStackTrace();
            }
        } else {
            ctx.addMessage("", new FacesMessage("Tập tin không hợp lệ"));
        }
        return "store";
    }

    public String doUploadFavico() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (validateFile(fileFavico)) {
            try (InputStream input = fileFavico.getInputStream()) {
                String realPath = ((ServletContext) ctx.getExternalContext().getContext()).getRealPath("/");
                System.out.println(realPath);
                Files.copy(input, new File(realPath + "/resources/uploads/store-favico.ico").toPath(), StandardCopyOption.REPLACE_EXISTING);
                ctx.addMessage("", new FacesMessage("Upload thành công", "Đã cập nhật icon website cửa hàng"));
                PropertyDTO p = new PropertyDTO();
                p.setKey("store_favico");
                p.setValue("./javax.faces.resource/uploads/store-favico.ico.xhtml");
                Methods.updateProperties(p);
            } catch (IOException e) {
                ctx.addMessage("", new FacesMessage("Upload thất bại", e.getMessage()));
            }
        } else {
            ctx.addMessage("", new FacesMessage("Tập tin không hợp lệ"));
        }
        return "store";
    }

    public String doTestFormatDateTime() {
        try {
            return new SimpleDateFormat(fmtDate + " " + fmtTime).format(new Date());
        } catch (Exception ex) {
            return "ĐỊNH DẠNG SAI";
        }
    }

    public String doTestFormatNumber() {
        try {
            String fmt = numberGroup > 0 ? "#," : "#";
            for (int i = 0; i < numberGroup; i++) {
                fmt += "#";
            }
            long time = new Date().getTime();
            String str = time / 1000 + "";
            if (numberDecimals > 0) {
                fmt += ".";
                for (int i = 0; i < numberDecimals; i++) {
                    fmt += isFilled ? "0" : "#";
                }
                str += "." + (time % 1000);
            }
            return new DecimalFormat(fmt).format(Double.parseDouble(str));
        } catch (Exception ex) {
            return "ĐỊNH DANG SAI";
        }
    }

    public String doTestFormatMoney() {
        try {
            return currencyBefore == null ? "" : currencyBefore
                    + doTestFormatNumber()
                    + currencyAfter == null ? "" : currencyAfter;
        } catch (Exception ex) {
            return "ĐỊNH DẠNG SAI";
        }
    }

    public String getContextTitle() {
        contextTitle = properties.get("context_title");
        return contextTitle;
    }

    public void setContextTitle(String contextTitle) {
        this.contextTitle = contextTitle;
    }

    public String getContextDelivery() {
        contextDelivery = properties.get("context_delivery");
        return contextDelivery;
    }

    public void setContextDelivery(String contextDelivery) {
        this.contextDelivery = contextDelivery;
    }

    public String getContextIntro() {
        contextIntro = properties.get("context_introduction");
        return contextIntro;
    }

    public void setContextIntro(String contextIntro) {
        this.contextIntro = contextIntro;
    }

    public String getContextTerm() {
        contextTerm = properties.get("context_term");
        return contextTerm;
    }

    public void setContextTerm(String contextTerm) {
        this.contextTerm = contextTerm;
    }

    public String getContextTrack() {
        contextTrack = properties.get("context_track");
        return contextTrack;
    }

    public void setContextTrack(String contextTrack) {
        this.contextTrack = contextTrack;
    }

    public String getContxtReview() {
        contxtReview = properties.get("context_review");
        return contxtReview;
    }

    public void setContxtReview(String contxtReview) {
        this.contxtReview = contxtReview;
    }

    public String getContextClosed() {
        contextClosed = properties.get("context_closed");
        return contextClosed;
    }

    public void setContextClosed(String contextClosed) {
        this.contextClosed = contextClosed;
    }

    public String getContextSecure() {
        contextSecure = properties.get("context_secure");
        return contextSecure;
    }

    public void setContextSecure(String contextSecure) {
        this.contextSecure = contextSecure;
    }

    public String getContextEmptyCart() {
        contextEmptyCart = properties.get("context_empty_cart");
        return contextEmptyCart;
    }

    public void setContextEmptyCart(String contextEmptyCart) {
        this.contextEmptyCart = contextEmptyCart;
    }

    public String getContactManager() {
        contactManager = properties.get("contact_manager");
        return contactManager;
    }

    public void setContactManager(String contactManager) {
        this.contactManager = contactManager;
    }

    public String getContactAdmin() {
        contactAdmin = properties.get("contact_admin");
        return contactAdmin;
    }

    public void setContactAdmin(String contactAdmin) {
        this.contactAdmin = contactAdmin;
    }

    public String getInfoEmployee() {
        infoEmployee = properties.get("info_employee");
        return infoEmployee;
    }

    public void setInfoEmployee(String infoEmployee) {
        this.infoEmployee = infoEmployee;
    }

    public String getInfoManager() {
        infoManager = properties.get("info_manager");
        return infoManager;
    }

    public void setInfoManager(String infoManager) {
        this.infoManager = infoManager;
    }

    public String getInfoProduct() {
        infoProduct = properties.get("info_product");
        return infoProduct;
    }

    public void setInfoProduct(String infoProduct) {
        this.infoProduct = infoProduct;
    }

    public String getFmtDate() {
        fmtDate = properties.get("format_date");
        return fmtDate;
    }

    public void setFmtDate(String fmtDate) {
        this.fmtDate = fmtDate;
    }

    public String getFmtTime() {
        fmtTime = properties.get("format_time");
        return fmtTime;
    }

    public void setFmtTime(String fmtTime) {
        this.fmtTime = fmtTime;
    }

    public String getFmtNumber() {
        fmtNumber = properties.get("format_number");
        String[] decimal = fmtNumber.split(".");
        try {
            numberDecimals = decimal[1].length();
            if (decimal[1].contains("0")) {
                isFilled = true;
            }
        } catch (Exception ex) {
        }
        try {
            String[] group;
            if (decimal.length <= 0) {
                group = fmtNumber.split(",");
            } else {
                group = decimal[0].split(",");
            }
            numberGroup = group[group.length - 1].length();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fmtNumber;
    }

    public void setFmtNumber(String fmtNumber) {
        this.fmtNumber = fmtNumber;
    }

    public String getFmtCurrency() {
        fmtCurrency = properties.get("format_currency");
        currencyAfter = "";
        currencyBefore = "";
        try {
            String[] cur = fmtCurrency.split("::");
            currencyBefore = cur[0];
            currencyAfter = cur[1];
        } catch (Exception ex) {
        }
        return fmtCurrency;
    }

    public void setFmtCurrency(String fmtCurrency) {
        this.fmtCurrency = fmtCurrency;
    }

    public String getStoreName() {
        storeName = properties.get("store_name");
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreOpen() {
        storeOpen = properties.get("store_open");
        return storeOpen;
    }

    public void setStoreOpen(String storeOpen) {
        this.storeOpen = storeOpen;
    }

    public String getStoreClose() {
        storeClose = properties.get("store_close");
        return storeClose;
    }

    public void setStoreClose(String storeClose) {
        this.storeClose = storeClose;
    }

    public String getStoreAddress() {
        storeAddress = properties.get("store_address");
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStorePhone() {
        storePhone = properties.get("store_phone");
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreLogo() {
        storeLogo = properties.get("store_logo");
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getStoreFavico() {
        storeFavico = properties.get("store_favico");
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
        currencyBefore = currencyBefore == null ? "" : currencyBefore;
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
        currencyAfter = currencyAfter == null ? "" : currencyAfter;
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

    public String getNumberDecimalsStr() {
        return numberDecimals + "";
    }

    public void setNumberDecimalsStr(String numberDecimalsStr) {
        try {
            this.numberDecimals = Integer.parseInt(numberDecimalsStr);
        } catch (NumberFormatException ex) {
        }
    }

    public String getNumberGroupStr() {
        return numberGroup + "";
    }

    public void setNumberGroupStr(String numberGroupStr) {
        try {
            this.numberGroup = Integer.parseInt(numberGroupStr);
        } catch (NumberFormatException ex) {
        }
    }

    private void doFetchData() {
        properties = new HashMap<>();
        Methods.fetchProperties().forEach((p) -> {
            properties.put(p.getKey(), p.getValue());
        });
    }

    public Part getFileLogo() {
        return fileLogo;
    }

    public void setFileLogo(Part fileLogo) {
        this.fileLogo = fileLogo;
    }

    public Part getFileFavico() {
        return fileFavico;
    }

    public void setFileFavico(Part fileFavico) {
        this.fileFavico = fileFavico;
    }

    private boolean validateFile(Part file) {
        if (file == null) {
            return false;
        }
        if (!file.getContentType().contains("image/")) {
            return false;
        }
        if (file.getSize() > 1000 * 1000 * 3) {
            return false;
        }
        return true;
    }
}
