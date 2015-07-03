package com.wz.dao;

import com.wz.entity.BookEntity;
import com.wz.entity.BookLanDoughnutChartEntity;
import com.wz.hibernate.HibernateUtil;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import java.math.BigInteger;
import java.util.List;

public class BookDao extends BaseDao {
	private Session session;
	private Query query;
	
	/**
	 * 查询所有
	 * @param hql
	 * @return
	 */
	public List<BookEntity> getBookListByHql(String hql) throws HibernateException{ 
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery(hql);
			return query.list();
		} catch(HibernateException ex) {
			throw ex;
		}
	}
	
	/**
	 * 分页显示符合所有的记录数
	 * @param currentPage 当前页码
	 * @param pageRowCount 每页显示的条数
	 * @param hql 
	 * @return
	 */
	public List<BookEntity> findPageByHqlFromBook(int currentPage, int pageRowCount, String hql) throws Exception {
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery(hql);
			query.setFirstResult((currentPage - 1) * pageRowCount);
			query.setMaxResults(pageRowCount);
			return query.list();
		} catch(HibernateException ex) {
			ex.printStackTrace();
			throw ex;
		} finally{
			session.clear();
		}
	}
	
	/**
	 * 根据hql获取总记录数
	 * @param hql
	 * @return
	 */
	public int getCountByHqlFromBook(String hql)  throws Exception{
		int count = 0;
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			List list = session.createSQLQuery(hql).list();
			if(list!=null) {
				count = ((BigInteger)list.get(0)).intValue();
			}
		} catch(Exception ex) {
			throw ex;
		} 
		return count;
	}
	
	/**
	 * 保存图书
	 * @param bookEntity
	 * @return
	 */
	public int updateBook(BookEntity bookEntity)  throws Exception{
		Transaction tran = null;
		try {
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			session.merge(bookEntity);
			tran.commit();
			return 1;
		} catch(HibernateException ex) {
			tran.rollback();
			throw ex;
		}
	}

	/**
	 * 添加--新书发稿、设计、印制
	 * @param bookEntity
	 * @return 1--添加成功 0--添加失败
	 */
	public int addBook(BookEntity bookEntity)  throws Exception{
		Transaction tran = null;
		try {
			session = HibernateUtil.getSession();
			String code = bookEntity.getBook_serial_number();
			tran = session.beginTransaction();
			query = session.createQuery("from BookEntity where book_serial_number = ?");
			query.setString(0, code);
			List list = query.list();
			if(list==null||list.isEmpty()){
				session.save(bookEntity);
				session.clear();
			}else{
				BookEntity oldbookEntity = (BookEntity)list.get(0);
				bookEntity.setBook_id(oldbookEntity.getBook_id());
				session.merge(bookEntity);
			}
			tran.commit();
	        return 1;
		} catch(HibernateException ex) {
			tran.rollback();
			throw ex;
		} 
	}
	
	/**
	 * 添加“其他”新书
	 * @param bsn
	 * @return
	 */
	public int addBookByBsn(String bsn, int userId)  throws Exception{
		Transaction tran = null;
		try {
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			int flag = session.createSQLQuery("insert into wz_book(book_serial_number, user_id) values('"+bsn+"', "+userId+")").executeUpdate();
			tran.commit();
			return flag;
		} catch(HibernateException ex) {
			tran.rollback();
			throw ex;
		} 
	}
	
	/**
	 * 删除图书
	 * @param bookId
	 * @return 成功返回1，失败返回0
	 */
	public int deleteBook(Integer bookId)  throws Exception{
		Transaction tran = null;
		try {
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			BookEntity bookEntity = (BookEntity)session.load(BookEntity.class, bookId);
			session.delete(bookEntity);
			tran.commit();
			return 1;
		} catch (HibernateException ex) {
			tran.rollback();
			throw ex;
		} 
	}
	
	/**
	 * 根据图书ID获取图书信息
	 * @param bookId
	 * @return
	 */
	public BookEntity getBookEntityById(Integer bookId)  throws Exception{
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery("from BookEntity where book_id = ?");
			query.setInteger(0, bookId);
			List list = query.list();
			if(list!=null&&list.size()>0) return (BookEntity)list.get(0);
 		} catch(Exception ex) {
 			throw ex;
		}
 		return null;
	}
	
	
	public BookEntity getBookPathByIsbn(String isbn) throws Exception {
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery("from BookEntity where book_isbn = ? and book_del_flag = 0");
			query.setString(0, isbn);
			List<BookEntity> list = query.list();
			if(list!=null&&list.size()==1) return list.get(0);
 		} catch(Exception ex) {
 			throw ex;
		}
 		return null;
	}
	
	public BookEntity getBookEntityBySql(String sql) throws Exception{
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			query = session.createQuery(sql);
			List<BookEntity> list = query.list();
			if(list!=null&&list.size()==1) return list.get(0);
 		} catch(Exception ex) {
 			throw ex;
		}
 		return null;
	}

	/**
	 * 转为饼状图实体类
	 * @return
	 * @throws Exception
	 */
	public List<BookLanDoughnutChartEntity> getBookLanEntityBySql() throws Exception{
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(BookEntity.class)
					.setProjection(Projections.projectionList()
							.add(Projections.property("book_language"), "book_language")
							.add(Projections.count("book_id"), "bookCount")
							.add(Projections.groupProperty("book_language")))
					.setResultTransformer(Transformers.aliasToBean(BookLanDoughnutChartEntity.class));

			return criteria.list();
		} catch(Exception ex) {
			throw ex;
		}
	}

	/**
	 * 出版时间柱状图实体类
	 * @param time
	 * @return
	 * @throws Exception
	 */
	public List<BookLanDoughnutChartEntity> getBookBarEntityBySql(String time) throws Exception{
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(BookEntity.class);
			criteria.add(Restrictions.like("book_publish_time", time+"%"));
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("book_publish_time"), "book_language")
					.add(Projections.count("book_id"), "bookCount")
					.add(Projections.groupProperty("book_publish_time")))
					.setResultTransformer(Transformers.aliasToBean(BookLanDoughnutChartEntity.class));

			return criteria.list();
		} catch(Exception ex) {
			throw ex;
		}
	}
	
	
	/**
	 * @param sql
	 * @return
	 */
	public int updateBySql(String sql)  throws Exception{
		Transaction tran = null;
		try {
			session = HibernateUtil.getSession();
			tran = session.beginTransaction();
			int flag = session.createSQLQuery(sql).executeUpdate();
			tran.commit();
			return flag;
		} catch(HibernateException ex) {
			tran.rollback();
			throw ex;
		} 
	}
}
