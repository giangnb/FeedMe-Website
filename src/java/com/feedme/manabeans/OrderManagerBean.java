/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.global.GlobalBean;
import com.feedme.info.Information;
import com.feedme.info.SingleInformation;
import com.feedme.service.EmployeeDTO;
import com.feedme.service.OrderDetail;
import com.feedme.service.OrderStatus;
import com.feedme.service.Product;
import com.feedme.service.ProductDTO;
import com.feedme.utils.Cart;
import com.feedme.utils.Json;
import com.feedme.ws.Methods;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
public class OrderManagerBean implements Serializable{

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
        fromTime = new Date(toTime.getTime() - 1000 * 60 * 60 * 18);
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public List<OrderDetail> doGetOrderDetailListByRating(int rate) {
        if (orderDetailList==null) {
            doGetOrderDetail();
        }
        return orderDetailList.stream().filter(o->{
            return o.getRating()==null?false:o.getRating()==rate;
        }).collect(Collectors.toList());
    }

    public void doGetOrderDetail() {
        if (fromTime == null || toTime == null) {
            return;
        }
        long fr, to;
        fr = fromTime.getTime();
        to = toTime.getTime();
        if (fr>=to) {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lỗi nhập liệu", "Ngày bắt đầu phải nhỏ hơn ngày kết thúc"));
        }
        orderDetailList = Methods.fetchOrders(fr + "", to + "");
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
        List<OrderDetail> list = Methods.fetchOrders(toTime.getTime() + "", fromTime.getTime() + "");
        if (list == null) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage("", new FacesMessage("Không tìm thấy hóa đơn từ " + toTime.toString() + " đến " + fromTime.toString()));
        }
        return list;
    }

    public List<OrderDetail> doLoadOrderByEmployee(EmployeeDTO employee) {
        List<OrderDetail> list = Methods.fetchOrdersByEmployee(toTime.getTime() + "", fromTime.getTime() + "", employee.getEmployee().getId());
        if (list == null) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage("", new FacesMessage("Không tìm thấy hóa đơn được xử lý bởi nhân viên " + employee.getEmployee().getUsername()));
        }
        return list;
    }

    public List<ProductDTO> doLoadFoodSales() {
        List<ProductDTO> listProduct = new ArrayList<>();
        List<OrderDetail> list = Methods.fetchOrders(toTime.getTime() + "", fromTime.getTime() + "");
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
        List<OrderDetail> list = Methods.fetchOrders(toTime.getTime() + "", fromTime.getTime() + "");
        list.stream().forEach((order) -> {
            comments.add(order.getComment());
        });
        if (list.isEmpty()) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage("", new FacesMessage("Không tìm thấy phản hồi từ" + fromTime.toString() + " đến " + toTime.toString()));

        }
        return comments;
    }

    public double doGetOrderPrice(OrderDetail od) {
        try {
            return new Cart(od.getFoods()).getTotal();
        } catch (Exception ex) {
            return 0;
        }
    }

    public double doGetOrderDiscount(OrderDetail od) {
        try {
            return new Cart(od.getFoods()).getDiscount();
        } catch (Exception ex) {
            return 0;
        }
    }

    public int doGetOrderQuantity(OrderDetail od) {
        try {
            return new Cart(od.getFoods()).getTotalQuantity();
        } catch (Exception ex) {
            return 0;
        }
    }

    public Date doGetOrderTime(OrderDetail od) {
        try {
            return new Date(Long.parseLong(od.getOrdertime()));
        } catch (Exception ex) {
            return new Date();
        }
    }

    public String doGetTrackingCode(String time) {
        try {
            Long t = Long.parseLong(time);
            return Long.toUnsignedString(t, Character.MAX_RADIX).toUpperCase();
        } catch (NumberFormatException ex) {
            return "NaN";
        }
    }

    public String doGetCustomerContext(OrderDetail od) {
        if (od == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<p>");
            Information i = Json.DeserializeObject(od.getCustomer(), Information.class);
            for (SingleInformation si : i) {
                sb.append(si.getKey()).append(":&nbsp;")
                        .append(si.getValue()).append("<br/>");
            }
            sb.append("</p>");
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public String doGetTraceContext(OrderDetail od) {
        if (od == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<ul>");
            Information i = Json.DeserializeObject(od.getNote(), Information.class);
            Collections.reverse(i);
            String format = GlobalBean.getPropertyValue("format_date") + " " + GlobalBean.getPropertyValue("format_time");
            SimpleDateFormat fmt = new SimpleDateFormat(format);
            long time;
            for (SingleInformation si : i) {
                time = Long.parseLong(si.getKey());
                sb.append("<li><strong>")
                        .append(fmt.format(new java.util.Date(time)))
                        .append("</strong>&nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp;")
                        .append(si.getValue()).append("</li>");
            }
            sb.append("</ul>");
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public String doGetProductsContext(OrderDetail od) {
        try {
            Cart c = new Cart(od.getFoods());
            StringBuilder sb = new StringBuilder();
            sb.append("<table class='table table-bordered table-hover'>")
                    .append("<tr><th>Sản phẩm</th><th>Đơn giá</th><th>Số lượng</th><th>Giảm giá</th></tr>");
            for (Product p : c.getProducts()) {
                sb.append("<tr>")
                        .append("<td>").append(p.getName()).append("</td>")
                        .append("<td>").append(p.getPrice()).append("</td>")
                        .append("<td>").append(c.getQuantityOfProduct(p)).append("</td>")
                        .append("<td>-").append(p.getPromotion()).append("</td>")
                        .append("</tr>");
            }
            return sb.append("</table>").toString();
        } catch (Exception ex) {
            return "";
        }
    }
    
    public int doGetRatingsCount() {
        return orderDetailList.stream()
                .filter(o->{return o.getRating()!=null?o.getRating()>0:false;})
                .collect(Collectors.toList()).size();
    }
    
    private int ratingPercentage;

    public int getRatingPercentage() {
        double result = 0d;
        int count = 0;
        for (OrderDetail od : orderDetailList) {
            if (od.getRating()!=null && od.getRating()>0) {
                count++;
                result += od.getRating();
            }
        }
        if (count<=0) {
            return 0;
        }
        return (int)(result / count) *20;
    }

    public void setRatingPercentage(int ratingPercentage) {
        this.ratingPercentage = ratingPercentage;
    }
    
}
