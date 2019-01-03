package com.cxy;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Color;

import java.util.Random;

/**
 * 
 * @author CXY
 *
 */
public class ValiImageServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//1.创建一个BufferedImage对象
		int base=30;
		int width=base*4;
		int height=base;
		BufferedImage valiimage=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		//2.获取图像的画布对象Graphics2D
		Graphics2D g=(Graphics2D)valiimage.getGraphics();
		//3.绘制图像背景色&边框
		g.setColor(new Color(199,238,206));//豆沙绿
		g.fillRect(0, 0, width, height);
		g.setColor(Color.LIGHT_GRAY);
		g.drawRect(0, 0, width-1, height-1);
		//4.绘制字符
		g.setColor(Color.BLACK);
		g.setFont(new Font("Times New Roman",Font.PLAIN,20));
		String strs="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		for(int i=0;i<4;i++){
			g.drawString(strs.charAt(getRand(0,strs.length()))+"", 8+i*base, 20);
		}
		//向浏览器输出验证码图片
		g.dispose();
		response.setContentType("image/jpeg");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", -1);
		OutputStream out=response.getOutputStream();
		ImageIO.write(valiimage,"jpg",out);
		
		

	}
	/**
	 * 获取一个介于begin和end之间的整数,包含begin，不包括end
	 */
	private Random r=new Random();
	private int getRand(int begin,int end){
		int ranumber=r.nextInt(end-begin)+begin;
		return ranumber;
	}

}
