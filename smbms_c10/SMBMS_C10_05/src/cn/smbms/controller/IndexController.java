package cn.smbms.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.smbms.pojo.User;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.Constants;

@Controller
public class IndexController{
	private Logger logger = Logger.getLogger(IndexController.class);

	@Resource
	private UserService userService;
	@RequestMapping("/login")
	public String main(HttpSession session){
		User u = (User) session.getAttribute(Constants.USER_SESSION);
		if(u!=null){
			return "frame";
		}else{
		    return "login";
		}
	}
	
	
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String login(@RequestParam("userCode") String userCode,
			@RequestParam("userPassword") String userPassword,
			HttpServletRequest request, HttpSession session) {
		User u = userService.login(userCode, userPassword);
		if (u != null) {
			// 正确的页面
			session.setAttribute(Constants.USER_SESSION, u);
			return "redirect:/user/index";
		} else {
			// 重新登录
			request.setAttribute("error", "用户名或者密码错误");
			// return "login";
			throw new RuntimeException("用户名或者密码错误");
		}
	}
	
	
	
	/*
	 * 参数传递：view to controller
	 */
	
/*	@RequestMapping("/welcome")
	public String welcome(String username){
		logger.info("welcome, " + username);
		return "index";
	}*/
	
	@RequestMapping("/welcome")
	public String welcome(@RequestParam(value="username",required=false) String username){
			logger.info("welcome," + username);
			return "index";
		}


	/*@RequestMapping(value="/welcome",method=RequestMethod.GET,params="username")
	public String welcome(String username){
		logger.info("welcome, " + username);
		return "index";
	}*/
	
	/**
	 * 参数传递：controller to view -(ModelAndView)
	 * @param username
	 * @return
	 */
	@RequestMapping("/index1")
	public ModelAndView index(String username){
		logger.info("welcome! username: " + username);
		ModelAndView mView = new ModelAndView();
		mView.addObject("username", username);
		mView.setViewName("index");
		return mView;
	}
	
	/**
	 * 参数传递：controller to view -(Model)
	 * @param username
	 * @param model
	 * @return
	 */
	@RequestMapping("/index2")
	public String index(String username,Model model){
		logger.info("hello,SpringMVC! username: " + username);
		model.addAttribute("username", username);
		/**
		 * 默认使用对象的类型作为key：
		 * model.addAttribute("string", username)
		 * model.addAttribute("user", new User())
		 */
		model.addAttribute(username);
		User user = new User();
		user.setUserName(username);
		model.addAttribute("currentUser", user);
		model.addAttribute(user);
		return "index";
	}
	
	/**
	 * 参数传递：controller to view -(Map<String,Object>)
	 * @param username
	 * @param model
	 * @return
	 */
	@RequestMapping("/index3")
	public String index(String username,Map<String, Object> model){
		logger.info("hello,SpringMVC! username: " + username);
		model.put("username", username);
		return "index";
	}
	
}
