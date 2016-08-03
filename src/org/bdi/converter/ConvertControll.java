package org.bdi.converter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.csvreader.CsvReader;

public class ConvertControll {
	
	public void convertControll(String from,String to,Charset csv_charset){
		ArrayList<String[]> params = convertReader(from,csv_charset);
		convertWriter(to,params);	
	}
	
	public ArrayList<String[]> convertReader(String from,Charset charset)
	{
		//logger定位行数,列数
//		int row = 0;
//		int sep = 0;
		//初始化参数
		int i;
		InputStream is = null;
		CsvReader creader = null;
		String [] listHeader = null;
		ArrayList<String[]> arr = new ArrayList<String[]>();
		String [] line = null;
		//按路径读取csv文件
		try {
			is = new FileInputStream(from);
			creader = new CsvReader(is,charset);
			//读取头部属性
			creader.readHeaders();
			listHeader = creader.getHeaders();
			arr.add(listHeader);
			while(creader.readRecord())
			{
				line = creader.getValues();
				//判断改行是否空行，或缺参。不缺走下一步判断，缺则继续下一行
				if(line.length != listHeader.length)
				{
					//logger
					continue;
				}
				//判断参数是否有空字符串
				for(i=0;i<listHeader.length;i++)
				{
					if(line[i] == "")
					{
						//logger
						break;
					}
				}
				//是否通过条件筛选
				if(i == listHeader.length)
				{
					arr.add(line);
				}
			}
		} 
		catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					//throw new RuntimeException("csv文件不存在:"+from);
			System.out.println("csv文件不存在:"+from);
			System.exit(1);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//throw new RuntimeException("CSVReader内容读取出错");
			System.out.println("CSVReader内容读取出错");
			System.exit(1);
		}	
		finally{
			if(is != null)
			{
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					//throw new RuntimeException("InputStream关闭失败");
					System.out.println("InputStream关闭失败");
					System.exit(1);
				}
			}
		}
		return arr;
	}
	
	public void convertWriter(String to,ArrayList<String[]> arr){
		String fileName = "web.xml";
		File file = new File(to);
		OutputStream os = null;
		//只获取文件夹路径
		if(!file.exists()){
			//throw new RuntimeException("路径不存在:"+to);
			System.out.println("路径不存在:"+to);
			System.exit(1);
		}
		if(file.isDirectory())
		{
			file = new File(to,fileName);
			try {
				if(file.exists())
				{
					//throw new RuntimeException("同名xml文件已存在，请更换路径:"+to);
					System.out.println("同名xml文件已存在，请更换路径:"+to);
					System.exit(1);
				}
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//throw new RuntimeException("xml文件创建出错:"+to);
				System.out.println("xml文件创建出错:"+to);
				System.exit(1);
			}
		}
		else if(file.isFile())
		{
			
			//throw new RuntimeException("同名xml文件已存在，请更换路径:"+to);
			System.out.println("同名xml文件已存在，请更换路径:"+to);
			System.exit(1);
		}
		else{
			//throw new RuntimeException("路径不存在:"+to);
			System.out.println("路径不存在:"+to);
			System.exit(1);
		}
		
		final String aHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
		+"<web-app version=\"3.0\" xmlns=\"http://java.sun.com/xml/ns/javaee\""
        +"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
        +"xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee "
        +"http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd\">"
        +"<display-name>dsw</display-name>\n\n";
		final String aTail = "\n<welcome-file-list>"
		+"<welcome-file>index.html</welcome-file>"
		+"<welcome-file>index.htm</welcome-file>"
		+"<welcome-file>index.jsp</welcome-file>"
		+"<welcome-file>default.html</welcome-file>"
		+"<welcome-file>default.htm</welcome-file>"
		+"<welcome-file>default.jsp</welcome-file>"
		+"</welcome-file-list>"
		+"</web-app>";
		String content = "";
		content = content+aHead;
		for(int i = 1;i < arr.size();i++)
		{
			content = content + servletParts(i,arr.get(i),arr.get(0));
		}
		content = content + aTail;
		try {
			os = new FileOutputStream(file);
		//	System.out.println("FileOutputStream");
			os = new BufferedOutputStream(os);
		//	System.out.println("BufferedOutputStream");
			os.write(content.getBytes());
		//	System.out.println("parseByte");
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//throw new RuntimeException("xml文件写入失败");
			System.out.println("xml文件写入失败");
			System.exit(1);
		}
		finally{
			if(os != null)
			{
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					//throw new RuntimeException("OutputStream关闭失败");
					System.out.println("OutputStream关闭失败");
					System.exit(1);
				}
			}
		}
	}
	
	public String servletParts(int i, String [] param ,String [] header)
	{
		final String servHead = "<servlet>"
				+"<servlet-name>dsw"+i+"</servlet-name>"
				+"<servlet-class>org.glassfish.jersey.servlet.ServletContainer</servlet-class>";
		final String servTail = "</servlet>";
		String initParams = "";
		int n;
		for(n = 0;n < param.length;n++)
		{
			initParams = initParams+"<init-param><param-name>"+header[n]+"</param-name><param-value>"
					+param[n]+"</param-value></init-param>";
		}
		String ServMap = "<servlet-mapping><servlet-name>dsw"+i
				+"</servlet-name><url-pattern>/"+param[0]+"/"+param[1]
				+"</url-pattern></servlet-mapping>\n\n";			
		
		return servHead+initParams+servTail+ServMap;
	}
}
