/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.userbeans;

import com.feedme.service.CategoryDTO;
import com.feedme.service.ProductDTO;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Giang
 */
@ManagedBean
@ViewScoped
public class ProductsBean {
    private CategoryDTO category; // Selected category
    private String search="";

    /**
     * Creates a new instance of ProductsBean
     */
    public ProductsBean() {
        search = "";
    }
    
    /**
     * Get all categories for Categories list on web page
     * @return Category list
     */
    public List<CategoryDTO> doGetCategories() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Procedure when user selects a category from categories list
     * @param category Selected category
     * @return Back to home page
     */
    public String doSelectCategory(CategoryDTO category) {
        this.category = category;
        return "index";
    }
    
    /**
     * Procedure when user submits the search form
     * @param search
     * @return Back to home page
     */
    public String doSearch(String search) {
        this.search = search.trim();
        return "index";
    }
    
    /**
     * Get products to show on home page
     * @return Products are filtered by selected category and/or search box
     */
    public List<ProductDTO> doGetProducts() {
        if (search!=null && category != null) {
            // Both category and search box are selected
            return getProductsByCategoryAndName();
        }
        else if (category!=null) {
            // Only category is selected
            return getProductsByCategory();
        }
        else if (search.length()>0) {
            // Only search box is submitted
            return getProductsByName();
        } else {
            // No filtering (activated only)
            return getAllProducts();
        }
    }

    /**
     * Get products which match search key word and in selected category
     * @return 
     */
    private List<ProductDTO> getProductsByCategoryAndName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Get products which in selected category
     * @return 
     */
    private List<ProductDTO> getProductsByCategory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Get products that match search key word
     * @return 
     */
    private List<ProductDTO> getProductsByName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Get all activating products
     * @return 
     */
    private List<ProductDTO> getAllProducts() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
