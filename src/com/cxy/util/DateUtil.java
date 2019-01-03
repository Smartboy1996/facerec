package com.cxy.util;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DateUtil {
	public static String getCurrentTime(){
		   SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
		   return df.format(new Date());	
	}
}

