package test;

import com.service.Service;

public class test {
	public static void main(String[] args) {
		boolean rs = new Service().setTime("hasafox@163.com", System.currentTimeMillis()+30*60*1000);
		System.out.println(rs);
	}	
}
