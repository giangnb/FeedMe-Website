/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.userbeans;

import com.feedme.global.GlobalBean;
import com.feedme.service.OrderDetailDTO;
import com.feedme.service.ProductDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

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

    /**
     * Creates a new instance of CartBean
     */
    public CartBean() {
        order = new OrderDetailDTO();
        cart = new Cart();
    }

    /**
     * Get products in cart
     *
     * @return
     */
    public List<ProductDTO> doGetCart() {
        return cart.getProducts();
    }

    /**
     * Get subtotal of shopping cart (un-formatted)
     *
     * @return
     */
    public double doGetSubtotal() {
        return cart.subTotal;
    }
    
    public double doGetTotal() {
        return cart.total;
    }
    
    public double doGetDiscount() {
        return cart.discount;
    }

    /**
     * Add a product to cart (if exists, increase the quantity)
     *
     * @param product
     * @return Back to home page
     */
    public void doAddToCart(ProductDTO product) {
        cart.put(product);
    }

    /**
     * Decrease the selected product's quantity (if quantity already is 1,
     * remove it)
     *
     * @param product
     * @return
     */
    public void doRemoveFromCart(ProductDTO product) {
        cart.pop(product);
    }

    /**
     * Completely remove product from cart
     *
     * @param product
     * @return
     */
    public void doDeleteFromCart(ProductDTO product) {
        cart.removeProduct(product);
    }

    /**
     * Get product entity
     *
     * @param product
     * @return
     */
    public int doGetCount(ProductDTO product) {
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

    public double doGetPrice(ProductDTO product) {
        return product.getPrice() * cart.get(product);
    }

    /**
     * Class stores Products and its quantity
     */
    public class Cart extends HashMap<ProductDTO, Integer> implements Serializable {

        public double subTotal = 0, total = 0, discount = 0;

        public void put(ProductDTO product) {
            Integer value = get(product);
            if (value == null) {
                put(product, 1);
            } else {
                put(product, value + 1);
            }
            subTotal += product.getPrice();
            total += product.getPrice();
            double dis = GlobalBean.processDiscount(product.getPrice(), product.getPromotion());
            discount += dis;
            total -= dis;
        }

        public void pop(ProductDTO product) {
            Integer value = get(product);
            if (value == null) {
                return;
            }
            if (value > 1) {
                put(product, value - 1);
            } else {
                remove(product);
            }
            subTotal -= product.getPrice();
            total -= product.getPrice();
            double dis = GlobalBean.processDiscount(product.getPrice(), product.getPromotion());
            discount -= dis;
            total += dis;
        }
        
        public void removeProduct(ProductDTO product) {
            for (int i = 0; i < get(product); i++) {
                pop(product);
            }
        }

        public void importProductsList(List<ProductDTO> list) {
            for (ProductDTO p : list) {
                put(p);
            }
        }

        public List<ProductDTO> exportProductsList() {
            ArrayList<ProductDTO> list = new ArrayList<>();
            for (ProductDTO p : keySet()) {
                for (int i = 0; i < get(p); i++) {
                    list.add(p);
                }
            }
            return list;
        }

        public List<ProductDTO> getProducts() {
            return new ArrayList<>(keySet());
        }
    }
}
