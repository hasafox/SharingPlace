package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.Service;

public class PostStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PostStatus() {
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
	    
		String aname = request.getParameter("aname");
		String bname = request.getParameter("bname");
		
		
		
		
		Service service = new Service();
		
		aname = service.IU(aname);
		bname = service.IU(bname);
		
		PrintWriter pw =  response.getWriter();
		String rs = service.postStu(aname, bname);
		pw.println(rs);	

	}
}
