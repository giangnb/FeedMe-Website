/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.global.GlobalBean;
import com.feedme.service.Property;
import com.feedme.service.PropertyDTO;
import com.feedme.ws.Methods;
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

    public String doGreeting() {
        java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("HH");
        int hr = Integer.parseInt(fmt.format(new java.util.Date()));
        if (6 < hr && hr <= 11) {
            return "Chúc một buổi sáng làm việc hiệu quả!";
        }
        if (11 < hr && hr <= 13) {
            return "Hãy phục vụ những bữa trưa chất lượng!";
        }
        if (13 < hr && hr <= 17) {
            return "Chúc buổi chiều làm việc vui vẻ!";
        }
        if (17 < hr && hr <= 20) {
            return "Hãy làm thực khách hài lòng với bữa tối bạn phục vụ!";
        }
        if (20 < hr && hr <= 21) {
            return "Mong rằng bạn đã có một ngày làm việc tuyệt vời.";
        }
        if ((21 < hr && hr <= 24) || (0 <= hr && hr <= 3)) {
            return "Màn đêm buông xuống...";
        }
        return "Chúc bạn khởi đầu ngày mới an lành!";
    }

    /* HOMEPAGE CONTEXT MANAGEMENT */
    private String adminContext;

    public String getAdminContext() {
        adminContext = GlobalBean.getPropertyValue("context_admin");
        return adminContext;
    }

    public void setAdminContext(String adminContext) {
        this.adminContext = adminContext;
    }

    public void doUpdateAdminContext() {
        Property p = new Property();
        p.setKey("context_admin");
        p.setValue(adminContext);
        PropertyDTO dto = new PropertyDTO();
        dto.setProperty(p);
        Methods.updateProperties(dto);
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Thay đổi nội dung thao báo thành công."));
    }
}
