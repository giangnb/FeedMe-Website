/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
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
}
