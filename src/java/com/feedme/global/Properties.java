/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.global;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Giang
 */
public class Properties extends HashMap<String, String> implements Serializable{
    public void removeAll() {
        this.clear();
    }
    
    public void loadProperties() {
        this.clear();
        // load PropertyDTO from Web Service
    }
}
