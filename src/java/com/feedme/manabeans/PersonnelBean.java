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
 * @author Giang
 */
@ManagedBean
@RequestScoped
public class PersonnelBean {
 private EmployeeDTO employee;
 private ManagerDTO manager;
    /**
     * Creates a new instance of PersonnelBean
     *  Note: Add new bean: loadOrderByEmployee
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
    
    
    public List<EmployeeDTO> doLoadEmployees () {
        List<EmployeeDTO> list = Methods.fetchEmployees();
        if(list==null) {return null;}
        return list;
    }
    
    public EmployeeDTO doGetEmployee () {
        
       return employee;
    }
    
    public String doAddEmployee() {
      boolean result = Methods.addEmployee(employee);
      if (!result) {
          FacesContext ctx = FacesContext.getCurrentInstance();
          ctx.addMessage("", new FacesMessage("Khong them duoc nhan vien moi."));
      }
      return "success";
    }
    
    public String doEditEmployee()  {
        boolean result = Methods.updateEmployee(employee);
        if (!result){
          FacesContext ctx = FacesContext.getCurrentInstance();
          ctx.addMessage("", new FacesMessage("Khong sua duoc nhan vien."));
        }
        return "success";
    }
    
    public String doRemoveEmployee() {
        boolean result = Methods.removeEmployee(employee.getEmployee().getId());
        if (!result){
          FacesContext ctx = FacesContext.getCurrentInstance();
          ctx.addMessage("", new FacesMessage("Khong xoa duoc nhan vien."));
        }
        return "success";
    }
    
    public List<ManagerDTO> doLoadManagers() {
       List<ManagerDTO> list = Methods.fetchManagers();
        if(list==null) {return null;}
        return list;
    }

    public String doEditManager() {
       boolean result = Methods.updateManager(manager);
        if (!result){
          FacesContext ctx = FacesContext.getCurrentInstance();
          ctx.addMessage("", new FacesMessage("Khong sua duoc quan ly."));
        }
        return "success";
    }
}
