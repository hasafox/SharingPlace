package com.service;


import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

import com.db.DataBaseManager;

public class Service {
	
	public Boolean login(String username, String password) {
		
		String logSql = "select name from findyou where name = '"+username+"'and password = '"+password+"'";
		
		DataBaseManager dataBaseManager = new DataBaseManager();
		dataBaseManager.connDataBase();
        boolean rs = dataBaseManager.selectSQL(logSql);
        
        return rs;
	}
    
	public String navLogin(String username) {
		String nlgSQL = "select email from findyou where name = '"+username+"'";
		DataBaseManager dataBaseManager = new DataBaseManager();
		dataBaseManager.connDataBase();
		String rs = dataBaseManager.msgSQL(nlgSQL);
		
		return rs;
	}
 
	 public Boolean queryName(String username) {
	
		String querySql = "select name from findyou where name = '"+username+"'";
	
	    DataBaseManager dataBaseManager = new DataBaseManager();
        boolean rs = dataBaseManager.selectSQL(querySql);
        dataBaseManager.closeDataBase();
        
        return rs;
	}
	 
	 public Boolean queryEmail(String email) {
			
			String querySql = "select email from findyou where email = '"+email+"'";
		
		    DataBaseManager dataBaseManager = new DataBaseManager();
	        boolean rs = dataBaseManager.selectSQL(querySql);
	        dataBaseManager.closeDataBase();
	        
	        return rs;
		}

    public Boolean register(String username, String email, String password) {
    
    	String regSql = "insert into findyou (name,email,password) values ('"+username+"','"+email+"','"+password+"')";

        DataBaseManager dataBaseManager = new DataBaseManager();
        boolean rs = dataBaseManager.handleSQL(regSql);
        dataBaseManager.closeDataBase();
        
        return rs;
    }
    
    public Boolean update(String email) {
    	
    	String updSql = "select name from findyou where email = '"+email+"'";
    	
    	DataBaseManager dataBaseManager = new DataBaseManager();
    	boolean rs = dataBaseManager.selectSQL(updSql);
    	
		return rs;
    	
    }

	public boolean getLocation(String x, String y, String name) {
		String glcSql = "update findyou set x = '"+x+"',y = '"+y+"' where name = '"+name+"'";
		
		DataBaseManager dataBaseManager = new DataBaseManager();
        boolean rs = dataBaseManager.handleSQL(glcSql);
        dataBaseManager.closeDataBase();
        
		return rs;
	}
	
	
	public String IU(String Name) {
		String name = null;
		try {
			name = new String(Name.getBytes("iso-8859-1"),"utf-8") ;
			return name;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return name;
	}
	
	public String friens(String name) {
		String friSQL = "select bname,astatus from friends where aname = '"+name+"'";
		String freSQL = "select aname,bstatus from friends where bname = '"+name+"'";
		
		 DataBaseManager dataBaseManager = new DataBaseManager();
		 String rs1 = dataBaseManager.friendsSQL(friSQL);
		 String rs2 = dataBaseManager.friendsSQL(freSQL);
		 dataBaseManager.closeDataBase();
		 return rs1 + rs2;
	}
	
	
	public String addfriends(String uname, String sname, String msg) {
		
		int  rs = uname.compareTo(sname);
		
		String findSQL = "select name from findyou where name = '"+sname+"'";
		
		String anbSQL = "select id from friends Where aname = '"+uname+"' and bname = '"+sname+"'";
		String bnaSQL = "select id from friends where aname = '"+sname+"' and bname = '"+uname+"'";
		
		String apySql = "insert into apply (uname,sname,msg) values ('"+uname+"','"+sname+"','"+msg+"')";
		
		DataBaseManager dataBaseManager = new DataBaseManager();
		dataBaseManager.connDataBase();
		boolean rs0, rs1 = true, rs2 = false;
		
		rs0= dataBaseManager.selectSQL(findSQL);
		
		
		if (rs0) {
			 if(rs < 0){
			boolean a = dataBaseManager.selectSQL(anbSQL);
			rs1 = a;
			 }else{
			boolean b = dataBaseManager.selectSQL(bnaSQL);
			rs1 = b;
			 }
			if (!rs1) {
				rs2 = dataBaseManager.handleSQL(apySql);
			}
		}
		
		return rs0 + ":" + rs1 + ":" + rs2;
		}
	
	public String newmsg(String uname) {
		String newSQL = "select msg from apply where sname = '"+uname+"'";
		DataBaseManager dataBaseManager = new DataBaseManager();
		dataBaseManager.connDataBase();
		String rs = dataBaseManager.friendSQL(newSQL);
		
		return rs;
	}

	public String addrs(String result, String aname, String bname) {
		boolean rs1, rs2 = false;
		int rs = aname.compareTo(bname);
		
		String insSQL = "insert into friends (aname,bname) values ('"+aname+"','"+bname+"')";
		String delSQL = "delete from apply where uname = '"+bname+"' and sname = '"+aname+"'";
		String innSQL = "insert into friends (aname,bname) values ('"+bname+"','"+aname+"')";
		
		DataBaseManager dataBaseManager = new DataBaseManager();
		dataBaseManager.connDataBase();
		rs1 = dataBaseManager.handleSQL(delSQL);
		if (result.equals("1")){
			if (rs < 0) 
				rs2 = dataBaseManager.handleSQL(insSQL);
			else 
				rs2 = dataBaseManager.handleSQL(innSQL);
		}
		
		return rs1 + ":" + rs2;
	}

	public boolean friendStatus(String result, String aname, String bname) {
		int rs = aname.compareTo(bname);
		boolean rs1 = false;
		DataBaseManager dataBaseManager = new DataBaseManager();
		dataBaseManager.connDataBase();
		
		String staSQL = "update friends set astatus = '1' where aname = '"+aname+"' and bname = '"+bname+"'";
		String stuSQL = "update friends set bstatus = '1' where aname = '"+bname+"' and bname = '"+aname+"'";
		
		String stbSQL = "update friends set astatus = '0' where aname = '"+aname+"' and bname = '"+bname+"'";
		String stvSQL = "update friends set bstatus = '0' where aname = '"+bname+"' and bname = '"+aname+"'";
		
		if (result.equals("true")) {
			if (rs < 0) 
				rs1 = dataBaseManager.handleSQL(staSQL);
			else 
				rs1 = dataBaseManager.handleSQL(stuSQL);
		}
		else {
			if (rs < 0) 
				rs1 = dataBaseManager.handleSQL(stbSQL);
			else 
				rs1 = dataBaseManager.handleSQL(stvSQL);
		}
		
		return rs1;
	}

	public String postStu(String aname, String bname) {
		
		int rs = aname.compareTo(bname);
		
		String rs1 = "";
		DataBaseManager dataBaseManager = new DataBaseManager();
		dataBaseManager.connDataBase();
		
		String stuSQL = "select bstatus from friends where aname = '"+aname+"' and bname = '"+bname+"'";
		String stvSQL = "select astatus from friends where aname = '"+bname+"' and bname = '"+aname+"'";
		
		if (rs < 0)
			rs1 = dataBaseManager.msgSQL(stuSQL);
		else 
			rs1 = dataBaseManager.msgSQL(stvSQL);
				
		return rs1 + ":" + bname;
	}

	public String postLoc(String name) {
		
		DataBaseManager dataBaseManager = new DataBaseManager();
		dataBaseManager.connDataBase();
		String selSQL = "select x,y from findyou where name = '"+name+"'";
		
		String rs = dataBaseManager.friendsSQL(selSQL);
		return rs;
	}
	
	public boolean setTime(String email, long date) {
		
		DataBaseManager dataBaseManager = new DataBaseManager();
		dataBaseManager.connDataBase();
		String upSQL = "update findyou set date = '"+date+"' where email = '"+email+"'";
		
		boolean rs = dataBaseManager.handleSQL(upSQL);
		return rs;
	}
}
