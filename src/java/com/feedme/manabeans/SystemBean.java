/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.service.Priviledge;
import com.feedme.service.PriviledgeDTO;
import com.feedme.service.Property;
import com.feedme.service.PropertyDTO;
import com.feedme.ws.Methods;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * For system configuration sections
 *
 * @author Giang
 */
@ManagedBean
@ViewScoped
public class SystemBean implements Serializable {

    private List<Priviledge> priviledgeList;
    private Priviledge priviledge, newPriviledge;
    private List<Property> propertyList;
    private Property property;
    private boolean opened, rating, pos, enabled;

    /**
     * Creates a new instance of SystemBean
     */
    public SystemBean() {
    }

    public List<Priviledge> getPriviledgeList() {
        if (priviledgeList == null) {
            priviledgeList = Methods.fetchPriviledges().stream().map(PriviledgeDTO::getPriviledge).collect(Collectors.toList());
        }
        return priviledgeList;
    }

    public void setPriviledgeList(List<Priviledge> priviledgeList) {
        this.priviledgeList = priviledgeList;
    }

    public Priviledge getPriviledge() {
        return priviledge;
    }

    public void setPriviledge(Priviledge priviledge) {
        this.priviledge = priviledge;
    }

    public Priviledge getNewPriviledge() {
        if (newPriviledge == null) {
            newPriviledge = new Priviledge();
        }
        return newPriviledge;
    }

    public void setNewPriviledge(Priviledge newPriviledge) {
        this.newPriviledge = newPriviledge;
    }

    public List<Property> getPropertyList() {
        if (propertyList == null) {
            propertyList = Methods.fetchProperties().stream().map(PropertyDTO::getProperty).collect(Collectors.toList());
            property = new Property();
        }
        return propertyList;
    }

    public void setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    public Property getProperty() {
        if (property == null) {
            property = new Property();
        }
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public void doGetPriviledge(short id) {
        priviledge = Methods.fetchPriviledgeById(id).getPriviledge();
    }

    public void doAddPriviledge() {
        PriviledgeDTO dto = new PriviledgeDTO();
        dto.setPriviledge(newPriviledge);
        boolean result = Methods.addPriviledge(dto);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (result) {
            ctx.addMessage("", new FacesMessage("Thêm quyền hạn thành công"));
        } else {
            ctx.addMessage("", new FacesMessage("Thêm quyền hạn thất bại"));
        }
        newPriviledge = null;
        priviledgeList = null;
    }

    public void doUpdatePriviledge() {
        PriviledgeDTO dto = new PriviledgeDTO();
        dto.setPriviledge(priviledge);
        boolean result = Methods.updatePriviledge(dto);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (result) {
            ctx.addMessage("", new FacesMessage("Chỉnh sửa quyền hạn thành công"));
        } else {
            ctx.addMessage("", new FacesMessage("Chỉnh sửa quyền hạn thất bại"));
        }
        priviledge = null;
        priviledgeList = null;
    }

    public void doRemovePriviledge() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (checkRemoveValidity()) {
            ctx.addMessage("", new FacesMessage("Không được phép xóa quyền hạn này"));
        }
        boolean result = Methods.removePriviledge(priviledge.getId());
        if (result) {
            ctx.addMessage("", new FacesMessage("Xóa quyền hạn thành công"));
        } else {
            ctx.addMessage("", new FacesMessage("Xóa quyền hạn thất bại"));
        }
        priviledge = null;
        priviledgeList = null;
    }

    public void doGetNewProperty() {
        property = new Property();
    }

    public void doGetProperty(String key) {
        property = Methods.fetchPropertyByKey(key).getProperty();
    }

    public void doAddProperty() {
        property.setKey("cust_" + property.getKey());
        PropertyDTO dto = new PropertyDTO();
        dto.setProperty(property);
        boolean result = Methods.addProperty(dto);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (result) {
            ctx.addMessage("", new FacesMessage("Thêm thuộc tính thành công"));
        } else {
            ctx.addMessage("", new FacesMessage("Thêm thuộc tính thất bại"));
        }
        property = null;
        propertyList = null;
    }

    public void doUpdateProperty() {
        PropertyDTO dto = new PropertyDTO();
        dto.setProperty(property);
        boolean result = Methods.updateProperties(dto);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (result) {
            ctx.addMessage("", new FacesMessage("Sửa thuộc tính thành công"));
        } else {
            ctx.addMessage("", new FacesMessage("Sửa thuộc tính thất bại"));
        }
        property = null;
        propertyList = null;
    }

    public void doRemoveProperty() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!checkCustomProperty()) {
            ctx.addMessage("", new FacesMessage("Không thể xóa thuộc tính hệ thống!"));
        } else {
            boolean result = Methods.removeProperties(property.getKey());
            if (result) {
                ctx.addMessage("", new FacesMessage("Xóa thuộc tính thành công"));
            } else {
                ctx.addMessage("", new FacesMessage("Xóa thuộc tính thất bại"));
            }
            property = null;
            propertyList = null;
        }
    }

    public void doLoadAvailbility() {
        enabled = Methods.fetchPropertyByKey("system_enabled").getValue().equals("1");
        pos = Methods.fetchPropertyByKey("system_pos").getValue().equals("1");
        rating = Methods.fetchPropertyByKey("system_rating").getValue().equals("1");
        opened = Methods.fetchPropertyByKey("system_opened").getValue().equals("1");
    }

    public void doUpdateAvailbility() {
        Property p = new Property();
        PropertyDTO dto = new PropertyDTO();
        boolean result1, result2, result3, result4;
        p.setKey("system_enabled");
        p.setValue(enabled ? "1" : "0");
        dto.setProperty(p);
        result1 = Methods.updateProperties(dto);
        p.setKey("system_pos");
        p.setValue(pos ? "1" : "0");
        dto.setProperty(p);
        result2 = Methods.updateProperties(dto);
        p.setKey("system_rating");
        p.setValue(rating ? "1" : "0");
        dto.setProperty(p);
        result3 = Methods.updateProperties(dto);
        p.setKey("system_opened");
        p.setValue(opened ? "1" : "0");
        dto.setProperty(p);
        result4 = Methods.updateProperties(dto);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (result1 && result2 && result3 && result4) {
            ctx.addMessage("", new FacesMessage("Thay đổi thuộc tính thành công"));
        } else {
            ctx.addMessage("", new FacesMessage("Thay đổi thuộc tính thất bại"));
        }
    }

    public String doGetPriviledgeContext(Priviledge p) {
        String result = "";
        if (p.isAdmin()) {
            result += "Admin ; ";
        }
        if (p.isEditor()) {
            result += "Editor ; ";
        }
        if (p.isHr()) {
            result += "HR ; ";
        }
        if (p.isManager()) {
            result += "Manager ; ";
        }
        if (p.isView()) {
            result += "View ; ";
        }
        return result.equals("") ? "Bị chặn" : result.substring(0, result.length() - 3);
    }

    /**
     * Check id there's at least 1 Administrator
     *
     * @return
     */
    private boolean checkRemoveValidity() {
        boolean adminCheck = false;
        for (Priviledge p : priviledgeList) {
            if (!Objects.equals(p.getId(), priviledge.getId())) {
                if (p.isAdmin()) {
                    adminCheck = true;
                    break;
                }
            }
        }
        return adminCheck;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isRating() {
        return rating;
    }

    public void setRating(boolean rating) {
        this.rating = rating;
    }

    public boolean isPos() {
        return pos;
    }

    public void setPos(boolean pos) {
        this.pos = pos;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean checkCustomProperty() {
        return property.getKey().contains("cust_") && property.getKey().indexOf("cust_") == 0;
    }
}
