/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.service.EmployeeDTO;
import com.feedme.service.ManagerDTO;
import com.feedme.ws.Methods;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 * For personnel management sections
 *
 * @author Giang
 */
@ManagedBean
@RequestScoped
public class PersonnelBean {

    private EmployeeDTO employee;
    private ManagerDTO manager;

    /**
     * Creates a new instance of PersonnelBean Note: Add new bean:
     * loadOrderByEmployee
     */
    public PersonnelBean() {
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    public ManagerDTO getManager() {
        return manager;
    }

    public void setManager(ManagerDTO manager) {
        this.manager = manager;
    }

    public List<EmployeeDTO> doLoadEmployees() {
        List<EmployeeDTO> list = Methods.fetchEmployees();
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (list.isEmpty()) {
            ctx.addMessage("", new FacesMessage("Không có nhân viên."));
            return null;
        }
        return list;
    }

    public EmployeeDTO doGetEmployee() {

        return employee;
    }

    public String doAddEmployee() {
        boolean result = Methods.addEmployee(employee);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!result) {
            ctx.addMessage("", new FacesMessage("Thêm mới nhân viên thất bại."));
        } else {
            ctx.addMessage("", new FacesMessage("Thêm mới nhân viên thành công."));
        }
        return "success";
    }

    public String doEditEmployee() {
        boolean result = Methods.updateEmployee(employee);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!result) {
            ctx.addMessage("", new FacesMessage("Không sửa được nhân viên."));
        } else {
            ctx.addMessage("", new FacesMessage("Sửa nhân viên thành công."));
        }
        return "success";
    }

    public String doRemoveEmployee() {
        boolean result = Methods.removeEmployee(employee.getEmployee().getId());
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!result) {
            ctx.addMessage("", new FacesMessage("Không xóa được nhân viên"));
        } else {
            ctx.addMessage("", new FacesMessage("Xóa nhân viên thành công"));
        }
        return "success";
    }

    public List<ManagerDTO> doLoadManagers() {
        List<ManagerDTO> list = Methods.fetchManagers();
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (list.isEmpty()) {
            ctx.addMessage("", new FacesMessage("Không có quản lý."));
            return null;
        }
        return list;
    }

    public String doEditManager() {
        boolean result = Methods.updateManager(manager);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!result) {
            ctx.addMessage("", new FacesMessage("Sửa quản lý thất bại."));
        } else {
            ctx.addMessage("", new FacesMessage("Sửa quản lý thành công."));
        }
        return "success";
    }
}
