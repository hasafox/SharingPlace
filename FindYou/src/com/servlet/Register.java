package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.Service;

public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public Register() {
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
	    
		String name = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		Service service = new Service();
		
		name = service.IU(name);
		
		PrintWriter pw =  response.getWriter();
		boolean rs0 = service.queryName(name);
		boolean rs1 = service.queryEmail(email);
		boolean rs2 = service.register(name, email, password);
		pw.println(rs0);
		pw.println(rs1);
		pw.println(rs2);
	}
}
