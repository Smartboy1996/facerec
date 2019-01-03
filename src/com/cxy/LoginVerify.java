package com.cxy;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.*;

import com.cxy.util.DateUtil;

import com.cxy.util.Base64Util;
/**
 * 
 * @author CXY
 *
 */
@SuppressWarnings("serial")
public class LoginVerify extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("gbk");
        PrintWriter out=response.getWriter();
		Connection connection;
		Statement statement;
		ResultSet result;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			out.print("数据库驱动加载失败！<br/>");
			return;
		}
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		String character=request.getParameter("character");
		try {
			connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/facerec","root", "root");
			statement=connection.createStatement();
			String sql="select * from users where username='"+username+"'";
			System.out.println(sql);
			result=statement.executeQuery(sql);
			if(!result.next()){
				out.print("该用户尚未注册:<font color='blue'>"+username+"</font><br/>2秒后返回登录页面:<script type='text/javascript'>setTimeout(function(){window.location.href='../login_with_password.html';},2000)</script>");
				return;
			}
			sql="select * from users where username='"+username+"'and password='"+password+"'";
			System.out.println(sql);
			result=statement.executeQuery(sql);
			if(!result.next()){
				out.print("<font color='blue'>账号或密码错误</font><br/>2秒后返回登录页面:<script type='text/javascript'>setTimeout(function(){window.location.href='../login_with_password.html';},2000)</script>");
			}else{
				out.print("您已成功登陆!欢迎您："+username+",您的身份是："+character);
			}
		}catch(SQLException e) {
			out.print("数据库服务异常！<br/>");
			e.printStackTrace();
			return;
		}
	}

	public synchronized void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			String snapdata=request.getParameter("snapData");
			String imgcode=snapdata.substring(22);
			String imgname="temp"+DateUtil.getCurrentTime()+".png";
			String imgpath="E:/FacerecSysUserData/temp/"+imgname;
			Base64Util.GenerateImage(imgcode, imgpath);
			response.setCharacterEncoding("utf-8");
			PrintWriter out=response.getWriter();
			//运行python 人脸识别代码
			try{
				String[] args=new String[] {"python","E:/FacerecSysUserData/facerec.py",imgname};
				Process process=Runtime.getRuntime().exec(args);
				int status=process.waitFor();
				InputStreamReader ir=new InputStreamReader(process.getInputStream());
				LineNumberReader input=new LineNumberReader(ir);
				String result=input.readLine();
				if(status==1){
					out.print("not_find_face");
				}else if(result.equals("-1")){
					out.print("not_find_this_face");
				}else{
					out.print(result);
				}
				input.close();
				ir.close();
				process.destroy();
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				File file=new File(imgpath);
				if(file.exists()){
					file.delete();
				}
			}
			
	}

}
