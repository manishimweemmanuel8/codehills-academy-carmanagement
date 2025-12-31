package com.carmanagement.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class HealthServlet extends BaseServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(CONTENT_TYPE_JSON);
        resp.setCharacterEncoding(CHARSET_UTF8);
        
        PrintWriter writer = resp.getWriter();
        writer.print("{\"status\":\"UP\"}");
        writer.flush();
    }
}

