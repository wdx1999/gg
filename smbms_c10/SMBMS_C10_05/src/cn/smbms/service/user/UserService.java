package cn.smbms.service.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;

public interface UserService {

	public User login(String userCode, String userPassword);
	
	public int getUserCount(String userName,int userRole);
	public List<Role> queryRoleList();
	
	public List<User> getUserList(String userName,
			int userRole,int currentPageNo,int pageSize);
	
	public boolean add(User user);
	public User getUserById(int id); 
	public boolean modify(User user);
	public boolean getUserByUserCode(String userCode);
	public int deleteUserById(Integer delId);
	public int updatePwd(int id,String pwd);
}
