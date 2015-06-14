package com.wz.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.wz.entity.MenuEntity;
import com.wz.entity.RoleEntity;
import com.wz.entity.RoleMenuEntity;
import com.wz.entity.UREntity;
import com.wz.entity.UserEntity;
import com.wz.entity.UserRoleEntity;
import com.wz.hibernate.HibernateUtil;
import com.wz.util.StringUtil;

public class UserDao extends BaseDao {
	private Session session;
	private Query query;

	/**
	 * 添加用户
	 * 
	 * @param userEntity
	 * @return
	 */
	public UserEntity addUser(UserEntity userEntity) throws Exception{
		Transaction tran = null;
		try {
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			session.save(userEntity);
			tran.commit();
			return userEntity;
		} catch (HibernateException ex) {
			tran.rollback();
			throw ex;
		}
	}
	
	/**
	 * 添加角色
	 * @param roleEntity
	 * @return
	 */
	public int addRole(RoleEntity roleEntity) throws Exception{
		Transaction tran = null;
		try {
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			session.save(roleEntity);
			tran.commit();
			return 1;
		} catch (HibernateException ex) {
			tran.rollback();
			throw ex;
		}
	}

	public int addUserRole(UserRoleEntity userRoleEntity) throws Exception{
		Transaction tran = null;
		try {
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			session.save(userRoleEntity);
			tran.commit();
			return 1;
		} catch (HibernateException ex) {
			tran.rollback();
			throw ex;
		}
	}

	/**
	 * 查询用户信息，返回list
	 * 
	 * @return
	 */
	public List<UserEntity> list() throws Exception{
		try {
			session = HibernateUtil.getSession();
			List<UserEntity> userEntitys = null;
			session.beginTransaction();
			userEntitys = (List<UserEntity>) session.createQuery("from UserEntity  where status = 1 order by user_id desc").list();
			return userEntitys;
		} catch (HibernateException e) {
			throw e;
		}
	}
	
	/**
	 * 查询用户信息，返回list
	 * 
	 * @return
	 */
	public List<UserEntity> getUserList() throws Exception{
		try {
			session = HibernateUtil.getSession();
			List<UserEntity> userEntitys = null;
			session.beginTransaction();
			userEntitys = (List<UserEntity>) session.createQuery("from UserEntity").list();
			return userEntitys;
		} catch (HibernateException e) {
			throw e;
		}
	}

	/**
	 * 登录成功返回true，不成功返回false
	 * 
	 * @param userName
	 *            用户名
	 * @param pwd
	 *            密码
	 * @return
	 */
	public UserEntity checkLogin(String userName, String pwd) throws Exception{
		Transaction tran = null;
		try {
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			query = session.createQuery("from UserEntity where user_name = ? and user_pwd = ? and status = 1");
			query.setString(0, userName);
			query.setString(1, pwd);
			List<UserEntity> list = query.list();
			System.out.println(list.size());
			if (list!=null&&list.size()>0) return list.get(0);
		} catch (HibernateException e) {
			throw e;
		}
		return null;
	}

	/**
	 * 判断用户名是否被使用
	 * 
	 * @param userName
	 * @return 被使用返回1，没有被使用返回0，查询出错返回-1
	 */
	public int checkUserName(String userName) throws Exception{
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery("from UserEntity where user_name = ?");
			query.setString(0, userName);
			List list = query.list();
			if (list!= null&&list.size()>0) return 1;
		} catch (HibernateException ex) {
			throw ex;
		}
		return 0;
	}

	/**
	 * 删除用户
	 * 
	 * @param userId
	 * @return
	 */
	public int deleteUser(Integer userId) throws Exception{
		Transaction tran = null;
		try {
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			UserEntity ue = (UserEntity) session.load(UserEntity.class, userId);
			session.delete(ue);
			tran.commit();
			return 1;
		} catch (HibernateException ex) {
			tran.rollback();
			throw ex;
		}
	}
	
	/**
	 * 删除用户对应的角色关系
	 * @param ure
	 * @return
	 */
	public int deleteUserRoleEntity(Integer urId) throws Exception{
		Transaction tran = null;
		try {
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			UserRoleEntity ure = (UserRoleEntity) session.load(UserRoleEntity.class, urId);
			session.delete(ure);
			tran.commit();
			return 1;
		} catch (HibernateException ex) {
			tran.rollback();
			throw ex;
		}
	}


	/**
	 * 更新用户
	 * 
	 * @param userEntity
	 * @return
	 */
	public int updateUser(UserEntity userEntity) throws Exception{
		Transaction tran = null;
		try {
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			session.merge(userEntity);
			tran.commit();
			return 1;
		} catch (HibernateException ex) {
			tran.rollback();
			throw ex;
		}
	}
	
	/**
	 * 更新用户角色权限表
	 * @param ure
	 * @return
	 */
	public int updateUserRoleEntity(UserRoleEntity ure) throws Exception{
		Transaction tran = null;
		try {
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			session.merge(ure);
			tran.commit();
			return 1;
		} catch (HibernateException ex) {
			tran.rollback();
			throw ex;
		}
	}
	
	/**
	 * 根据用户id获取用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserEntity getUserEntityByUserId(int userId) throws Exception{
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery("from UserEntity where user_id = ?");
			query.setInteger(0, userId);
			List<UserEntity> list = query.list();
			if (list!= null&&list.size()>0) return list.get(0);
		} catch (HibernateException ex) {
			throw ex;
		}
		return null;
	}
	
	/**
	 * 根据角色ID获取角色信息
	 * @param roleId
	 * @return
	 */
	public RoleEntity getRoleEntityByRoleId(int roleId)throws Exception{
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery("from RoleEntity where role_id = ?");
			query.setInteger(0, roleId);
			List<RoleEntity> list = query.list();
			if (list!= null&&list.size()>= 0) return list.get(0);
		} catch (HibernateException ex) {
			throw ex;
		}
		return null;
	}

	/**
	 * 分页查询用户信息
	 * 
	 * @param currentPage
	 *            当前页
	 * @param pageRowCount
	 *            每页显示记录数
	 * @param hql
	 * @return
	 */
	public List<UserEntity> showUserByPage(int currentPage, int pageRowCount) throws Exception{
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			//sql不包含管理员，管理员user_id = 1
			String hql = "from UserEntity where status=1 and user_id != 1 order by user_id desc ";
			query = session.createQuery(hql);
			query.setFirstResult((currentPage - 1) * pageRowCount);
			query.setMaxResults(pageRowCount);
			return query.list();
		} catch (HibernateException ex) {
			throw ex;
		}
	}

	/**
	 * 
	 * @param currentPage
	 * @param pageRowCount
	 * @param sql
	 * @return
	 */
	public List<UREntity> showUrByPage(int currentPage, int pageRowCount) throws Exception{
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			String sql = "select u.user_id, u.user_name, u.user_pwd, u.nick_name, u.email, r.role_id, r.role_name from wz_user u left join wz_user_role ur on u.user_id = ur.user_id left join wz_role r on r.role_id = ur.role_id where u.user_id != 1 order by u.user_id desc ";
			query = session.createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(UREntity.class));
			query.setFirstResult((currentPage - 1) * pageRowCount);
			query.setMaxResults(pageRowCount);
			return query.list();
		} catch (HibernateException ex) {;
			throw ex;
		}
	}

	/**
	 * 获取角色列表
	 * 
	 * @param sql
	 * @return
	 */
	public List<RoleEntity> getRoleList() throws Exception{
		try {
			String hql = "from RoleEntity where status = 1 and role_id !=1 order by role_id desc";
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery(hql);
			return query.list();
		} catch (HibernateException ex) {
			throw ex;
		}
	}

	/**
	 * 根据用户获取角色
	 * 
	 * @param sql
	 * @return
	 */
	public UserRoleEntity getUserRoleByUser(UserEntity userEntity)throws Exception{
		try {
			String hql = "from UserRoleEntity where userEntity.user_id = ?";
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery(hql);
			query.setInteger(0, userEntity.getUser_id());
			List<UserRoleEntity> list = query.list();
			if (list != null && list.size()>0) return list.get(0);
		} catch (HibernateException ex) {
			throw ex;
		}
		return null;
	}

	/**
	 * 根据角色获取菜单
	 * 
	 * @param hql
	 * @return
	 */
	public List<RoleMenuEntity> getMenuListByRoleEntity(RoleEntity roleEntity) throws Exception{
		try {
			String hql = null;
			session = HibernateUtil.getSession();
			session.beginTransaction();
			if(roleEntity==null) {
				hql = "from RoleMenuEntity where roleEntity.role_id != 1";
			}else{
				hql = "from RoleMenuEntity where roleEntity.role_id = " + roleEntity.getRole_id();
			}
			query = session.createQuery(hql);
			return query.list();
		} catch (HibernateException ex) {
			throw ex;
		}
	}

	/**
	 * 判断角色名是否重复
	 * @param hql
	 * @return 存在返回true，不存在返回false
	 */
	public boolean checkRoleName(String hql) throws Exception{
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery(hql);
			List list = query.list();
			if (list!= null&&list.size()>0) return true;
		} catch (HibernateException ex) {
			throw ex;
		}
		return false;
	}

	/**
	 * 获取菜单List
	 * @return
	 */
	public List<MenuEntity> getMenuEntity() throws Exception{
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery("from MenuEntity order by menu_id");
			return query.list();
		} catch (HibernateException ex) {
			throw ex;
		}
	}

	/**
	 * sql删除roleMenu表
	 * @param sql
	 */
	public boolean removeRoleMenuById(int roleId) throws Exception{
		Transaction tran = null;
		try {
			String sql = "delete from wz_role_menu where role_id = " + roleId;
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			session.createSQLQuery(sql).executeUpdate();
			tran.commit();
			return true;
		} catch (HibernateException ex) {
			tran.rollback();
			throw ex;
		}
	}

	/**
	 * 根据roleId添加菜单
	 * @param roleId
	 * @param menuId
	 * @return
	 */
	public boolean addRoleMenu(int roleId, String menuId) throws Exception{
		Transaction tran = null;
		try {
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			for(int i = 0; i < menuId.split(",").length; i++) {
				session.createSQLQuery("insert into wz_role_menu (role_id, menu_id) values ("+new Integer(roleId)+", "+new Integer(StringUtil.StringToInt(menuId.split(",")[i]))+") ").executeUpdate();
			}
			tran.commit();
			return true;
		} catch (HibernateException ex) {
			tran.rollback();
			throw ex;
		}
	}


	public List<UserEntity> getUserListByHql(String hql) {
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery(hql);
			return query.list();
		} catch (HibernateException ex) {
			throw ex;
		}
	}
}
