package cn.smbms.service.user;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.smbms.mapper.RoleDao;
import cn.smbms.mapper.UserDao;
import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;
	
	@Resource
	private RoleDao roleDao;
	@Override
	public User login(String userCode, String userPassword) {
		// TODO Auto-generated method stub
		return userDao.login(userCode, userPassword);
	}
	@Override
	public int getUserCount(String userName, int userRole) {
		
		return userDao.getUserCount(userName, userRole);
	}
	@Override
	public List<Role> queryRoleList() {
		// TODO Auto-generated method stub
		return roleDao.queryRoleList();
	}
	@Override
	public List<User> getUserList(String userName, int userRole,
			int currentPageNo, int pageSize) {
		// TODO Auto-generated method stub
		return userDao.getUserList(userName, userRole, (currentPageNo-1)*pageSize, pageSize);
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean add(User user) {
		try {
			userDao.add(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public User getUserById(int id) {
		// TODO Auto-generated method stub
		return userDao.getUserById(id);
	}
	@Override
	public boolean modify(User user) {
		// TODO Auto-generated method stub
		try {
			userDao.modify(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public boolean getUserByUserCode(String userCode) {
		// TODO Auto-generated method stub
		User u = userDao.getUserByUserCode(userCode);
		if(u!=null && u.getUserCode()!=null ){
			//用户不能为空并且UserCode有值，则userCode已存在
			return false;
		}else{
			return true;
		}
		
		
	}
	@Override
	public int deleteUserById(Integer delId) {
		int u = userDao.deleteUserById(delId);
		
		return u;
	}
	
	@Override
	public int updatePwd(int id, String pwd) {
		// TODO Auto-generated method stub
		return 0;
	}

}
