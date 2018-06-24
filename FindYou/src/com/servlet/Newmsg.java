package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.Service;

public class Newmsg extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Newmsg() {
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
	    
		String uname = request.getParameter("uname");
		
		Service service = new Service();
		
		uname = service.IU(uname);
		PrintWriter pw =  response.getWriter();
		String rs = service.newmsg(uname);
		
		pw.println(rs);
	}

}
