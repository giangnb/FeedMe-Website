/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.global;

import com.feedme.service.PropertyDTO;
import com.feedme.ws.Methods;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;

/**
 *
 * @author Giang
 */
@ManagedBean
@ApplicationScoped
public class GlobalBean {

    private static DecimalFormat number;
    private static SimpleDateFormat date, time;
    private static String money;
    private static final Timer TIMER = new Timer();
    private static final HashMap<String, String> PROP = new HashMap<>();

    /**
     * Creates a new instance of GlobalBean
     */
    public GlobalBean() {
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
            return new SimpleDateFormat(date.toPattern() + " " + time.toPattern()).parse(d);
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
    
    public String doGetProperty(String key) {
        return PROP.get(key);
    }

    private static void startTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                new Thread(() -> {
                    Methods.fetchProperties().forEach((p) -> {
                        PROP.put(p.getKey(), p.getValue());
                    });
                    number = new DecimalFormat(PROP.get("format_number"));
                    date = new SimpleDateFormat(PROP.get("format_date"));
                    time = new SimpleDateFormat(PROP.get("format_time"));
                    money = PROP.get("format_currency");
                }).start();
            }
        };
        TIMER.schedule(task, 0, 60 * 1000);
    }
}
