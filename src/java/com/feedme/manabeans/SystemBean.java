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
 * For system configuration sections
 * @author Giang
 */
@ManagedBean
@RequestScoped
public class SystemBean {
 private boolean turnOnOff;
    /**
     * Creates a new instance of SystemBean
     */
    public SystemBean() {
    }

    public boolean isTurnOnOff() {
        return turnOnOff;
    }

    public void setTurnOnOff(boolean turnOnOff) {
        this.turnOnOff = turnOnOff;
    }
    
    /**
     * Turn on/off system
     */
    public String doSetupSystem() {
        PropertyDTO prop = Methods.fetchPropertyByKey("system_enable");
        return "";
    }
}
