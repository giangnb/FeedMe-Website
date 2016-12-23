/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.userbeans;

import com.feedme.global.GlobalBean;
import com.feedme.service.CategoryDTO;
import com.feedme.service.ProductDTO;
import com.feedme.ws.Methods;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

/**
 * Managed bean for processing the Category and Products sections
 *
 * @author Giang
 */
@ManagedBean
@SessionScoped
public class ProductsBean implements Serializable {

    private CategoryDTO category; // Selected category
    private String search = "";
    private ProductDTO selectedProduct;
    private int page = 1, productListSize;

    /**
     * Creates a new instance of ProductsBean
     */
    public ProductsBean() {
        search = "";
    }

    /**
     * Get all categories for Categories list on web page
     *
     * @return Category list
     */
    public List<CategoryDTO> doGetCategories() {
        return Methods.fetchCategories();
    }

    /**
     * Procedure when user selects a category from categories list
     *
     * @param category Selected category
     * @return 
     */
    public String doSelectCategory(CategoryDTO category) {
        this.category = category;
        return "index";
    }

    /**
     * Procedure when user submits the search form
     *
     */
    public void doSearch() {
        if (search.matches("\\s*")) {
            search = null;
        }
    }

    /**
     * Get products to show on home page
     *
     * @return Products are filtered by selected category and/or search box
     */
    public List<ProductDTO> doGetProducts() {
        if (search != null && category != null) {
            // Both category and search box are selected
            return getProductsByCategoryAndName();
        } else if (category != null) {
            // Only category is selected
            return getProductsByCategory();
        } else if (search.length() > 0) {
            // Only search box is submitted
            return getProductsByName();
        } else {
            // No filtering (activated only)
            return getAllProducts();
        }
    }

    /**
     * Get products to show on home page with applied pagination
     *
     * @return Products are filtered by selected category and/or search box
     */
    public List<ProductDTO> doGetProductsRange() {
        List<ProductDTO> list;
        if (search==null) {search = "";}
        if (!search.equals("") && category != null) {
            // Both category and search box are selected
            list = getProductsByCategoryAndName();
        } else if (category != null) {
            // Only category is selected
            list = getProductsByCategory();
        } else if (search.length() > 0) {
            // Only search box is submitted
            list = getProductsByName();
        } else {
            // No filtering (activated only)
            list = getAllProducts();
        }
        int size = list.size();
        if (size==0) {
            return list;
        }
        int from = 12 * (page - 1), to = 12 * page-1;
        System.out.println("Total products: "+size);
        System.out.println("From: "+from);
        System.out.println("To: "+to);
        list = list.subList(from>size?size-1:from, to>size?size:to);
        productListSize = list.size();
        if (productListSize <= 0) {
            if (page > 1) {
                page--;
            } else {
                page++;
            }
            return doGetProductsRange();
        }
        return list;
    }

    /**
     * Reset all filters
     *
     * @return
     */
    public String doResetFilters() {
        category = null;
        search = "";
        return "index";
    }

    public String doGetFilterContext() {
        String result = "&nbsp;";
        if (search.length() > 0 && category != null) {
            result = String.format("Tìm \"%s\" trong %s", search, category.getName());
        } else if (search.length() > 0) {
            result = String.format("Tìm kiếm \"%s\"", search);
        } else if (category != null) {
            result = String.format("Danh mục %s", category.getName());
        }
        return result;
    }

    public void doSelectProduct(short id) {
        selectedProduct = Methods.fetchProductsById(id);
    }

    /**
     * Get products which match search key word and in selected category
     *
     * @return
     */
    private List<ProductDTO> getProductsByCategoryAndName() {
        List<ProductDTO> list = Methods.fetchProductsByName(search);
        for (ProductDTO p : list) {
            if (!Objects.equals(p.getCategory().getCategory().getId(), category.getCategory().getId())) {
                list.remove(p);
            }
        }
        return list;
    }

    /**
     * Get products which in selected category
     *
     * @return
     */
    private List<ProductDTO> getProductsByCategory() {
        List<ProductDTO> list = getAllProducts();
        for (ProductDTO p : list) {
            System.out.println("prod cat: "+p.getCategory().getCategory().getId());
            if (!Objects.equals(p.getCategory().getCategory().getId(), category.getCategory().getId())) {
                list.remove(p);
            }
        }
        System.out.println("list: "+list.size());
        return list;
    }

    /**
     * Get products that match search key word
     *
     * @return
     */
    private List<ProductDTO> getProductsByName() {
        return Methods.fetchProductsByName(search);
    }

    /**
     * Get all activating products
     *
     * @return
     */
    private List<ProductDTO> getAllProducts() {
        return Methods.fetchProducts();
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public ProductDTO getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(ProductDTO selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void doPreviousPage() {
        page--;
    }

    public void doNextPage() {
        page++;
    }

    public int getProductListSize() {
        return productListSize;
    }

    public void setProductListSize(int productListSize) {
        this.productListSize = productListSize;
    }

    public List<String> doGetImageUrls(ProductDTO product) {
        List<String> list = new java.util.ArrayList<>();
        for (String s : product.getImageUrl().split(";")) {
            if (s.equals("")) {
                continue;
            }
            if (s.contains("[url]")) {
                list.add(s.replace("[url]", ""));
            } else {
                list.add("./javax.faces.resource/uploads/products/" + s + ".xhtml");
            }

        }
        return list;
    }
    
    public String doGetFirstImageUrl(ProductDTO product) {
        List<String> list = doGetImageUrls(product);
        if (list.size()>0) {
            try {
                return list.get(1);
            } catch(Exception ex) {}
        }
        return "./javax.faces.resource/img/default-photo.png.xhtml";
    }
    
    public double doGetProductDiscount(ProductDTO product) {
        return GlobalBean.processDiscount(product.getPrice(), product.getPromotion());
    }
    
    public void doViewProductDetail(ProductDTO product) {
        selectedProduct = product;
    }
}
