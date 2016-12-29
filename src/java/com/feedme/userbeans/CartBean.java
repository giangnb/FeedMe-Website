/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.userbeans;

import com.feedme.global.GlobalBean;
import com.feedme.info.Information;
import com.feedme.service.Employee;
import com.feedme.service.OrderDetail;
import com.feedme.service.OrderDetailDTO;
import com.feedme.service.OrderStatus;
import com.feedme.service.Product;
import com.feedme.utils.Cart;
import com.feedme.utils.Json;
import com.feedme.ws.Methods;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 * Managed bean for processing Cart section
 *
 * @author Giang
 */
@ManagedBean
@SessionScoped
public class CartBean implements Serializable {

    private OrderDetailDTO order;
    private Cart cart;
    private String name, phone, address, email;
    private boolean accept;
    private long orderDate;

    /**
     * Creates a new instance of CartBean
     */
    public CartBean() {
        order = new OrderDetailDTO();
        cart = new Cart();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public long getOrderDate() {
        if (orderDate<0) {
            orderDate = 0;
            return -1;
        }
        return orderDate;
    }

    public void setOrderDate(long orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * Get products in cart
     *
     * @return
     */
    public List<Product> doGetCart() {
        return Arrays.asList(cart.getProducts());
    }

    /**
     * Get subtotal of shopping cart (un-formatted)
     *
     * @return
     */
    public double doGetSubtotal() {
        return cart.getSubTotal();
    }

    public double doGetTotal() {
        return cart.getTotal();
    }

    public double doGetDiscount() {
        return cart.getDiscount();
    }

    /**
     * Add a product to cart (if exists, increase the quantity)
     *
     * @param product
     */
    public void doAddToCart(Product product) {
        orderDate = 0;
        cart.put(product);
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Cập nhật giỏ hàng", "Đã thêm " + product.getName()));
    }

    /**
     * Decrease the selected product's quantity (if quantity already is 1,
     * remove it)
     *
     * @param product
     */
    public void doRemoveFromCart(Product product) {
        cart.pop(product);
        if (cart.get(product) != null) {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Cập nhật giỏ hàng", "Đã bỏ 1 " + product.getName()));
        } else {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Cập nhật giỏ hàng", "Đã xoá " + product.getName()));
        }
    }

    /**
     * Completely remove product from cart
     *
     * @param product
     */
    public void doDeleteFromCart(Product product) {
        cart.removeProduct(product);
    }

    /**
     * Get product entity
     *
     * @param product
     * @return
     */
    public int doGetCount(Product product) {
        return cart.get(product);
    }

    public OrderDetailDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDetailDTO order) {
        this.order = order;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public double doGetPrice(Product product) {
        return product.getPrice() * cart.get(product).doubleValue();
    }

    public void doCreateOrder() {
        orderDate = -1;
        FacesContext ctx = FacesContext.getCurrentInstance();
        RequestContext req = RequestContext.getCurrentInstance();
        if (!GlobalBean.checkOrderable()) {
            ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Không thể đặt hàng", "Chúng tôi đã dừng tiếp nhận đơn hàng, vui lòng quay lại sau. Rất mong Quý khách thông cảm!"));
        }
        if (!validateData()) {
            ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Tạo đơn hàng", "Vui lòng nhập đầy đủ các thông tin cần thiết"));
        } else {
            orderDate = new Date().getTime();
            order = new OrderDetailDTO();
            try {
                OrderDetail od = new OrderDetail();
                od.setComment("");
                od.setCustomer(getCustomerJson());
                od.setDiscount(doGetDiscount());
                od.setEmployee(getDefaultEmployee());
                od.setFoods(cart.exportToJson());
                od.setNote(getInitNote());
                od.setOrdertime(orderDate + "");
                od.setRating(Double.parseDouble("0"));
                od.setStatus(getDefaultStatus());
                od.setSubtotal(doGetSubtotal());
                order.setOrderDetail(od);
                boolean result = Methods.addOrder(order);
                if (result) {
                    ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Tạo đơn hàng thành công", "Cảm ơn bạn đã mua hàng, chúng tôi sẽ sơm liên hệ tới bạn!"));
                    resetCart();
                    req.execute("PF('dlgOrderSuccess').show();");
                    req.openDialog("dlgOrderSuccess");
                } else {
                    ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tạo đơn hàng thất bại", "Không thể tạo đơn hàng. Hãy kiểm tra kết nốt Internet và thử lại sau ít phút."));
                }
            } catch (Exception ex) {
                ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Lỗi xảy ra khi tạo đơn hàng", "Không thể tạo đơn hàng do một lỗi xảy ra. Hãy liên hệ chúng tôi để được hỗ trợ. Rất mong Quý khách thông cảm!"));
            }
        }
    }
    
    public String doGetOrderTrackingCode() {
        if (orderDate<=0) {
            return null;
        }
        return Long.toString(orderDate, Character.MAX_RADIX).toUpperCase();
    }

    private String getCustomerJson() throws Exception {
        Information info = new Information();
        info.put("name", name);
        info.put("tel", phone);
        info.put("email", email);
        info.put("address", address);
        return Json.SerializeObject(info);
    }

    private Employee getDefaultEmployee() {
        Employee e = new Employee();
        e.setId(Short.parseShort("1"));
        return e;
    }

    private OrderStatus getDefaultStatus() {
        OrderStatus o = new OrderStatus();
        o.setId(Short.parseShort("1"));
        return o;
    }

    private String getInitNote() throws Exception {
        Information note = new Information();
        note.put(orderDate + "", "Tạo đơn hàng thành công");
        return Json.SerializeObject(note);
    }

    private void resetCart() {
        cart = new Cart();
        order = new OrderDetailDTO();
        accept = false;
    }

    private boolean validateData() {
        if (name.length() <= 0 || name.length() > 64) {
            return false;
        }
        if (!phone.matches("([(]?[\\+]?[0-9]+[)]?)?[0-9]{4,}")) {
            return false;
        }
        if (email.length() > 0 && !email.matches("[a-z0-9-_.]+[@]([a-z0-9-]+[.])+[a-z]+")) {
            return false;
        }
        if (address.length() < 10) {
            return false;
        }
        return accept;
    }
}
