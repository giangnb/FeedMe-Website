/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.global.GlobalBean;
import com.feedme.service.Category;
import com.feedme.service.CategoryDTO;
import com.feedme.service.Product;
import com.feedme.service.ProductDTO;
import com.feedme.ws.Methods;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * For products and categories managements
 *
 * @author Giang
 */
@ManagedBean(name = "productManaBean")
//@ViewScoped
@SessionScoped
public class ProductManagerBean implements Serializable {

    private CategoryDTO category, newCategory;
    private Product product, newProduct;

    private List<CategoryDTO> categoryList;
    private List<Product> productList;

    private short selectedCategoryId;
    private boolean productActivated;
    private String productImages, productImageUrl;
    private String productName, productPromo, productInfo, productDesc;
    private double productPrice;

    public ProductManagerBean() {
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductImages() {
        return productImages;
    }

    public void setProductImages(String productImages) {
        this.productImages = productImages;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPromo() {
        return productPromo;
    }

    public void setProductPromo(String productPromo) {
        this.productPromo = productPromo;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public CategoryDTO getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(CategoryDTO newCategory) {
        this.newCategory = newCategory;
    }

    public Product getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(Product newProduct) {
        this.newProduct = newProduct;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product p) {
        this.product = p;
        product.setInfo(doUpdateProductInfo(product.getInfo()));
        selectedCategoryId = product.getCategory().getId();
    }

    public short getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public void setSelectedCategoryId(short selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
    }

    public boolean isProductActivated() {
        return productActivated;
    }

    public void setProductActivated(boolean productActivated) {
        this.productActivated = productActivated;
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
        newCategory = null;
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
        newProduct = new Product();
        newProduct.setInfo(doGetProductInfo());
        newProduct.setPromotion("0");
        newProduct.setDescription(" ");
        selectedCategoryId = 1;
        productActivated = true;
        productImages = "";
        productDesc = "";
        productInfo = newProduct.getInfo();
        productName = "";
        productPrice = 1;
        productPromo = "0";
        return "product-edit";
    }

    public String doLoadProduct(short id) {
        product = Methods.fetchProductsById(id).getProduct();
        if (product != null) {
            selectedCategoryId = product.getCategory().getId();
            productActivated = product.isIsActive();
            productImages = product.getImageFile();
            productDesc = product.getDescription();
            productInfo = product.getInfo();
            productPromo = product.getPromotion();
            productName = product.getName();
            productPrice = product.getPrice();
            return "product-edit";
        } else {
            return "product";
        }
    }

    public List<Product> doLoadProducts() {
        product = null;
        newProduct = null;
        if (productList == null || productList.isEmpty()) {
            productList = Methods.fetchProducts().stream().map(ProductDTO::getProduct).collect(Collectors.toList());
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (productList.isEmpty()) {
                ctx.addMessage("", new FacesMessage("Không có sản phẩm"));
            }
        }
        return productList;
    }

    public List<Product> doGetProduct(String name) {
        List<Product> prods = Methods.fetchProductsByName(name).stream().map(ProductDTO::getProduct).collect(Collectors.toList());
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (prods.isEmpty()) {
            ctx.addMessage("", new FacesMessage("Không tìm thấy sản phẩm"));
        }
        return prods;
    }

    public String doAddProduct() {
        Category cat = new Category();
        cat.setId(selectedCategoryId);
        newProduct.setCategory(cat);
        newProduct.setImageFile(productImages);
        newProduct.setDescription(productDesc);
        newProduct.setInfo(productInfo);
        newProduct.setName(productName);
        newProduct.setIsActive(productActivated);
        newProduct.setPrice(productPrice);
        newProduct.setPromotion(productPromo);
        ProductDTO dto = new ProductDTO();
        dto.setProduct(newProduct);
        boolean result = Methods.addProduct(dto);
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
        Category cat = new Category();
        cat.setId(selectedCategoryId);
        product.setCategory(cat);
        product.setImageFile(productImages);
        product.setDescription(productDesc);
        product.setInfo(productInfo);
        product.setName(productName);
        product.setIsActive(productActivated);
        product.setPrice(productPrice);
        product.setPromotion(productPromo);
        ProductDTO dto = new ProductDTO();
        dto.setProduct(product);
        boolean result = Methods.updateProduct(dto);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!result) {
            ctx.addMessage("", new FacesMessage("Không thể sửa sản phẩm"));
        } else {
            ctx.addMessage("", new FacesMessage("Sửa sản phẩm thành công"));
        }
        productList = null;
        return "product-edit";
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

    public List<String> doGetUrls() {
        List<String> list = new java.util.ArrayList<>();
        for (String s : productImages.split(";")) {
            if (s.equals("")) {
                continue;
            }
            if (s.contains("[url]")) {
                list.add(s.replace("[url]", ""));
            } else {
                list.add("../javax.faces.resource/uploads/products/" + s + ".xhtml");
            }

        }
        return list;
    }

    public void eventFileUpload(FileUploadEvent event) {
        eventFileUploadNew(event);
        doUpdateProduct();
    }

    public void eventFileUploadNew(FileUploadEvent event) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        UploadedFile file = event.getFile();
        if (true) {
            try (InputStream input = file.getInputstream()) {
                String realPath = ((ServletContext) ctx.getExternalContext().getContext()).getRealPath("/")
                        + "/resources/uploads/products/";
                if (!new File(realPath).exists()) {
                    Files.createDirectory(Paths.get(realPath));
                }
                String fileName = new Date().getTime() + ".bin";
                File destination = new File(realPath + fileName);
                while (destination.exists()) {
                    fileName = new Date().getTime() + ".bin";
                    destination = new File(realPath + fileName);
                }
                Files.copy(input, destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                ctx.addMessage("", new FacesMessage("Upload ảnh sản phẩm thành công", "Đã upload ảnh " + file.getFileName()));
                productImages += fileName + ";";
            } catch (Exception ex) {
                ctx.addMessage("", new FacesMessage("Upload ảnh sản phẩm thất bại", ex.getMessage()));
                ex.printStackTrace();
            }
        } else {
            ctx.addMessage("", new FacesMessage("Tập tin không hợp lệ"));
        }
    }

    public void eventFileDelete(String fileName) {
        eventFileDeleteNew(fileName);
        doUpdateProduct();
    }

    public void eventFileDeleteNew(String fileName) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (fileName.contains("http") && fileName.contains(":")) {
            productImages = productImages.replace(fileName, "");
            productImages = productImages.replace("[url]", "");
            productImages = productImages.replace(";;", ";");
            productImages = productImages.equals(";") ? "" : productImages;
            return;
        }
        try {
            fileName = fileName.replace("../javax.faces.resource/uploads/products/", "")
                    .replace(".xhtml", "");
            String realPath = ((ServletContext) ctx.getExternalContext().getContext()).getRealPath("/")
                    + "/resources/uploads/products/" + fileName;
            productImages = productImages.replace(fileName, "");
            productImages = productImages.replace(";;", ";");
            productImages = productImages.equals(";") ? "" : productImages;
            Files.delete(new File(realPath).toPath());
            ctx.addMessage("", new FacesMessage("Xóa ảnh sản phẩm thành công"));
        } catch (IOException ex) {
            ctx.addMessage("", new FacesMessage("Xóa ảnh sản phẩm thất bại", ex.getMessage()));
            //ex.printStackTrace();
        }
    }

    private boolean validateFile(UploadedFile file) {
        if (file == null) {
            return false;
        }
        if (!file.getContentType().contains("image/")) {
            return false;
        }
        if (file.getSize() > 1000 * 1000 * 1.5) {
            return false;
        }
        return true;
    }

    public void doSetImageUrl() {
        productImages += ";" + "[url]" + productImageUrl + ";";
        productImages = productImages.replace(";;", ";");
        System.out.println("set " + productImages);
        doUpdateProduct();
    }
}
