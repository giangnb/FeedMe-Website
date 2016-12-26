/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.service.EmployeeDTO;
import com.feedme.service.OrderDetail;
import com.feedme.service.OrderStatus;
import com.feedme.service.ProductDTO;
import com.feedme.ws.Methods;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * For orders and customer's feedback section
 *
 * @author Giang
 */
@ManagedBean(name = "orderManaBean")
@ViewScoped
public class OrderManagerBean {

    private List<OrderDetail> orderDetailList;
    private List<OrderStatus> orderStatusList;

    private OrderDetail order;
    private OrderStatus status, newStatus;
    private boolean isFinished;

    private Date fromTime;
    private Date toTime;

    /**
     * Creates a new instance of OrderManagerBean
     */
    public OrderManagerBean() {
        toTime = new Date();
        fromTime = new Date(toTime.getTime() - 1000 * 60 * 12);
    }

    public List<OrderDetail> getOrderDetailList() {
        if (orderDetailList == null) {
            orderDetailList = Methods.fetchOrders(fromTime.getTime()+"", toTime.getTime()+"");
        }
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetail) {
        this.orderDetailList = orderDetail;
    }

    public List<OrderStatus> getOrderStatusList() {
        if (orderStatusList == null) {
            orderStatusList = Methods.fetchOrderStatus();
        }
        return orderStatusList;
    }

    public void setOrderStatusList(List<OrderStatus> orderStatus) {
        this.orderStatusList = orderStatus;
    }

    public OrderDetail getOrder() {
        return order;
    }

    public void setOrder(OrderDetail order) {
        this.order = order;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(OrderStatus newStatus) {
        this.newStatus = newStatus;
    }

    public boolean isIsFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
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

    public void doNewStatus() {
        newStatus = new OrderStatus();
        isFinished = false;
    }

    public void doLoadStatus(short id) {
        status = Methods.fetchOrderStatusById(id);
        isFinished = status.isIsStopped();
    }

    public void doAddStatus() {
        newStatus.setIsStopped(isFinished);
        boolean result = Methods.addOrderStatus(newStatus);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (result) {
            ctx.addMessage("", new FacesMessage("Thêm trạng thái thành công"));
            doNewStatus();
            orderStatusList = null;
        } else {
            ctx.addMessage("", new FacesMessage("Thêm trạng thái thất bại"));
        }
    }

    public void doUpdateStatus() {
        status.setIsStopped(isFinished);
        boolean isAvail = false;
        for (OrderStatus s : orderStatusList) {
            if ((!Objects.equals(s.getId(), status.getId())) && s.isIsStopped()) {
                isAvail = true;
                break;
            }
        }
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (isAvail) {
            boolean result = Methods.updateOrderStatus(status);
            if (result) {
                ctx.addMessage("", new FacesMessage("Cập nhật trạng thái thành công"));
                orderStatusList = null;
            } else {
                ctx.addMessage("", new FacesMessage("Cập nhật trạng thái thất bại"));
            }
        } else {
            ctx.addMessage("", new FacesMessage("Phải có ít nhất 1 trạng thái kết thúc"));
        }
    }

    public void doRemoveStatus() {
        boolean isAvail = false;
        for (OrderStatus s : orderStatusList) {
            if ((!Objects.equals(s.getId(), status.getId())) && s.isIsStopped()) {
                isAvail = true;
                break;
            }
        }
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (isAvail) {
            boolean result = Methods.removeOrderStatus(status.getId());
            if (result) {
                ctx.addMessage("", new FacesMessage("Xóa trạng thái thành công"));
                orderStatusList = null;
            } else {
                ctx.addMessage("", new FacesMessage("Xóa trạng thái thất bại"));
            }
        } else {
            ctx.addMessage("", new FacesMessage("Phải có ít nhất 1 trạng thái kết thúc"));
        }
    }

    public List<OrderDetail> doLoadOrderByTime() {
        List<OrderDetail> list = Methods.fetchOrders(toTime.getTime()+"", fromTime.getTime()+"");
        if (list == null) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage("", new FacesMessage("Không tìm thấy hóa đơn từ " + toTime.toString() + " đến " + fromTime.toString()));
        }
        return list;
    }

    public List<OrderDetail> doLoadOrderByEmployee(EmployeeDTO employee) {
        List<OrderDetail> list = Methods.fetchOrdersByEmployee(toTime.getTime()+"", fromTime.getTime()+"", employee.getEmployee().getId());
        if (list == null) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage("", new FacesMessage("Không tìm thấy hóa đơn được xử lý bởi nhân viên " + employee.getEmployee().getUsername()));
        }
        return list;
    }

    public List<ProductDTO> doLoadFoodSales() {
        List<ProductDTO> listProduct = new ArrayList<>();
        List<OrderDetail> list = Methods.fetchOrders(toTime.getTime()+"", fromTime.getTime()+"");
        for (OrderDetail order : list) {
            //listProduct = order.get;
        }
        if (listProduct.isEmpty()) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage("", new FacesMessage("Không tìm thấy doanh số sản phẩm từ" + fromTime.toString() + " đến " + toTime.toString()));

        }
        return listProduct;
    }

    public List<String> doLoadComments() {
        List<String> comments = new ArrayList<>();
        List<OrderDetail> list = Methods.fetchOrders(toTime.getTime()+"", fromTime.getTime()+"");
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
