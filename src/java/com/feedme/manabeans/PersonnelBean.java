/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.global.GlobalBean;
import com.feedme.service.Employee;
import com.feedme.service.EmployeeDTO;
import com.feedme.service.Manager;
import com.feedme.service.ManagerDTO;
import com.feedme.service.Priviledge;
import com.feedme.service.PriviledgeDTO;
import com.feedme.ws.Methods;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * For personnel management sections
 *
 * @author Giang
 */
@ManagedBean
@ViewScoped
public class PersonnelBean implements Serializable {

    private Employee employee, newEmployee;
    private Manager manager, newManager;
    private List<Employee> employeeList;
    private List<Manager> managerList;
    private List<Priviledge> priv;
    private short selectedManagerId = 1, selectedPrivId = 1;
    private String newPassword;
    private boolean enabled;

    public PersonnelBean() {
        managerList = new ArrayList<>();
        employeeList = new ArrayList<>();
        
        refreshNewEmployee();
        refreshNewManager();
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getNewEmployee() {
        return newEmployee;
    }

    public void setNewEmployee(Employee newEmployee) {
        this.newEmployee = newEmployee;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Manager getNewManager() {
        return newManager;
    }

    public void setNewManager(Manager newManager) {
        this.newManager = newManager;
    }

    public List<Employee> getEmployeeList() {
        if (employeeList.isEmpty()) {
            employeeList = Methods.fetchEmployees().stream().map(EmployeeDTO::getEmployee).collect(Collectors.toList());
        }
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public List<Manager> getManagerList() {
        if (managerList.isEmpty()) {
            managerList = Methods.fetchManagers().stream().map(ManagerDTO::getManager).collect(Collectors.toList());
        }
        return managerList;
    }

    public void setManagerList(List<Manager> managerList) {
        this.managerList = managerList;
    }

    public List<Priviledge> getPriv() {
        if (priv==null) {
            priv = Methods.fetchPriviledges().stream().map(PriviledgeDTO::getPriviledge).collect(Collectors.toList());
        }
        return priv;
    }

    public void setPriv(List<Priviledge> priviledgeList) {
        this.priv = priviledgeList;
    }
    
    public void doLoadEmployee(short id) {
        employee = Methods.fetchEmployeeById(id).getEmployee();
        employee.setInfo(updateEmployeeInfo(employee.getInfo()));
        selectedManagerId = employee.getManager().getId();
        enabled = employee.isIsEnabled();
    }

    public void doAddEmployee() {
        EmployeeDTO eDto = new EmployeeDTO();
        eDto.setEmployee(newEmployee);
        ManagerDTO mDto = Methods.fetchManagerById(selectedManagerId);
        eDto.setManager(mDto);
        eDto.setIsEnabled(enabled);
        boolean result = Methods.addEmployee(eDto);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (result) {
            ctx.addMessage("", new FacesMessage("Thêm nhân viên", "Thêm nhân viên thành công"));
        } else {
            ctx.addMessage("", new FacesMessage("Thêm nhân viên", "Thêm nhân viên thất bại"));
        }
        refreshNewEmployee();
    }

    public void doUpdateEmployee() {
        EmployeeDTO eDto = new EmployeeDTO();
        eDto.setEmployee(employee);
        ManagerDTO mDto = Methods.fetchManagerById(selectedManagerId);
        eDto.setManager(mDto);
        eDto.setIsEnabled(enabled);
        boolean result = Methods.updateEmployee(eDto);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (result) {
            ctx.addMessage("", new FacesMessage("Cập nhật nhân viên", "Sửa thông tin nhân viên thành công"));
        } else {
            ctx.addMessage("", new FacesMessage("Cập nhật nhân viên", "Sửa thông tin nhân viên thất bại"));
        }
        employeeList = new ArrayList<>();
    }

    public void doRemoveEmployee() {
        boolean result = Methods.removeEmployee(employee.getId());
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (result) {
            ctx.addMessage("", new FacesMessage("Xóa nhân viên", "Xóa nhân viên thành công"));
        } else {
            ctx.addMessage("", new FacesMessage("Xóa nhân viên", "Xóa nhân viên thất bại"));
        }
        employeeList = new ArrayList<>();
    }

    public void doLoadManager(short id) {
        manager = Methods.fetchManagerById(id).getManager();
        manager.setInfo(updateManagerInfo(manager.getInfo()));
        selectedPrivId = manager.getPriviledge().getId();
    }

    public void doAddManager() {
        ManagerDTO dto = new ManagerDTO();
        dto.setManager(newManager);
        dto.setPassword(newPassword);
        
        PriviledgeDTO p = new PriviledgeDTO();
        p.setId(selectedPrivId);
        dto.setPriviledge(p);
        boolean result = Methods.addManager(dto);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (result) {
            ctx.addMessage("", new FacesMessage("Thêm quản lý", "Thêm quản lý thành công"));
        } else {
            ctx.addMessage("", new FacesMessage("Thêm quản lý", "Thêm quản lý thất bại"));
        }
        refreshNewManager();
    }

    public void doUpdateManager() {
        ManagerDTO dto = new ManagerDTO();
        dto.setManager(manager);
        if (newPassword.length()>0) {
            dto.setPassword(newPassword);
            Methods.updateManagerPassword(dto);
        }
        
        PriviledgeDTO p = new PriviledgeDTO();
        p.setId(selectedPrivId);
        dto.setPriviledge(p);
        boolean result = Methods.updateManager(dto);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (result) {
            ctx.addMessage("", new FacesMessage("Sửa quản lý", "Sửa thông tin quản lý thành công"));
        } else {
            ctx.addMessage("", new FacesMessage("Sửa quản lý", "SỬa thông tin quản lý thất bại"));
        }
        managerList = new ArrayList<>();
    }

    public void doRemoveManager() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!Methods.fetchEmployeesByManager(manager.getId()).isEmpty()) {
            ctx.addMessage("", new FacesMessage("Xóa quản lý", "Quản lý này đang quản lý các nhân viên khác. Vui lòng điều chuyển các nhân viên sang quản lý khác trước khi xóa quản lý này."));
            return;
        }

        boolean result = Methods.removeManager(manager.getId());
        if (result) {
            ctx.addMessage("", new FacesMessage("Xóa quản lý", "Xóa quản lý thành công"));
        } else {
            ctx.addMessage("", new FacesMessage("Xóa quản lý", "Xóa quản lý thất bại"));
        }
        managerList = new ArrayList<>();
    }

    private void refreshNewEmployee() {
        newEmployee = new Employee();
        newEmployee.setInfo(getEmployeeInfo());
        newEmployee.setIsEnabled(true);
        selectedManagerId = 1;
    }

    private void refreshNewManager() {
        newManager = new Manager();
        newManager.setInfo(getManagerInfo());
        selectedPrivId = 1;
    }

    private String getEmployeeInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1' cellspacing='0' width='100%'>");
        sb.append("</table>");
        return updateEmployeeInfo(sb.toString());
    }

    private String updateEmployeeInfo(String info) {
        StringBuilder sb = new StringBuilder(info);
        if (!(sb.toString().contains("<table border='1' cellspacing='0' width='100%'>") && sb.toString().contains("</table>"))) {
            sb = new StringBuilder();
            sb.append("<table border='1' cellspacing='0' width='100%'>");
            sb.append("</table>");
        }
        int offset = sb.length() - "</table>".length();
        try {
            List<String> props = Arrays.asList(GlobalBean.getPropertyValue("info_employee").split(";"));
            Collections.reverse(props);
            for (String p : props) {
                if (!sb.toString().contains("<th width='25%'>" + p + "</th>")) {
                    sb.insert(offset, "<tr><th width='25%'>"+p+"</th><td>&nbsp;</td></tr>");
                }
            }
        } catch (NullPointerException ex) {
        }
        return sb.toString();
    }

    private String getManagerInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1' cellspacing='0' width='100%'>");
        sb.append("</table>");
        return updateManagerInfo(sb.toString());
    }

    private String updateManagerInfo(String info) {
        StringBuilder sb = new StringBuilder(info);
        if (!(sb.toString().contains("<table border='1' cellspacing='0' width='100%'>") && sb.toString().contains("</table>"))) {
            sb = new StringBuilder();
            sb.append("<table border='1' cellspacing='0' width='100%'>");
            sb.append("</table>");
        }
        int offset = sb.length() - "</table>".length();
        try {
            List<String> props = Arrays.asList(GlobalBean.getPropertyValue("info_manager").split(";"));
            Collections.reverse(props);
            for (String p : props) {
                if (!sb.toString().contains("<th width='25%'>" + p + "</th>")) {
                    sb.insert(offset, "<tr><th width='25%'>"+p+"</th><td>&nbsp;</td></tr>");
                }
            }
        } catch (NullPointerException ex) {
        }
        return sb.toString();
    }

    public short getSelectedManagerId() {
        return selectedManagerId;
    }

    public void setSelectedManagerId(short selectedManagerId) {
        this.selectedManagerId = selectedManagerId;
    }

    public short getSelectedPrivId() {
        return selectedPrivId;
    }

    public void setSelectedPrivId(short selectedPrivId) {
        this.selectedPrivId = selectedPrivId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
