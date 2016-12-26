/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.userbeans;

import com.feedme.service.OrderDetail;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Managed bean for processing the Order tracking function
 * @author Giang
 */
@ManagedBean
@ViewScoped
public class OrderBean implements Serializable{
    private OrderDetail order;
    private String trackingCode;
    private long timeStamp;
    private String phoneNumber;
    
    /**
     * Creates a new instance of OrderBean
     */
    public OrderBean() {
    }

    public OrderDetail getOrder() {
        return order;
    }

    public void setOrder(OrderDetail order) {
        this.order = order;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
        timeStamp = Long.parseLong(trackingCode, Character.MAX_RADIX);
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
}
