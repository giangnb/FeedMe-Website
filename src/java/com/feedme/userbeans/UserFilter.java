/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.userbeans;

import com.feedme.service.PropertyDTO;
import com.feedme.ws.Methods;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Block user from main page if system_enabled = 0
 *
 * @author Giang
 */
@WebFilter(filterName = "userFilter", urlPatterns = {"/*"})
public class UserFilter implements Filter {

    private static final long serialVersionUID = 1L;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (((HttpServletRequest) request).getRequestURI().contains("/admin/") ||
                ((HttpServletRequest) request).getRequestURI().contains("/javax.faces.resource/") ||
                ((HttpServletRequest) request).getRequestURI().contains("/errors/")) {
            chain.doFilter(request, response);
            return;
        }
        
        HttpServletRequest req = (HttpServletRequest) request;

        PropertyDTO prop = Methods.fetchPropertyByKey("system_enabled");

        if (prop == null) {
            System.out.println("Cannot fetch system property!");
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/errors/disabled.xhtml");
            requestDispatcher.forward(request, response);
        }

        String isEnabled = prop.getValue();
        if (isEnabled.equals("1")) {
            chain.doFilter(request, response);
        } else {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/errors/disabled.xhtml");
            requestDispatcher.forward(request, response);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

}
