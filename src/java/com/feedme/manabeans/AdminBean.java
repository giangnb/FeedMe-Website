/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.global.GlobalBean;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 * Utilities for manager's site
 *
 * @author Giang
 */
@ManagedBean
@RequestScoped
public class AdminBean {

    /**
     * Creates a new instance of AdminBean
     */
    public AdminBean() {
    }

    public String doNavigate(String section) {
        return section;
    }

    public String doGetOut() {
        return "../index";
    }

    public String doSubString(String str, int count, String suffix) {
        return str.length() > count - 1 ? str.substring(0, count - 1) + suffix : str;
    }

    public String doGetLogoUrl() {
        String logo = GlobalBean.getPropertyValue("store_logo");
        if (logo != null) {
            if (logo.contains("http://")) {
                return logo;
            }
            return "." + logo;
        }
        return "";
    }

    public String doGetFavicoUrl() {
        String logo = GlobalBean.getPropertyValue("store_favico");
        if (logo != null) {
            if (logo.contains("http://")) {
                return logo;
            }
            return "." + logo;
        }
        return "";
    }

    public void eventIdle() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Không có hoạt động", "Bạn không sử dụng trang web này trong 10 phút. Phiên làm việc sẽ kết thúc trong 5 phút nữa nếu không có hoạt động nào."));
    }

    public void eventActive() {
        Iterator<FacesMessage> msgIterator = FacesContext.getCurrentInstance().getMessages();
        while (msgIterator.hasNext()) {
            msgIterator.next();
            msgIterator.remove();
        }
    }
}
