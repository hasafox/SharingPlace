package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.Service;

public class AddFriends extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public AddFriends() {
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
		String sname = request.getParameter("sname");
		
		Service service = new Service();
		
		uname = service.IU(uname);
		sname = service.IU(sname);
		
		String msg = uname;
		PrintWriter pw =  response.getWriter();
		String rs = service.addfriends(uname, sname, msg);
		pw.println(rs);
	}

}
