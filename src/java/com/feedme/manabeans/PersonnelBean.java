/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.global.GlobalBean;
import com.feedme.service.EmployeeDTO;
import com.feedme.service.Manager;
import com.feedme.service.ManagerDTO;
import com.feedme.ws.Methods;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
public class PersonnelBean {

    private EmployeeDTO employee, newEmployee;
    private ManagerDTO manager, newManager;

    private List<EmployeeDTO> employeeList;
    private List<ManagerDTO> managerList;
    private Short selectedManagerId;

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
        employee.setInfo(updateEmployeeInfo(employee.getInfo()));
        this.employee = employee;
        selectedManagerId = employee.getManager().getManager().getId();
    }

    public ManagerDTO getManager() {
        return manager;
    }

    public void setManager(ManagerDTO manager) {
        manager.setInfo(updateManagerInfo(manager.getInfo()));
        this.manager = manager;
    }

    public EmployeeDTO getNewEmployee() {
        newEmployee = new EmployeeDTO();
        newEmployee.setInfo(getEmployeeInfo());
        selectedManagerId = 1;
        return newEmployee;
    }

    public void setNewEmployee(EmployeeDTO newEmployee) {
        this.newEmployee = newEmployee;
    }

    public void setNewManager(ManagerDTO newManager) {
        this.newManager = newManager;
    }

    public ManagerDTO getNewManager() {
        newManager = new ManagerDTO();
        newManager.setInfo(getManagerInfo());
        return newManager;
    }

    public short getSelectedManagerId() {
        return selectedManagerId;
    }

    public void setSelectedManagerId(short selectedManagerId) {
        this.selectedManagerId = selectedManagerId;
    }

    public List<EmployeeDTO> getEmployeeList() {
        if (employeeList == null || employeeList.isEmpty()) {
            employeeList = Methods.fetchEmployees();
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (employeeList.isEmpty()) {
                ctx.addMessage("", new FacesMessage("Không có nhân viên."));
                return null;
            }
        }
        return employeeList;
    }

    public void setEmployeeList(List<EmployeeDTO> employeeList) {
        this.employeeList = employeeList;
    }

    public List<ManagerDTO> getManagerList() {
        if (managerList == null || managerList.isEmpty()) {
            managerList = Methods.fetchManagers();
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (managerList.isEmpty()) {
                ctx.addMessage("", new FacesMessage("Không có quản lý."));
                return null;
            }
        }
        return managerList;
    }

    public void setManagerList(List<ManagerDTO> managerList) {
        this.managerList = managerList;
    }

    public String doAddEmployee() {
        Manager m = new Manager();
        m.setId(selectedManagerId);
        ManagerDTO dto = new ManagerDTO();
        dto.setManager(m);
        newEmployee.setManager(dto);
        boolean result = Methods.addEmployee(newEmployee);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!result) {
            ctx.addMessage("", new FacesMessage("Thêm mới nhân viên thất bại."));
        } else {
            ctx.addMessage("", new FacesMessage("Thêm mới nhân viên thành công."));
        }
        return "success";
    }

    public String doEditEmployee() {
        Manager m = new Manager();
        m.setId(selectedManagerId);
        ManagerDTO dto = new ManagerDTO();
        dto.setManager(m);
        employee.setManager(dto);
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
    
    private String getEmployeeInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1' cellpacing='0' width='100%'>");
        sb.append("</table>");
        return updateEmployeeInfo(sb.toString());
    }

    private String updateEmployeeInfo(String info) {
        StringBuilder sb = new StringBuilder(info);
        if (!(sb.toString().contains("<table border='1' cellpacing='0' width='100%'>") && sb.toString().contains("</table>"))) {
            sb = new StringBuilder();
            sb.append("<table border='1' width='100%' cellpacing='0' cellspacing='0'>");
            sb.append("</table>");
        }
        int offset = sb.length() - "</table>".length();
        List<String> props = Arrays.asList(GlobalBean.getPropertyValue("info_employee").split(";"));
        Collections.reverse(props);
        for (String p : props) {
            if (!sb.toString().contains("<th width='25%%'>" + p + "</th>")) {
                sb.insert(offset, String.format("<tr><th width='25%%'>%s</th><td>&nbsp;</td></tr>", p));
            }
        }
        return sb.toString();
    }
    
    private String getManagerInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1' width='100%'>");
        sb.append("</table>");
        return updateManagerInfo(sb.toString());
    }

    private String updateManagerInfo(String info) {
        StringBuilder sb = new StringBuilder(info);
        if (!(sb.toString().contains("<table border='1' width='100%'>") && sb.toString().contains("</table>"))) {
            sb = new StringBuilder();
            sb.append("<table border='1' width='100%' cellspacing='0'>");
            sb.append("</table>");
        }
        int offset = sb.length() - "</table>".length();
        List<String> props = Arrays.asList(GlobalBean.getPropertyValue("info_manager").split(";"));
        Collections.reverse(props);
        for (String p : props) {
            if (!sb.toString().contains("<th width='25%%'>" + p + "</th>")) {
                sb.insert(offset, String.format("<tr><th width='25%%'>%s</th><td>&nbsp;</td></tr>", p));
            }
        }
        return sb.toString();
    }
}
