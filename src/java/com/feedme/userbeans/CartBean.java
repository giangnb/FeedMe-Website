/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.userbeans;

import com.feedme.service.OrderDetailDTO;
import com.feedme.service.ProductDTO;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Giang
 */
@ManagedBean
@SessionScoped
public class CartBean {
    OrderDetailDTO order;

    /**
     * Creates a new instance of CartBean
     */
    public CartBean() {
        order = new OrderDetailDTO();
    }
    
    /**
     * Get products in cart
     * @return 
     */
    public List<ProductDTO> doGetCart() {
        return order.getFoods();
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
}
