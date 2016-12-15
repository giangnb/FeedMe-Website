/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.global.GlobalBean;
import com.feedme.info.Information;
import com.feedme.service.Category;
import com.feedme.service.CategoryDTO;
import com.feedme.service.ProductDTO;
import com.feedme.utils.Json;
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

    private List<CategoryDTO> categoryList;
    private List<ProductDTO> productList;

    public ProductManagerBean() {
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        if (category == null) {
            category = new CategoryDTO();
        }
        this.category = category;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        if (product == null) {
            product = new ProductDTO();
            product.setInfo(doGetProductInfo());
        } else {
            product.setInfo(doUpdateProductInfo(product.getInfo()));
        }
        this.product = product;
    }

    public String doNewCategory() {
        category = new CategoryDTO();
        return "category";
    }

    public List<CategoryDTO> doLoadCategories() {
        if (categoryList == null || categoryList.isEmpty()) {
            categoryList = Methods.fetchCategories();
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (categoryList.isEmpty()) {
                ctx.addMessage("", new FacesMessage("Không có danh mục "));
            }
        }
        return categoryList;
    }

    public List<Category> doLoadCategoryEntities() {
        List<Category> list = new java.util.ArrayList<>();
        for (CategoryDTO c : Methods.fetchCategories()) {
            list.add(c.getCategory());
        }
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
        if (productList == null || productList.isEmpty()) {
            productList = Methods.fetchProducts();
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (productList.isEmpty()) {
                ctx.addMessage("", new FacesMessage("Không có sản phẩm"));
            }
            return productList;
        }
        return productList;
    }

    public List<ProductDTO> doGetProduct(String name) {
        List<ProductDTO> prods = Methods.fetchProductsByName(name);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (prods.isEmpty()) {
            ctx.addMessage("", new FacesMessage("Không tìm thấy sản phẩm"));
        }
        return prods;
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
    
    public String doGetProductInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='table table-border table-hover'>");
        sb.append("</table>");
        return doUpdateProductInfo(sb.toString());
    }
    
    public String doUpdateProductInfo(String info) {
        StringBuilder sb = new StringBuilder(info);
        int offset = sb.length()-"</table>".length();
        String[] props = GlobalBean.getPropertyValue("info_product").split(";");
        for (String p : props) {
            if (sb.toString().contains("<th>"+p+"</th>")) {
                sb.append("<tr><th>")
                        .append(p)
                        .append("</th>")
                        .append("<td>&nbsp;</td></tr>");
            }
        }
        return info;
    }
}
