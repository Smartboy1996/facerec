package com.cxy;

import java.io.*;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cxy.util.Base64Util;

@SuppressWarnings("serial")
public class SignupVerify extends HttpServlet {
	Connection connection;
	Statement statement;
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		File file=new File("E:/FacerecSysUserData/userdata/image/"+username+".png");
		//verify
		if(file.exists()){
			System.out.println(username+"已被注册");
			out.print("fail");
			return;
		}
		//save to table
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			System.out.println("数据库驱动加载失败！<br/>");
			return;
		}
		try{
			connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/facerec","root", "root");
			statement=connection.createStatement();
			String sql="insert into users (username,password) values ('"+username+"','"+password+"')";
			statement.execute(sql);
			System.out.println(sql);
		}catch(SQLException e){
			System.out.println("数据库服务异常！");
		}
		//save img
		String snapdata=request.getParameter("snapData");
		String imgcode=snapdata.substring(22);
		Base64Util.GenerateImage(imgcode, "E:/FacerecSysUserData/userdata/image/"+username+".png");
		try{
			String[] args=new String[] {"python","E:/FacerecSysUserData/crop_encoding.py",username};
			Process process=Runtime.getRuntime().exec(args);
			int status=process.waitFor();
			if(status==1){
				out.print("error");
				//删除已保存的图片和数据库中的信息
				if(file.exists()){
					file.delete();
				}
				try{
					String sql="delete from users where username='"+username+"';";
					System.out.println(sql);
					statement.execute(sql);
				}catch(SQLException e){
					e.printStackTrace();
					return;
				}
				
				return;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(username+"注册成功");
		out.print("success");

	}

}
