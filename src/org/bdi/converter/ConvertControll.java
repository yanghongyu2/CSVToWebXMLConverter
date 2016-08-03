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
		//logger��λ����,����
//		int row = 0;
//		int sep = 0;
		//��ʼ������
		int i;
		InputStream is = null;
		CsvReader creader = null;
		String [] listHeader = null;
		ArrayList<String[]> arr = new ArrayList<String[]>();
		String [] line = null;
		//��·����ȡcsv�ļ�
		try {
			is = new FileInputStream(from);
			creader = new CsvReader(is,charset);
			//��ȡͷ������
			creader.readHeaders();
			listHeader = creader.getHeaders();
			arr.add(listHeader);
			while(creader.readRecord())
			{
				line = creader.getValues();
				//�жϸ����Ƿ���У���ȱ�Ρ���ȱ����һ���жϣ�ȱ�������һ��
				if(line.length != listHeader.length)
				{
					//logger
					continue;
				}
				//�жϲ����Ƿ��п��ַ���
				for(i=0;i<listHeader.length;i++)
				{
					if(line[i] == "")
					{
						//logger
						break;
					}
				}
				//�Ƿ�ͨ������ɸѡ
				if(i == listHeader.length)
				{
					arr.add(line);
				}
			}
		} 
		catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					//throw new RuntimeException("csv�ļ�������:"+from);
			System.out.println("csv�ļ�������:"+from);
			System.exit(1);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//throw new RuntimeException("CSVReader���ݶ�ȡ����");
			System.out.println("CSVReader���ݶ�ȡ����");
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
					//throw new RuntimeException("InputStream�ر�ʧ��");
					System.out.println("InputStream�ر�ʧ��");
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
		//ֻ��ȡ�ļ���·��
		if(!file.exists()){
			//throw new RuntimeException("·��������:"+to);
			System.out.println("·��������:"+to);
			System.exit(1);
		}
		if(file.isDirectory())
		{
			file = new File(to,fileName);
			try {
				if(file.exists())
				{
					//throw new RuntimeException("ͬ��xml�ļ��Ѵ��ڣ������·��:"+to);
					System.out.println("ͬ��xml�ļ��Ѵ��ڣ������·��:"+to);
					System.exit(1);
				}
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//throw new RuntimeException("xml�ļ���������:"+to);
				System.out.println("xml�ļ���������:"+to);
				System.exit(1);
			}
		}
		else if(file.isFile())
		{
			
			//throw new RuntimeException("ͬ��xml�ļ��Ѵ��ڣ������·��:"+to);
			System.out.println("ͬ��xml�ļ��Ѵ��ڣ������·��:"+to);
			System.exit(1);
		}
		else{
			//throw new RuntimeException("·��������:"+to);
			System.out.println("·��������:"+to);
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
			//throw new RuntimeException("xml�ļ�д��ʧ��");
			System.out.println("xml�ļ�д��ʧ��");
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
					//throw new RuntimeException("OutputStream�ر�ʧ��");
					System.out.println("OutputStream�ر�ʧ��");
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
