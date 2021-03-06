/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

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
import javax.servlet.http.HttpSession;

/**
 *
 * @author Giang
 */
@WebFilter(filterName = "adminFilter", urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    private static final long serialVersionUID = 1L;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        HttpSession session = req.getSession(true);
        String loginSession = session.getAttribute("isLoggedIn")+"";

        if (loginSession.equals("1")) {
            session.setMaxInactiveInterval(30*60);
            String url = ((HttpServletRequest) request).getRequestURL().toString();
            chain.doFilter(request, response);
        } else {
            session.setMaxInactiveInterval(90*60);
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/admin/login.xhtml?faces-redirect=true");
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
