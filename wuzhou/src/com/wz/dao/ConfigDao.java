package com.wz.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.Session;

import com.wz.entity.ConfigEntity;
import com.wz.hibernate.HibernateUtil;
import com.wz.util.StringUtil;

public class ConfigDao extends BaseDao{
	private Session session;
	private Query query;
	
	/**
	 * 获取用户配置
	 * @return
	 */
	public ConfigEntity getConfigByUserId(Integer userId) throws Exception{
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery("from ConfigEntity where user_id = ?");
			query.setInteger(0, userId);
			List<ConfigEntity> list = query.list();
			if(list!=null&&list.size()>0) {
				return list.get(0);
			}
		} catch (HibernateException ex) {
			throw ex;
		}
		return null;
	}
	
	/**
	 * 根据sql更新配置表
	 * @param sql
	 * @return
	 */
	public int addOrUpdateConfig(String column, int userId, String columnValue ) throws Exception{
		Transaction tran = null;
		try{
			ConfigEntity ce = getConfigByUserId(userId);
			String sql;
			if(ce==null) {
				sql = "insert into wz_config (user_id, "+column+"_column) values ("+userId+" , '"+columnValue+"')";
			} else {
				sql = "update wz_config set "+column+"_column = '"+columnValue+"' where user_id = "+userId;
			}
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			int flag = session.createSQLQuery(sql).executeUpdate();
			tran.commit();
			return flag;
		} catch (HibernateException ex) {
			tran.rollback();
			throw ex;
		}
	}
	
}
