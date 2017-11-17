package cn.smbms.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.smbms.pojo.User;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.Constants;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	private Logger logger = Logger.getLogger(UserController.class);

	@Resource
	private UserService userService;

	@RequestMapping({ "/welcome", "/" })
	public String welcome(String username) {
		logger.info("welcome, username:" + username);
		return "index";
	}

	private ArrayList<User> userList = new ArrayList<User>();
	private ArrayList<User> queryUserList = new ArrayList<User>();

	public UserController() {
		try {
			userList.add(new User(1, "test001", "测试用户001", "1111111", 1,
					new SimpleDateFormat("yyyy-MM-dd").parse("1986-12-10"),
					"13566669998", "北京市朝阳区北苑", 1, 1, new Date(), 1, new Date()));
			userList.add(new User(2, "zhaoyan", "赵燕", "2222222", 1,
					new SimpleDateFormat("yyyy-MM-dd").parse("1984-11-10"),
					"18678786545", "北京市海淀区成府路", 1, 1, new Date(), 1, new Date()));
			userList.add(new User(3, "test003", "测试用户003", "3333333", 1,
					new SimpleDateFormat("yyyy-MM-dd").parse("1980-11-11"),
					"13121334531", "北京市通州北苑", 1, 1, new Date(), 1, new Date()));
			userList.add(new User(4, "wanglin", "王林", "4444444", 1,
					new SimpleDateFormat("yyyy-MM-dd").parse("1989-09-10"),
					"18965652364", "北京市学院路", 1, 1, new Date(), 1, new Date()));
			userList.add(new User(5, "zhaojing", "赵静", "5555555", 1,
					new SimpleDateFormat("yyyy-MM-dd").parse("1981-08-01"),
					"13054784445", "北京市广安门", 1, 1, new Date(), 1, new Date()));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	// 没有查询条件的情况下，获取userList(公共查询)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		logger.info("无查询条件下，获取userList(公共查询)==== userList");
		model.addAttribute("queryUserList", userList);
		return "user/userlist";
	}

	// 增加查询条件：userName
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public String list(
			@RequestParam(value = "userName", required = false) String userName,
			Model model) {
		logger.info("查询条件：userName: " + userName + ", 获取userList==== ");
		if (userName != null && !userName.equals("")) {
			for (User user : userList) {
				if (user.getUserName().indexOf(userName) != -1) {
					queryUserList.add(user);
				}
			}
			model.addAttribute("queryUserList", queryUserList);
		} else {
			model.addAttribute("queryUserList", userList);
		}
		return "user/userlist";
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
	@RequestMapping("/zhux")
	public String zhux(HttpServletRequest request){
		request.getSession().setAttribute(Constants.USER_SESSION, null);
		return "login";
	}

	@ExceptionHandler
	public String handlerException(Exception e, HttpServletRequest request) {
		request.setAttribute("error", e);
		return "error";
	}

	@RequestMapping("/index")
	public String main(HttpSession session) {
		User u = (User) session.getAttribute(Constants.USER_SESSION);
		if (u != null) {
			return "frame";
		} else {
			return "login";
		}
	}

	@RequestMapping("/userlist.html")
	public String queryUserList(
			Model model,
			@RequestParam(value = "queryname", required = false) String queryname,
			@RequestParam(value = "queryUserRole", required = false) String queryUserRole,
			@RequestParam(value = "pageIndex", required = false) String pageIndex) {

		// 角色id
		int roleId = 0;
		// 当前页
		int currentPageNo = 1;

		if (null != queryUserRole && !("").equals(queryUserRole)) {
			roleId = Integer.parseInt(queryUserRole);
		}
		if (null != pageIndex && !("").equals(pageIndex)) {
			currentPageNo = Integer.parseInt(pageIndex);
		}
		// 总条数
		int totalNum = userService.getUserCount(queryname, roleId);
		// 总页数
		int totalPage = (totalNum % Constants.pageSize) == 0 ? totalNum
				/ Constants.pageSize : (totalNum / Constants.pageSize + 1);

		model.addAttribute("totalCount", totalNum);
		model.addAttribute("currentPageNo", currentPageNo);
		model.addAttribute("totalPageCount", totalPage);
		model.addAttribute("queryUserName", queryname);
		model.addAttribute("queryUserRole", queryUserRole);
		model.addAttribute("roleList", userService.queryRoleList());
		model.addAttribute("userList", userService.getUserList(queryname,
				roleId, currentPageNo, Constants.pageSize));
		// DispatcherServler
		return "userlist";
	}

	@RequestMapping("/useradd.html")
	public String addView() {
		return "useradd";
	}

	// @RequestMapping("/useraddsave.html")
	// public String addUser(User user,HttpSession session,HttpServletRequest
	// request,
	// @RequestParam(value="a_idPicPath",required=false) MultipartFile attach){
	// User u = (User) session.getAttribute(Constants.USER_SESSION);
	// String idPicPath = null;
	// if(!attach.isEmpty()){
	// String path =
	// request.getSession().getServletContext().getRealPath("statics"+File.separator+"image");
	// logger.info("uploadFile path =============>"+path);
	// String oldFileName = attach.getOriginalFilename();//原文件名
	// logger.info("uploadFile oldFileName =======>"+oldFileName);
	// String prefix = FilenameUtils.getExtension(oldFileName);
	// logger.info("uploadFile prefix ===========>"+prefix);
	// int filesize = 5000000;
	// logger.info("uploadFile filesize =========>"+filesize);
	// if(attach.getSize() > filesize){
	// request.setAttribute("uploadFileError", "* 上传文件大小不得超过500KB");
	// return "useradd";
	// }else if(prefix.equalsIgnoreCase("jpg")
	// ||prefix.equalsIgnoreCase("jpeg")
	// ||prefix.equalsIgnoreCase("png")
	// ||prefix.equalsIgnoreCase("pneg")){
	// String fileName = System.currentTimeMillis()+"_personal.jsp";
	// logger.debug("new fileName ========="+attach.getName());
	// File targetFile = new File(path,fileName);
	// if(!targetFile.exists()){
	// targetFile.mkdirs();
	// }
	// try {
	// attach.transferTo(targetFile);
	// } catch (Exception e) {
	// e.printStackTrace();
	// request.setAttribute("uploadFileError", "* 上传文件失败！");
	// return "useradd";
	// }
	// idPicPath=path+File.separator+fileName;
	// user.setIdPicPath(idPicPath);
	// logger.debug(idPicPath+idPicPath.length());
	// }else{
	// request.setAttribute("uploadFileError", "* 上传图片格式不正确");
	// return "useradd";
	// }
	//
	// }
	// if(u!=null&&u.getId()!=0){
	// user.setCreatedBy(u.getId());
	// user.setCreationDate(new Date());
	// if(userService.add(user)){
	// return "redirect:/user/userlist.html";
	// }else{
	// return "redirect:/user/useradd.html";
	// }
	// }else{
	// request.setAttribute("error","请输入用户名和密码.");
	// return "redirect:/user/index";
	// }
	// }

	@RequestMapping("/useraddsave.html")
	public String addUser(
			User user,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "attachs", required = false) MultipartFile[] attachs) {
		User u = (User) session.getAttribute(Constants.USER_SESSION);
		String idPicPath = null;
		String workPicPath = null;
		boolean flag = true;
		String path = request.getSession().getServletContext()
				.getRealPath("statics" + File.separator + "image");
		for (int i = 0; i < attachs.length; i++) {
			MultipartFile attach = attachs[i];
			String errorName = "";

			logger.debug("I------------------------->" + i);
			if (i == 0) {
				errorName = "uploadFileError";
				logger.debug("idPicPath------------------------->" + idPicPath);
			} else {
				errorName = "uploadWpError";
				logger.debug("workPicPath------------------------->"
						+ workPicPath);
			}
			if (!attach.isEmpty()) {
				logger.info("uploadFile path =============>" + path);
				String oldFileName = attach.getOriginalFilename();// 原文件名
				logger.info("uploadFile oldFileName =======>" + oldFileName);
				String prefix = FilenameUtils.getExtension(oldFileName);
				logger.info("uploadFile prefix ===========>" + prefix);
				int filesize = 5000000;
				logger.info("uploadFile filesize =========>" + filesize);
				if (attach.getSize() > filesize) {
					request.setAttribute(errorName, "* 上传文件不得500KB");
					flag = false;
				} else if (prefix.equalsIgnoreCase("jpg")
						|| prefix.equalsIgnoreCase("jpeg")
						|| prefix.equalsIgnoreCase("png")
						|| prefix.equalsIgnoreCase("pneg")) {
					Random random = new Random();
					String fileName = System.currentTimeMillis()
							+ Math.abs(random.nextInt(10000)) + "_personal.jsp";
					logger.debug("new fileName =========" + attach.getName());
					File targetFile = new File(path, fileName);
					if (!targetFile.exists()) {
						targetFile.mkdirs();
					}
					try {
						attach.transferTo(targetFile);
						logger.debug("Iffff------------------------->" + i);
						if (i == 0) {
							idPicPath = path + File.separator + fileName;
							logger.debug("idPicPathfff------------------------->"
									+ idPicPath);
						} else {
							workPicPath = path + File.separator + fileName;
							logger.debug("workPicPathffff------------------------->"
									+ workPicPath);
						}
					} catch (Exception e) {
						e.printStackTrace();
						request.setAttribute("errorName", "* 上传文件失败！");
						flag = false;

					}
					if (!flag) {
						return "useradd";
					}
					idPicPath = path + File.separator + fileName;
					user.setIdPicPath(idPicPath);
					user.setWorkPicPath(workPicPath);
					logger.debug("------------------------->" + idPicPath);
					logger.debug("------------------------->" + workPicPath);
					logger.debug(idPicPath + idPicPath.length());
				} else {
					request.setAttribute("uploadFileError", "* 上传图片格式不正确");
					flag = false;
				}
			}
		}

		if (u != null && u.getId() != 0) {
			user.setCreatedBy(u.getId());
			user.setCreationDate(new Date());
			if (userService.add(user)) {
				return "redirect:/user/userlist.html";
			} else {
				return "redirect:/user/useradd.html";
			}
		} else {
			request.setAttribute("error", "请输入用户名和密码.");
			return "redirect:/user/index";
		}
	}

	@RequestMapping(value = "/useradd", method = RequestMethod.GET)
	public String addJsp(User user) {
		return "/user/useradd";
	}

	// 可以没有action 提交的地址是访问的该页面的url /user/useradd
	@RequestMapping(value = "/useradd", method = RequestMethod.POST)
	public String addUser1(@Valid User user, BindingResult bindingResult,
			HttpSession session) {
		System.out.println(user.toString());
		if (bindingResult.hasErrors()) {
			System.out.println("------------->error!");
			return "/user/useradd";
		} else {
			User u = (User) session.getAttribute(Constants.USER_SESSION);
			if (u != null && u.getId() != 0) {
				user.setCreatedBy(u.getId());
				user.setCreationDate(new Date());
				if (userService.add(user)) {
					return "redirect:/user/userlist.html";
				} else {
					return "redirect:/user/useradd.html";
				}
			} else {
				return "redirect:/user/index";
			}
		}
	}

	@RequestMapping("/getUserById/{id}")
	public String getUserById(Model model, @PathVariable int id) {
		User user = userService.getUserById(id);
		model.addAttribute("user", user);
		return "usermodify";
	}

	@RequestMapping("/usermodify")
	public String getUserById() {
		return "usermodify";
	}

	@RequestMapping("/usermodifysave.html")
	public String modify(User user) {
		if (userService.modify(user)) {
			return "redirect:/user/userlist.html";
		} else {
			return "redircet:/user/getUserById" + user.getId();
		}
	}

	@RequestMapping("/ucexist")
	@ResponseBody
	public Object ucexist(String userCode) {
		boolean flag = userService.getUserByUserCode(userCode);
		Map<String, String> map = new HashMap<String, String>();
		if (flag) {
			map.put("userCode", "noexist");
		} else {
			map.put("userCode", "exist");
		}

		return JSONArray.toJSONString(map);
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object view(int id) {
		User user = userService.getUserById(id);
		logger.debug("userrole------------->" + user.getUserRole());
		logger.debug("userCode------------->" + user.getUserCode());
		return JSON.toJSONString(user);
	}

	@RequestMapping("login.html")
	public String login() {
		return "redirect:/user/index";
	}

	@RequestMapping("/user/index.html")
	public String index() {
		return "frame";
	}
}
