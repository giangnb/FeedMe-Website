/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.service.CategoryDTO;
import com.feedme.service.ProductDTO;
import com.feedme.ws.Methods;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * For products and categories managements
 * @author Giang
 */
@ManagedBean(name = "productManaBean")
@ViewScoped
public class ProductManagerBean {
   private CategoryDTO category;
   private ProductDTO product;
  
    /**
     * Creates a new instance of ProductManagerBean
     */
    public ProductManagerBean() {
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }
    
    public List<CategoryDTO> doLoadCategories() {
       List<CategoryDTO> list = Methods.fetchCategories();
       if (list==null) {
           return null;
       }
       return list;
    }
    
    public CategoryDTO doLoadCategory() {
      category = Methods.fetchCategoryById(category.getCategory().getId());
      if (category==null) {
        return null;
      }
       return category;
    }
    
    public String doAddCategory() {
      boolean result = Methods.addCategory(category);
       if (!result) {
           FacesContext ctx = FacesContext.getCurrentInstance();
           ctx.addMessage("", new FacesMessage("Không thể thêm danh mục"));
       }
        return "";
    }
    
    public String doUpdateCategiry() {
       boolean result = Methods.updateCategory(category);
       if (!result) {
           FacesContext ctx = FacesContext.getCurrentInstance();
           ctx.addMessage("", new FacesMessage("Không thể sửa danh mục"));
       }
       return"ok";
    }
    
    public List<ProductDTO> doLoadProducts() {
      List<ProductDTO> list = null;
      return list;
    }
    
    public ProductDTO doGetProduct(String name) {
        product = Methods.fetchProductsByName(name);
        if (product==null) {
           FacesContext ctx = FacesContext.getCurrentInstance();
           ctx.addMessage("", new FacesMessage("Khong tim thay san pham"));
        }
        return product;
    }
    
    public String doUpdateProduct() {
        boolean result = Methods.updateProduct(product);
       if (!result) {
           FacesContext ctx = FacesContext.getCurrentInstance();
           ctx.addMessage("", new FacesMessage("Không thể sửa san pham"));
       }
       return"ok";
    }
    
     public String doRemoveProduct() {
        boolean result = Methods.removeProduct(product.getId());
       if (!result) {
           FacesContext ctx = FacesContext.getCurrentInstance();
           ctx.addMessage("", new FacesMessage("Không thể xoa san pham"));
       }
       return"ok";
    }
}
