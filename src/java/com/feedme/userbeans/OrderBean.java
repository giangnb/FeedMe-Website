/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.userbeans;

import com.feedme.global.GlobalBean;
import com.feedme.info.Information;
import com.feedme.info.SingleInformation;
import com.feedme.service.OrderDetail;
import com.feedme.service.OrderDetailDTO;
import com.feedme.service.Product;
import com.feedme.utils.Cart;
import com.feedme.utils.Json;
import com.feedme.ws.Methods;
import com.sun.faces.util.Util;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * Managed bean for processing the Order tracking function
 *
 * @author Giang
 */
@ManagedBean
@ViewScoped
public class OrderBean implements Serializable {

    private OrderDetail order;
    private String trackingCode;
    private long timeStamp = 0;
    private String phoneNumber, comment = "";
    private int rating;
    private Cart cart;

    /**
     * Creates a new instance of OrderBean
     */
    public OrderBean() {
    }

    public OrderDetail getOrder() {
        return order;
    }

    public void setOrder(OrderDetail order) {
        this.order = order;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode.toUpperCase();
        try {
            timeStamp = Long.parseLong(trackingCode, Character.MAX_RADIX);
        } catch (NumberFormatException ex) {
        }
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment.replace("\n", "<br/>");
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void doGetOrder() {
        order = null;
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!phoneNumber.matches("([(]?[\\+]?[0-9]+[)]?)?[\\s]?[0-9]{4,}")) {
            ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Tra cứu đơn hàng thất bại", "Số điện thoại không hợp lệ"));
            return;
        }
        if (timeStamp <= 0) {
            ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tra cứu đơn hàng thất bại", "Mã đơn hàng không hợp lệ"));
            return;
        }
        List<OrderDetail> list = Methods.fetchOrders((timeStamp - 1) + "", (timeStamp + 1) + "").stream()
                .filter(o -> {
                    return o.getCustomer().contains("\"key\":\"tel\",\"value\":\"" + phoneNumber + "\"");
                }).collect(Collectors.toList());
        if (list.isEmpty()) {
            ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Tra cứu đơn hàng thất bại", "Không tìm thấy đơn hàng, vui lòng kiểm tra Mã đơn hàng và Số điện thoại"));
        } else {
            order = list.get(0);
            try {
                cart = new Cart(order.getFoods());
            } catch (Exception ex) {
                ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Tra cứu đơn hàng", "Không thể tải các sản phẩm trong đơn hàng"));
            }
        }
    }

    public boolean doCheckRatingAvailable() {
        if (GlobalBean.getPropertyValue("system_rating").equals("0")) {
            return false;
        }
        if (order.getRating() > 0) {
            return false;
        }
        return Methods.fetchOrderStatusById(order.getStatus().getId()).isIsStopped();
    }

//    public void doRatingOrder() {
//        FacesContext ctx = FacesContext.getCurrentInstance();
//        if (!doCheckRatingAvailable()) {
//            ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Đánh giá chất lượng dịch vụ", "Đơn hàng của Quý khách không thể gửi đánh giá"));
//        }
//        try {
//            order.setRating(Double.parseDouble(rating + ""));
//            comment = comment == null ? "Không có nhận xét" : comment;
//            order.setComment(comment);
//            OrderDetailDTO dto = new OrderDetailDTO();
//            dto.setOrderDetail(order);
//            boolean result = Methods.updateOrder(dto);
//            if (result) {
//                ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Đánh giá chất lượng dịch vụ", "Cảm ơn Quý khách đã đánh giá!"));
//            } else {
//                ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Đánh giá chất lượng dịch vụ", "Hãy kiểm tra kết nối Internet hoặc thử lại sau ít phút"));
//            }
//        } catch (NumberFormatException | NullPointerException ex) {
//            ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Đánh giá chất lượng dịch vụ", "Vui lòng kiểm tra dữ liệu đã nhập"));
//        }
//    }

    public String doGetCustomerContext() {
        if (order == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<div class='row'>");
            Information i = Json.DeserializeObject(order.getCustomer(), Information.class);
            for (SingleInformation si : i) {
                sb.append(" - ")
                        .append(si.getValue()).append("<br/>");
            }
            sb.append("</div>");
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public String doGetTraceContext() {
        if (order == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<ul>");
            Information i = Json.DeserializeObject(order.getNote(), Information.class);
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

    @Deprecated
    public Cart doGetOrderProducts() {
        try {
            System.out.println(order.getFoods());
            Product[] prods = Json.DeserializeObject(order.getFoods(), Product[].class);
            Cart cart = new Cart();
            for (Product p : prods) {
                //System.out.println(p +" - "+p.getName());
                cart.put(p);
            }
            return cart;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String doGetOrderTime() {
        try {
            String format = GlobalBean.getPropertyValue("format_date") + " " + GlobalBean.getPropertyValue("format_time");
            SimpleDateFormat fmt = new SimpleDateFormat(format);
            return fmt.format(new Date(Long.parseLong(order.getOrdertime())));
        } catch (NumberFormatException ex) {
            return "NaN";
        }
    }
    
    public void doSubmitRating() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        try {
            Information note = Json.DeserializeObject(order.getNote(), Information.class);
            note.put(new Date().getTime()+"", "Khách hàng đã gửi nhận xét ("+rating+" sao)");
            order.setNote(note.toJson());
            order.setComment(comment);
            order.setRating(Double.parseDouble(rating+""));
            OrderDetailDTO dto = new OrderDetailDTO();
            dto.setOrderDetail(order);
            boolean result = Methods.updateOrder(dto);
            if (result) {
                ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Cảm ơn Quý khách đã đánh giá!", ""));
            } else {
                ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Không thành công", "Vui lòng thử gửi lại phản hồi trong ít phút nữa"));
            }
        } catch (Exception ex) {
            ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Không thành công", "Vui lòng thử gửi lại phản hồi trong ít phút nữa"));
        }
    }
}
