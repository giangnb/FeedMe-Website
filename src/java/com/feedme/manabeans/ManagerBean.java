/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.service.PropertyDTO;
import com.feedme.ws.Methods;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * For manager's management and configuration sections
 * @author Giang
 */
@ManagedBean
@RequestScoped
public class ManagerBean {
   
    /**
     * Creates a new instance of ManagerBean
     */
    public ManagerBean() {
    }
    
   public List<PropertyDTO> doLoadProperties() {
     List<PropertyDTO> list = Methods.fetchProperties();
     return list;
   }
   
   public String doAddProperty() {
       
      return "";
   }
}
