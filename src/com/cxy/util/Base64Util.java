package com.cxy.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import sun.misc.BASE64Decoder;  
import sun.misc.BASE64Encoder;  
public class Base64Util   
{  
    //ͼƬת����base64�ַ���  
    public static String GetImageStr()  
    {//��ͼƬ�ļ�ת��Ϊ�ֽ������ַ��������������Base64���봦��  
        String imgFile = "D:\\tupian\\a.jpg";//�������ͼƬ  
        InputStream in = null;  
        byte[] data = null;  
        //��ȡͼƬ�ֽ�����  
        try   
        {  
            in = new FileInputStream(imgFile);          
            data = new byte[in.available()];  
            in.read(data);  
            in.close();  
        }   
        catch (IOException e)   
        {  
            e.printStackTrace();  
        }  
        //���ֽ�����Base64����  
        BASE64Encoder encoder = new BASE64Encoder();  
        return encoder.encode(data);//����Base64��������ֽ������ַ���  
    }  
      
    //base64�ַ���ת����ͼƬ  
    public static boolean GenerateImage(String imgcode,String imgFilePath)  
    {   //���ֽ������ַ�������Base64���벢����ͼƬ  
        if (imgcode == null) //ͼ������Ϊ��  
            return false;  
        BASE64Decoder decoder = new BASE64Decoder();  
        try   
        {  
            //Base64����  
            byte[] b = decoder.decodeBuffer(imgcode);  
            for(int i=0;i<b.length;++i)  
            {  
                if(b[i]<0)  
                {//�����쳣����  
                    b[i]+=256;  
                }  
            }  
            //����ͼƬ   
            OutputStream out = new FileOutputStream(imgFilePath);      
            out.write(b);  
            out.flush();  
            out.close();  
            return true;  
        }   
        catch (Exception e)   
        {  
            e.printStackTrace();
        	return false;  
        }  
    }  
}
