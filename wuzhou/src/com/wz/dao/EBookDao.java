package com.wz.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.wz.entity.BookOnlineEntity;
import com.wz.entity.BookOnlineOSEntity;
import com.wz.entity.ConfigEntity;
import com.wz.hibernate.HibernateUtil;

public class EBookDao extends BaseDao {
	private Session session;
	private Query query;
	
	
	/**
	 * 电子书上线平台
	 * @return
	 */
	public List<BookOnlineOSEntity> getBookOnlineOS() throws Exception{
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery("from BookOnlineOSEntity where os_flag = 0 order by os_id"); //默认启用状态是0
			List<BookOnlineOSEntity> list = query.list();
			if(list!=null&&list.size()>0) {
				return list;
			} else {
				return null;
			}
		} catch (HibernateException ex) {
			throw ex;
		}
	}
	
	/**
	 * 添加电子书平台
	 * @param bookOnlineOSEntity
	 * @return
	 * @throws Exception
	 */
	public boolean addEBookOS(BookOnlineOSEntity bookOnlineOSEntity) throws Exception {
		Transaction tr = null;
		try {
			session = HibernateUtil.getSession();
			tr = session.beginTransaction();
			session.save(bookOnlineOSEntity);
			tr.commit();
			return true;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			tr.rollback();
			throw ex;
		}
	}
	
	/**
	 * 删除
	 * @param osId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEBookOS(Integer osId) throws Exception {
		Transaction tr = null;
		try {
			session = HibernateUtil.getSession();
			tr = session.beginTransaction();
			BookOnlineOSEntity os = (BookOnlineOSEntity)session.load(BookOnlineOSEntity.class, osId);
			session.delete(os);
			tr.commit();
			return true;
		} catch (HibernateException ex) {
			tr.rollback();
			throw ex;
		}
	}
	
	public boolean deleteEBookOsByBookId(int bookId) {
		Transaction tr = null;
		try {
			session = HibernateUtil.getSession();
			tr = session.beginTransaction();
			String sql = "delete from wz_book_online where book_id = " + bookId;
			session = HibernateUtil.getSession();
			tr = session.beginTransaction();
			session.createSQLQuery(sql).executeUpdate();
			tr.commit();
			return true;
		} catch (HibernateException ex) {
			tr.rollback();
			throw ex;
		}
	}
	
	/**
	 * 更新
	 * @param bookOnlineOSEntity
	 * @return
	 * @throws Exception
	 */
	public boolean updateEBookOS(BookOnlineOSEntity bookOnlineOSEntity) throws Exception {
		Transaction tr = null;
		try {
			session = HibernateUtil.getSession();
			tr = session.beginTransaction();
			session.merge(bookOnlineOSEntity);
			tr.commit();
			return true;
		} catch (HibernateException ex) {
			tr.rollback();
			throw ex;
		}
	}
	
	public List<BookOnlineEntity> getBookOnline(String bookIds) throws Exception {
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery("from BookOnlineEntity where book_id in (" + bookIds +")");
			List<BookOnlineEntity> list = query.list();
			if(list!=null&&list.size()>0) {
				return list;
			} else {
				return null;
			}
		} catch (HibernateException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
	
	/**
	 * 根据图书ID 获取所有平台列表
	 * @param bookId
	 * @return
	 * @throws Exception
	 */
	public List<BookOnlineEntity> getBookOnlineByBookId(int bookId) throws Exception {
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery("from BookOnlineEntity where book_id = " + bookId);
			List<BookOnlineEntity> list = query.list();
			if(list!=null&&list.size()>0) {
				return list;
			} else {
				return null;
			}
		} catch (HibernateException ex) {
			throw ex;
		}
	}
	
	public BookOnlineEntity getBookOnlineByBookIdAndOsId(int bookId, int osId) throws Exception {
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery("from BookOnlineEntity where book_id = " + bookId + " and os_id = " + osId);
			List<BookOnlineEntity> list = query.list();
			if(list!=null&&list.size()>0) {
				return list.get(0);
			} else {
				return null;
			}
		} catch (HibernateException ex) {
			throw ex;
		}
	}
	
	public int updateBookOnline(String sql) {
		Transaction tran = null;
		try{
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
	
	public void saveOrUpdateBookOnline(BookOnlineEntity boe) {
		Transaction tran = null;
		try{
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			session.merge(boe);
			session.flush();
			tran.commit();
		} catch (HibernateException ex) {
			tran.rollback();
			throw ex;
		}
	}
	public boolean deleteEBookOSByBookOnlineEntity(BookOnlineEntity boe) throws Exception {
		Transaction tr = null;
		try {
			session = HibernateUtil.getSession();
			tr = session.beginTransaction();
			session.delete(boe);
			session.flush();
			tr.commit();
			return true;
		} catch (HibernateException ex) {
			tr.rollback();
			throw ex;
		}
	}
	public List<BookOnlineEntity> getOnlineBookByHql(String hql) {
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery(hql);
			List<BookOnlineEntity> list = query.list();
			return list;
		} catch (HibernateException ex) {
			throw ex;
		}
	}
}
