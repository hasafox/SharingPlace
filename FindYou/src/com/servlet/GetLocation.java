package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.Service;

public class GetLocation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetLocation() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
		}				

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			
		request.setCharacterEncoding("utf-8");  
	    response.setCharacterEncoding("utf-8");
	    
		String x = request.getParameter("x");
		String y = request.getParameter("y");
		String name = request.getParameter("name");
		
		Service service = new Service();
		
		name = service.IU(name);
		
		PrintWriter pw =  response.getWriter();
		boolean rs = service.getLocation(x, y, name);
		pw.println(rs);
	}

}
