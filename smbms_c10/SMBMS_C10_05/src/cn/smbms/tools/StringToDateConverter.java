package cn.smbms.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class StringToDateConverter implements Converter<String, Date> {
	/*
	 * 1.自定义日期转换器类StringToDateConverter implements Converter<String, Date>
	 * 2.重写 convert(S):T SimpleDateFormat().parse(source);
	 * 3.在springmvc-servlet.xml只能配置组件ConversionServiceFactory
	 * 为conveters属性注入内部bean(StringToDateConverter)
	 * 4.在<mvc:annotation-driven conversion-service="myConversionService">
	 */
	private String dateFormat;
	
	public StringToDateConverter(String dateFormat){
		this.dateFormat = dateFormat;
		System.out.println("-------------->dateformat"+dateFormat);
	}

	@Override
	public Date convert(String s) {
		// TODO Auto-generated method stub
		Date date = null;
		try {
			date = new SimpleDateFormat(dateFormat).parse(s);
			System.out.println("----------> StringToDateConverter convert date:"+date);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return date;
	}

}
