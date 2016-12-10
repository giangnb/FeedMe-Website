/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.userbeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Giang
 */
@ManagedBean
@RequestScoped
public class ViewBean {
    private String currentPage;
    
    /**
     * Creates a new instance of ViewBean
     */
    public ViewBean() {
        currentPage = "";
    }
    
    /**
     * Inform the bean where is user's location
     * @param page
     * @return 
     */
    public String doSetPage(String page) {
        currentPage = page.trim();
        return page;
    }
    
    /**
     * Navigate to page (request a redirection)
     * @param page
     * @return 
     */
    public String doNavigate(String page) {
        currentPage = page.trim();
        return page;
    }
}
