package org.bdi.converter;

import java.nio.charset.Charset;

public class ConverterMain {   

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length == 2){
			String csv_dir =  args[0];
			String xml_dir = args[1];
			ConvertControll ctrl = new ConvertControll();
			ctrl.convertControll(csv_dir, xml_dir, Charset.forName("GBK"));
			System.out.println("\nweb.xml生成完毕\n");
		}
		else{
			System.out.println("\nUsage:java Converter [CSV_FILE_DIR] [XML_FILE_DIR] \n");
			//throw new RuntimeException("\nUsage:java Converter [CSV_FILE_DIR] [XML_FILE_DIR] \n");
		}
//		System.out.println("hello");
//		String [] s = {"1","2","233"};
		//System.out.println(s[10]);
//		String from = "C:/Users/hongyu/Desktop/学习/Converter/InitParam.csv";
//		String to = "C:/Users/hongyu/Des";
//		ConvertControll ctrl = new ConvertControll();
//		ctrl.convertControll(from, to, Charset.forName("GBK"));
	}
}
