/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.userbeans;

import com.feedme.service.OrderDetailDTO;
import com.feedme.service.ProductDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Managed bean for processing Cart section
 * @author Giang
 */
@ManagedBean
@SessionScoped
public class CartBean {
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
     * @return 
     */
    public List<ProductDTO> doGetCart() {
        return cart.getProducts();
    }
    
    /**
     * Get subtotal of shopping cart (un-formatted)
     * @return 
     */
    public double doGetSubtotal() {
        double total = 0d;
        for (ProductDTO p : order.getFoods()) {
            total += p.getPrice();
        }
        return total;
    }
    
    /**
     * Add a product to cart (if exists, increase the quantity)
     * @param product
     * @return Back to home page
     */
    public String doAddToCart(ProductDTO product) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Decrease the selected product's quantity (if quantity already is 1, remove it)
     * @param product
     * @return 
     */
    public String doRemoveFromCart(ProductDTO product) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Completely remove product from cart
     * @param product
     * @return 
     */
    public String doDeleteFromCart(ProductDTO product) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Get product entity
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
    
    /**
     * Class stores Products and its quantity
     */
    public class Cart extends HashMap<ProductDTO, Integer>{

        public void put(ProductDTO product) {
            Integer value = get(product);
            if (value==null) {
                put(product, 1);
            } else {
                put(product, value+1);
            }
        }
        
        public void pop(ProductDTO product) {
            Integer value = get(product);
            if (value==null) {
                return;
            } 
            if (value > 1) {
                put(product, value-1);
            } else {
                remove(product);
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
