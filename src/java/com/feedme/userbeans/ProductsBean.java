/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.userbeans;

import com.feedme.global.GlobalBean;
import com.feedme.service.Category;
import com.feedme.service.CategoryDTO;
import com.feedme.service.Product;
import com.feedme.service.ProductDTO;
import com.feedme.ws.Methods;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Managed bean for processing the Category and Products sections
 *
 * @author Giang
 */
@ManagedBean(name = "productsBean")
@ViewScoped
public class ProductsBean implements Serializable {

    private Category category; // Selected category
    private List<Product> products;
    private String search = "";
    private Product selectedProduct;
    private int page = 1, productListSize;

    /**
     * Creates a new instance of ProductsBean
     */
    public ProductsBean() {
        search = "";
        category = null;
        page = 1;
    }

    public List<Product> getProducts() {
        return getProductsRequest();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * Get all categories for Categories list on web page
     *
     * @return Category list
     */
    public List<Category> doGetCategories() {
        return Methods.fetchCategories().stream().map(CategoryDTO::getCategory).collect(Collectors.toList());
    }

    /**
     * Procedure when user selects a category from categories list
     *
     * @param category Selected category
     */
    public void doSelectCategory(Category category) {
        this.category = category;
        System.out.println(category);
    }

    public String doSelectCategoryNonAjax(Category category) {
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
     * Reset all filters
     *
     */
    public void doResetFilters() {
        category = null;
        search = "";
    }

    /**
     * Get context for showing in title bar
     *
     * @return
     */
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
        selectedProduct = Methods.fetchProductsById(id).getProduct();
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
        getProductsRequest();
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
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

    /**
     * Get image URLs of a product
     *
     * @param product
     * @return
     */
    public List<String> doGetImageUrls(Product product) {
        List<String> list = new java.util.ArrayList<>();
        for (String s : product.getImageFile().split(";")) {
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

    /**
     * Get the first image URL of an product If it doea not have any image, get
     * a default one
     *
     * @param product
     * @return
     */
    public String doGetFirstImageUrl(Product product) {
        List<String> list = doGetImageUrls(product);
        if (list.size() > 0) {
            try {
                return list.get(1);
            } catch (Exception ex) {
            }
        }
        return "./javax.faces.resource/img/default-photo.png.xhtml";
    }

    /**
     * Get discount amount of a product
     *
     * @param product
     * @return
     */
    public double doGetProductDiscount(Product product) {
        return GlobalBean.processDiscount(product.getPrice(), product.getPromotion());
    }

    public void doViewProductDetail(Product product) {
        selectedProduct = product;
    }

    private List<Product> getProductsRequest() {
        List<Product> prod = new ArrayList<>();
        List<ProductDTO> dto = new ArrayList<>();
        try {
            // Prepare fetching
            if (search == null) {
                search = "";
            }
            search = search.trim().replaceAll("\\s{2,}", "");

            // Fetch products
            dto = Methods.fetchProducts();
            if (!search.equals("") && category != null) {
                // Both category and search box are selected
                dto = dto.stream().filter(p -> {
                    return p.isIsActive() && p.getName().contains(search) && Objects.equals(p.getCategory().getCategory().getId(), category.getId());
                }).collect(Collectors.toList());
            } else if (category != null) {
                // Only category is selected
                dto = dto.stream().filter(p -> {
                    return p.isIsActive() && Objects.equals(p.getCategory().getCategory().getId(), category.getId());
                }).collect(Collectors.toList());
            } else if (search.length() > 0) {
                // Only search box is submitted
                dto = dto.stream().filter(p -> {
                    return p.isIsActive() && p.getName().contains(search);
                }).collect(Collectors.toList());
            } else {
                // No filtering (activated only)
                dto = dto.stream().filter(p -> {
                    return p.isIsActive();
                }).collect(Collectors.toList());
            }
            prod = dto.stream().map(ProductDTO::getProduct).collect(Collectors.toList());
            int size = dto.size();
            if (size == 0) {
                return prod;
            }
            
            // Paging
            int from = 12 * (page - 1), to = 12 * page - 1;
            prod = prod.subList(from > size ? size - 1 : from, to > size ? size : to);
            productListSize = prod.size();
            if (productListSize <= 0) {
                if (page > 1) {
                    page--;
                } else {
                    page++;
                }
                System.out.println("Concurrent >>>");
                return getProducts();
            }
            products = prod;
        } catch (ConcurrentModificationException | NullPointerException ex) {
            prod = new ArrayList<>();
            System.out.println("Error: " + ex.getMessage());
        }
        return prod;
    }
}
