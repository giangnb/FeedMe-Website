/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.service.CategoryDTO;
import com.feedme.service.ProductDTO;
import com.feedme.ws.Methods;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * For products and categories managements
 *
 * @author Giang
 */
@ManagedBean(name = "productManaBean")
@ViewScoped
public class ProductManagerBean implements Serializable {

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
        if (category==null) {
            category = new CategoryDTO();
        }
        this.category = category;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        if (product==null) {
            product = new ProductDTO();
        }
        this.product = product;
    }
    
    public String doNewCategory() {
        category = new CategoryDTO();
        return "category";
    }

    public List<CategoryDTO> doLoadCategories() {
        List<CategoryDTO> list = Methods.fetchCategories();
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (list.isEmpty()) {
            ctx.addMessage("", new FacesMessage("Không có danh mục "));
        }
        return list;
    }

    public CategoryDTO doLoadCategory() {
        category = Methods.fetchCategoryById(category.getCategory().getId());
        if (category == null) {
            return null;
        }
        return category;
    }

    public String doAddCategory() {
        boolean result = Methods.addCategory(category);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!result) {
            ctx.addMessage("", new FacesMessage("Không thể thêm danh mục"));
        } else {
            ctx.addMessage("", new FacesMessage("Thêm danh mục thành công"));
        }
        return "category";
    }

    public String doUpdateCategory() {
        boolean result = Methods.updateCategory(category);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!result) {
            ctx.addMessage("", new FacesMessage("Không thể sửa danh mục"));
        } else {
            ctx.addMessage("", new FacesMessage("Sửa danh mục thành công"));
        }
        return "category";
    }
    
    public String doRemoveCategory() {
        boolean result = Methods.removeCategory(category.getCategory().getId());
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!result) {
            ctx.addMessage("", new FacesMessage("Không thể xóa danh mục"));
        } else {
            ctx.addMessage("", new FacesMessage("Xóa danh mục thành công"));
        }
        return "category";
    }

    public List<ProductDTO> doLoadProducts() {
        List<ProductDTO> list = Methods.fetchProducts();
        FacesContext ctx = FacesContext.getCurrentInstance();
         if (list.isEmpty()) {
            ctx.addMessage("", new FacesMessage("Không có sản phẩm"));
        }
        return list;
    }

    public ProductDTO doGetProduct(String name) {
        product = Methods.fetchProductsByName(name);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (product == null) {
            ctx.addMessage("", new FacesMessage("Không tìm thấy sản phẩm"));
        }
        return product;
    }

    public String doAddProduct() {
        boolean result = Methods.addProduct(product);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!result) {
            ctx.addMessage("", new FacesMessage("Không thể thêm sản phẩm"));
        } else {
            ctx.addMessage("", new FacesMessage("Thêm sản phẩm thành công"));
        }
        return "product";
    }

    public String doUpdateProduct() {
        boolean result = Methods.updateProduct(product);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!result) {
            ctx.addMessage("", new FacesMessage("Không thể sửa sản phẩm"));
        } else {
            ctx.addMessage("", new FacesMessage("Sửa sản phẩm thành công"));
        }
        return "product";
    }

    public String doRemoveProduct() {
        boolean result = Methods.removeProduct(product.getId());
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!result) {
            ctx.addMessage("", new FacesMessage("Không thể xóa sản phẩm"));
        } else {
            ctx.addMessage("", new FacesMessage("Xóa sản phẩm thành công"));
        }
        return "product";
    }
}
