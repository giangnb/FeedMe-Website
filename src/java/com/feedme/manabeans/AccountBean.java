/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.service.ManagerDTO;
import com.feedme.service.PriviledgeDTO;
import com.feedme.ws.Methods;
import java.io.Serializable;
import java.util.Timer;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * For current manager's account
 *
 * @author Giang
 */
@ManagedBean
@SessionScoped
public class AccountBean implements Serializable{

    private String user;
    private String pass;
    private ManagerDTO manager;
    private PriviledgeDTO priv;

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

    public String doLogin() {
        manager = Methods.loginManager(user, pass);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (user.length()<5 || pass.length()<5) {
            ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lỗi đăng nhập", "Tên người dùng hoặc mật khẩu quá ngắn"));
            return "login";
        }
        if (manager == null) {
            user = "";
            pass = "";
            ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Đăng nhập thất bại", "Người dùng hoặc mật khẩu sai"));
            return "login";  //Manager login page
        }
        priv = manager.getPriviledge();
        if (priviledgeNone()) {
            ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Đăng nhập thất bại", "Tài khoản bị vô hiệu hóa"));
            return "login";
        }
        // Create session
        HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
        session.setAttribute("isLoggedIn", "1");
        return "index";  //Manager page
    }

    public String doLogout() {
        manager = null;
        // Invalidate session
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.addMessage("", new FacesMessage("Đã đăng xuất"));
        ctx.getExternalContext().getSessionMap().clear();
        HttpSession session = (HttpSession) ctx.getExternalContext().getSession(false);
        session.setAttribute("isLoggedIn", "0");
        return "login";
    }

    public String doChangePassword() {
        try {
            Methods.updateManagerPassword(manager);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }

    public String doChangeInfo() {
        boolean result = Methods.updateManager(manager);
        if (!result) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage("failed", new FacesMessage("Không thể sửa thông tin. Vui lòng thử lại!"));
        }
        return "#";
    }

    public boolean priviledgeAdmin() {
        return priv.isAdmin();
    }

    public boolean priviledgeManager() {
        return priv.isManager();
    }

    public boolean priviledgeView() {
        return priv.isView();
    }

    public boolean priviledgeHR() {
        return priv.isHr();
    }

    public boolean priviledgeEditor() {
        return priv.isEditor();
    }

    public boolean priviledgeNone() {
        return !(priviledgeAdmin() || priviledgeEditor() || priviledgeHR() || priviledgeManager() || priviledgeView());
    }

    public void setUser(String user) {
        this.user = user;
    }
    
    /* CURRENT ACCOUNT MANAGEMENTS */
    public String accountInfo, newPass, rePass;

    public String getAccountInfo() {
        accountInfo = manager.getInfo();
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getRePass() {
        return rePass;
    }

    public void setRePass(String rePass) {
        this.rePass = rePass;
    }
    
    public void doUpdateAccountInfo() {
        manager.setInfo(accountInfo);
        boolean result = Methods.updateManager(manager);
        if (result) {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Thành công", "Đã thay đổi thông tin cá nhân"));
        } else {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Thất bại", "Hãy thử lại"));
        }
    }
    
    public void doUpdateAccountPassword() {
        if (!newPass.equals(rePass)) {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mật khẩu không trùng nhau", "Hãy kiểm tra và nhập lại mật khẩu mới"));
            return;
        }
        ManagerDTO m = Methods.loginManager(user, pass);
        if (m==null) {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sai mật khẩu", "Hãy kiểm tra và nhập lại mật khẩu cũ"));
        } else {
            m.setPassword(newPass);
            Methods.updateManagerPassword(m);
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Thành công", "Đổi mật khẩu thành công"));
        }
    }
}
