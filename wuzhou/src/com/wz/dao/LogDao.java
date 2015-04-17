package com.wz.dao;

import com.wz.entity.LogEntity;
import com.wz.hibernate.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LogDao extends BaseDao {
	private Session session;
	private Query query;
	
	
	public List<LogEntity> getLogList()throws Exception {
		try {
			String hql = "from LogEntity order by modify_id desc";
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery(hql);
			return query.list();
		} catch(HibernateException ex) {
			throw ex;
		} 
	}
	
	public List<LogEntity> getLogListByCondition(String name,String content)throws Exception {
		try {
			String hql = "from LogEntity where "+name+" like '%"+content.trim()+"%' order by modify_id desc";
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery(hql);
			return query.list();
		} catch(HibernateException ex) {
			throw ex;
		} 
	}
	
	/**
	 * 添加日志文件
	 * @param logEntity
	 */
	public int addLog(LogEntity logEntity) throws Exception{
		Transaction tran = null;
		try {
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			session.save(logEntity);
			tran.commit();
	        return 1;
		} catch(HibernateException ex) {
			tran.rollback();
			throw ex;
		}
	}
	
	/**
	 * 删除日志
	 * @param modify_id
	 * @return
	 */
	public int deleteLog(Integer modify_id) throws Exception{
		Transaction tran = null;
		try {
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			LogEntity logEntity = (LogEntity)session.load(LogEntity.class, modify_id);
			session.delete(logEntity);
			tran.commit();
			return 1;
		} catch (HibernateException ex) {
			tran.rollback();
			throw ex;
		}
	}
}
