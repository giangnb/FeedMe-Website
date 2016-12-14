/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.service.EmployeeDTO;
import com.feedme.service.OrderDetailDTO;
import com.feedme.service.ProductDTO;
import com.feedme.ws.Methods;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 * For orders and customer's feedback section
 * @author Giang
 */
@ManagedBean(name = "orderManaBean")
@RequestScoped
public class OrderManagerBean {
  
  private Date fromTime;
  private Date toTime;
  
    /**
     * Creates a new instance of OrderManagerBean
     */
    public OrderManagerBean() {
    }

    public Date getFromTime() {
        return fromTime;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    public Date getToTime() {
        return toTime;
    }
    public void setToTime(Date toTime) {
        this.toTime = toTime;
    }
    
  
    public List<OrderDetailDTO> doLoadOrderByTime() {
       List<OrderDetailDTO> list = Methods.fetchOrders(toTime.getTime(), fromTime.getTime());
       if(list==null) {
           FacesContext ctx = FacesContext.getCurrentInstance();
           ctx.addMessage("", new FacesMessage("Không tìm thấy hóa đơn từ " + toTime.toString()+ " đến " + fromTime.toString()));
       }
       return list;
    }
    
    public List<OrderDetailDTO> doLoadOrderByEmployee (EmployeeDTO employee) {
        List<OrderDetailDTO> list = Methods.fetchOrdersByEmployee(toTime.getTime(), fromTime.getTime(), employee.getEmployee().getId());
        if(list==null) {
           FacesContext ctx = FacesContext.getCurrentInstance();
           ctx.addMessage("", new FacesMessage("Không tìm thấy hóa đơn được xử lý bởi nhân viên " + employee.getEmployee().getUsername()));
       }
        return list;
    }
    
    
    public List<ProductDTO> doLoadFoodSales () {
      List<ProductDTO> listProduct = new ArrayList<>();
      List<OrderDetailDTO> list = Methods.fetchOrders(toTime.getTime(), fromTime.getTime());
       for (OrderDetailDTO order:list) {
          listProduct = order.getFoods();
       }
       if (listProduct.isEmpty()) {
          FacesContext ctx = FacesContext.getCurrentInstance();
           ctx.addMessage("", new FacesMessage("Không tìm thấy doanh số sản phẩm từ" + fromTime.toString() + " đến " + toTime.toString()));
                   
       }
      return listProduct;
    }
    
    public List<String> doLoadComments() {
       List<String> comments = new ArrayList<>();
       List<OrderDetailDTO> list = Methods.fetchOrders(toTime.getTime(), fromTime.getTime());
       list.stream().forEach((order) -> {
           comments.add(order.getComment());
      });
       if (list.isEmpty()) {
          FacesContext ctx = FacesContext.getCurrentInstance();
           ctx.addMessage("", new FacesMessage("Không tìm thấy phản hồi từ" + fromTime.toString() + " đến " + toTime.toString()));
                   
       }
       return comments;
    }
}
