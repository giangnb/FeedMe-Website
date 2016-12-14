/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.service.PropertyDTO;
import com.feedme.ws.Methods;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 * For manager's management and configuration sections
 * @author Giang
 */
@ManagedBean
@RequestScoped
public class ManagerBean {
   private PropertyDTO property;
   private String Key;
    /**
     * Creates a new instance of ManagerBean
     */
    public ManagerBean() {
    }

    public PropertyDTO getProperty() {
        return property;
    }

    public void setProperty(PropertyDTO property) {
        this.property = property;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String Key) {
        this.Key = Key;
    }
    
    
   public List<PropertyDTO> doLoadProperties() {
     List<PropertyDTO> list = Methods.fetchProperties();
     return list;
   }
   
   /**
    * Search Property
    */
   public PropertyDTO doGetProperty() {
       property = Methods.fetchPropertyByKey(Key);
       if (property==null) {
         FacesContext ctx = FacesContext.getCurrentInstance();
          ctx.addMessage("", new FacesMessage("Không tìm thấy Property có Key = " + Key));
       }
      return property;
   }
   
   public String doAddProperty() {
      boolean result = Methods.addProperty(property);
      if (!result) {
          FacesContext ctx = FacesContext.getCurrentInstance();
          ctx.addMessage("", new FacesMessage("Không thêm mới Property"));
      }
      return "success";
   }
   
    public String doEditProperty() {
      boolean result = Methods.updateProperties(property);
      if (!result) {
          FacesContext ctx = FacesContext.getCurrentInstance();
          ctx.addMessage("", new FacesMessage("Không sửa được Property"));
      }
      return "success";
   }
   
     public String doDeleteProperty() {
      boolean result = Methods.removeProperties(property.getKey());
      if (!result) {
          FacesContext ctx = FacesContext.getCurrentInstance();
          ctx.addMessage("", new FacesMessage("Không xóa được Property"));
      }
      return "success";
   }
   
   
}
