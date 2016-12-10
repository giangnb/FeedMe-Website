/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.global;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;

/**
 *
 * @author Giang
 */
@ManagedBean
@ApplicationScoped
public class GlobalBean {

    private DecimalFormat number;
    private SimpleDateFormat date, time;
    private String money;

    /**
     * Creates a new instance of GlobalBean
     */
    public GlobalBean() {
        doLoadProperties();
        startTimer();
    }

    public String doFormatPrice(String formattedNumber) {
        String result = "";
        try {
            String[] str = money.split("\\?");
            result = str[0] + formattedNumber + str[1];
        } catch (Exception ex) {
        }
        return result.trim();
    }

    public String doFormatPrice(double price) {
        return doFormatPrice(doFormatNumber(price));
    }

    public String doFormatNumber(double num) {
        return number.format(num);
    }

    public String doFormatTime(Date d) {
        return time.format(d);
    }

    public String doFormatDate(Date d) {
        return date.format(d);
    }
    
    public Double doParsePrice(String price) {
        for (String s : money.split("\\?")) {
            price = price.replace(s.trim(), "");
        }
        return doParseNumber(price);
    }

    public Double doParseNumber(String num) {
        try {
            return number.parse(num).doubleValue();
        } catch (ParseException ex) {
            return null;
        }
    }

    public Date doParseDateTime(String d) {
        try {
            return new SimpleDateFormat(date.toPattern()+" "+time.toPattern()).parse(d);
        } catch (ParseException ex) {
            return null;
        }
    }

    public Date doParseDate(String d) {
        try {
            return date.parse(d);
        } catch (ParseException ex) {
            return null;
        }
    }
    
    private void doLoadProperties() {
        // Demo data
        number = new DecimalFormat("#,###.##");
        date = new SimpleDateFormat("dd/MM/yyyy");
        time = new SimpleDateFormat("hh:mm a");
        money = " ?đ ";
    }

    private void startTimer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
