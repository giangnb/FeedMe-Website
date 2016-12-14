/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.service.PropertyDTO;
import com.feedme.ws.Methods;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 * For system configuration sections
 * @author Giang
 */
@ManagedBean
@RequestScoped
public class SystemBean {
    /**
     * Creates a new instance of SystemBean
     */
 
    /**
     * On/Off system
     * @param enable
     * enable value is 0 or 1
     * @return 
     */
    public String doSystemEnable(int enable) {
       PropertyDTO prop = Methods.fetchPropertyByKey("system_enable");
       FacesContext ctx = FacesContext.getCurrentInstance();
       if (enable < 0 || enable > 1) {
           ctx.addMessage("", new FacesMessage("Lỗi khi bật tắt website"));
       }
       prop.setValue(String.valueOf(enable));
       if (enable==1) {
           ctx.addMessage("", new FacesMessage("Đã bật website"));
       }
       else { ctx.addMessage("", new FacesMessage("Đã tắt website"));}
       return "system_enable";       
    }
    
    /**
     * Allows Order?
     * @param enable
     * enable value is 0 or 1
     * @return 
     */
    public String doSystemOrder(int enable) {
       PropertyDTO prop = Methods.fetchPropertyByKey("system_opened");
       FacesContext ctx = FacesContext.getCurrentInstance();
       if (enable < 0 || enable > 1) {
           ctx.addMessage("", new FacesMessage("Lỗi khi bật tắt 'Cho phép đặt hàng'"));
       }
       prop.setValue(String.valueOf(enable));
       if (enable==1) {
           ctx.addMessage("", new FacesMessage("Đã bật 'Cho phép đặt hàng'"));
       }
       else { ctx.addMessage("", new FacesMessage("Đã tắt 'Cho phép đặt hàng'"));}
       return "system_order";       
    }
    
     /**
     * Allows Rating?
     * @param enable
     * enable value is 0 or 1
     * @return 
     */
    public String doSystemRating(int enable) {
       PropertyDTO prop = Methods.fetchPropertyByKey("system_rating");
       FacesContext ctx = FacesContext.getCurrentInstance();
       if (enable < 0 || enable > 1) {    
           ctx.addMessage("", new FacesMessage("Lỗi khi bật tắt 'Cho phép đánh giá đơn hàng'"));
       }
       prop.setValue(String.valueOf(enable));
       if (enable==1) {
            ctx.addMessage("", new FacesMessage("Đã bật 'Cho Phép Đánh Giá Đơn Hàng'"));
       }
       else { ctx.addMessage("", new FacesMessage("Đã tắt 'Cho Phép Đánh Giá Đơn Hàng'"));}
       return "system_rating";       
    }
    
     /**
     * On/Off POS
     * @param enable
     * enable value is 0 or 1
     * @return 
     */
    public String doSystemPos(int enable) {
       PropertyDTO prop = Methods.fetchPropertyByKey("system_pos");
       FacesContext ctx = FacesContext.getCurrentInstance();
       if (enable < 0 || enable > 1) {
           ctx.addMessage("", new FacesMessage("Lỗi khi bật tắt POS"));
       }
       prop.setValue(String.valueOf(enable));
       if (enable==1) {
           ctx.addMessage("", new FacesMessage("Đã bật POS"));
       }
       else {ctx.addMessage("", new FacesMessage("Đã tắt POS"));}
       return "system_pos";       
    }
}
