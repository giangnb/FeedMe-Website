/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.global.GlobalBean;
import com.feedme.service.Category;
import com.feedme.service.CategoryDTO;
import com.feedme.service.ProductDTO;
import com.feedme.ws.Methods;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

/**
 * For products and categories managements
 *
 * @author Giang
 */
@ManagedBean(name = "productManaBean")
@ViewScoped
public class ProductManagerBean implements Serializable {

    private CategoryDTO category, newCategory;
    private ProductDTO product, newProduct;

    private List<CategoryDTO> categoryList;
    private List<ProductDTO> productList;

    private short selectedCategoryId;

    public ProductManagerBean() {
    }

    public CategoryDTO getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(CategoryDTO newCategory) {
        this.newCategory = newCategory;
    }

    public ProductDTO getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(ProductDTO newProduct) {
        this.newProduct = newProduct;
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

    public void setProduct(ProductDTO p) {
        System.out.println("set" + p);
        this.product = p;
        product.setInfo(doUpdateProductInfo(product.getInfo()));
        selectedCategoryId = product.getCategory().getCategory().getId();
    }

    public short getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public void setSelectedCategoryId(short selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
    }

    public String doNewCategory() {
        newCategory = new CategoryDTO();
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
        boolean result = Methods.addCategory(newCategory);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!result) {
            ctx.addMessage("", new FacesMessage("Không thể thêm danh mục"));
        } else {
            ctx.addMessage("", new FacesMessage("Thêm danh mục thành công"));
        }
        newCategory=null;
        categoryList = null;
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
        categoryList = null;
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
        categoryList = null;
        return "category";
    }

    public String doNewProduct() {
        newProduct = new ProductDTO();
        newProduct.setCategory(new CategoryDTO());
        newProduct.setInfo(doGetProductInfo());
        newProduct.setPromotion("0");
        newProduct.setIsActive(true);
        newProduct.setDescription(" ");
        selectedCategoryId = 1;
        return "product";
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
        Category cat = new Category();
        cat.setId(selectedCategoryId);
        CategoryDTO dto = new CategoryDTO();
        dto.setCategory(cat);
        newProduct.setCategory(dto);
        boolean result = Methods.addProduct(newProduct);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!result) {
            ctx.addMessage("", new FacesMessage("Không thể thêm sản phẩm"));
        } else {
            ctx.addMessage("", new FacesMessage("Thêm sản phẩm thành công"));
        }
        productList = null;
        return "product";
    }

    public String doUpdateProduct() {
        System.out.println("Prod ID: "+product.getId());
        Category cat = new Category();
        cat.setId(selectedCategoryId);
        CategoryDTO dto = new CategoryDTO();
        dto.setCategory(cat);
        product.setCategory(dto);
        boolean result = Methods.updateProduct(product);
        System.out.println("Status: "+result);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!result) {
            ctx.addMessage("", new FacesMessage("Không thể sửa sản phẩm"));
        } else {
            ctx.addMessage("", new FacesMessage("Sửa sản phẩm thành công"));
        }
        productList = null;
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
        productList = null;
        product = null;
        return "product";
    }

    private String doGetProductInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1' width='100%'>");
        sb.append("</table>");
        return doUpdateProductInfo(sb.toString());
    }

    private String doUpdateProductInfo(String info) {
        StringBuilder sb = new StringBuilder(info);
        if (!(sb.toString().contains("<table border='1' width='100%'>") && sb.toString().contains("</table>"))) {
            sb = new StringBuilder();
            sb.append("<table border='1' width='100%' cellspacing='0'>");
            sb.append("</table>");
        }
        int offset = sb.length() - "</table>".length();
        List<String> props = Arrays.asList(GlobalBean.getPropertyValue("info_product").split(";"));
        Collections.reverse(props);
        for (String p : props) {
            if (!sb.toString().contains("<th width='25%%'>" + p + "</th>")) {
                sb.insert(offset, String.format("<tr><th width='25%%'>%s</th><td>&nbsp;</td></tr>", p));
            }
        }
        return sb.toString();
    }
}
