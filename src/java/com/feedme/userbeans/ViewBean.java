/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.userbeans;

import com.feedme.global.GlobalBean;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Giang
 */
@ManagedBean
@ViewScoped
public class ViewBean implements Serializable {

    private String currentPage;

    /**
     * Creates a new instance of ViewBean
     */
    public ViewBean() {
        currentPage = "";
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Inform the bean where is user's location
     *
     * @param page
     * @return
     */
    public String doSetPage(String page) {
        currentPage = page.trim();
        return page;
    }

    /**
     * Navigate to page (request a redirection)
     *
     * @param page
     * @return
     */
    public String doNavigate(String page) {
        currentPage = page.trim();
        return page;
    }

    public String doGetShortAddress() {
        try {
            String addr = GlobalBean.getPropertyValue("store_address");
            if (!addr.contains("::")) {
                return addr;
            }
            return addr.split("::")[0];
        } catch (Exception ex) {
            return "";
        }
    }

    public String doGetFullAddress() {
        try {
            String addr = GlobalBean.getPropertyValue("store_address");
            if (!addr.contains("::") || addr.split("::").length < 2) {
                return addr;
            }
            return addr.split("::")[1];
        } catch (Exception ex) {
            return "";
        }
    }

    public String doGetFavicoUrl() {
        String logo = GlobalBean.getPropertyValue("store_favico");
        if (logo != null) {
            if (logo.contains("http://")) {
                return logo;
            }
            return logo;
        }
        return "";
    }

    public String doGetOpenTimeContext() {
        if (GlobalBean.getPropertyValue("system_opened").equals("0")) {
            return "Chúng tôi tạm thời không nhận các đơn đặt hàng";
        }

        String fromStr, toStr;
        fromStr = GlobalBean.getPropertyValue("store_open");
        toStr = GlobalBean.getPropertyValue("store_close");
        if (fromStr.equals(toStr)) {
            return "Chúng tôi tạm thời không nhận các đơn đặt hàng";
        }

        Date from, to;
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        try {
            from = fmt.parse(fromStr);
            to = fmt.parse(toStr);
            fmt = new SimpleDateFormat(GlobalBean.getPropertyValue("format_time"));
        } catch (ParseException ex) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Thời gian nhận đơn hàng<br/>")
                .append("Từ ")
                .append(fmt.format(from))
                .append(" đến ")
                .append(fmt.format(to));
        return sb.toString();
    }
}
