package cn.smbms.controller;


import java.util.Date;
import java.text.SimpleDateFormat;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;


public class BaseController {
	
	@InitBinder
	public void inItBinder(WebDataBinder wdb){
		wdb.registerCustomEditor(Date.class,new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),true));
	}
	
}
