/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.service.ManagerDTO;
import com.feedme.service.PriviledgeDTO;
import com.feedme.ws.Methods;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Giang
 */
@ManagedBean
@SessionScoped
public class AccountBean {

    private String user;
    private String pass;
    private ManagerDTO manager;
    private PriviledgeDTO priv;
    private short id;

    /**
     * Creates a new instance of AccountBean
     */
    public AccountBean() {
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public ManagerDTO getManager() {
        return manager;
    }

    public void setManager(ManagerDTO manager) {
        this.manager = manager;
    }

    public PriviledgeDTO getPriv() {
        return priv;
    }

    public void setPriv(PriviledgeDTO priv) {
        this.priv = priv;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }
    
    public String doLoginManager() {
        manager = Methods.loginManager(user, pass);
        if (manager == null) {
            return "page_login";  //Manager login page
        }
        return "manager_page";  //Manager page
    }

    public String doLogoutManager() {
        manager = null;
        return "page_login";
    }
    
    public String changePassword() {
        try {
            Methods.updateManagerPassword(manager);
            return "success";  
        } catch (Exception e) {
            return "fail";
        }
    }
    
    public String changeInfo() {
      return"";
    }

    public boolean checkIsAdmin() {
        priv = Methods.fetchPriviledgeById(id);
        return priv.isAdmin();
    }

    public boolean checkIsManager() {
        priv = Methods.fetchPriviledgeById(id);
        return priv.isManager();
    }

    public boolean checkIsView() {
        priv = Methods.fetchPriviledgeById(id);
        return priv.isView();
    }

    public boolean checkIsHr() {
        priv = Methods.fetchPriviledgeById(id);
        return priv.isHr();
    }
    
    public boolean checkIsEditor() {
        priv = Methods.fetchPriviledgeById(id);
        return priv.isEditor();
    }  
}
