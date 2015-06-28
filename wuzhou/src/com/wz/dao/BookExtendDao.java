package com.wz.dao;

import com.wz.entity.BookExtendEntity;
import com.wz.hibernate.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by wanghonghui on 2015/6/29.
 */
public class BookExtendDao extends BaseDao {
    private Session session;
    private Query query;

    /**
     * 查询所有
     * @param hql
     * @return
     */
    public List<BookExtendEntity> getBookListByHql(String hql) throws HibernateException {
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
    public List<BookExtendEntity> findPageByHqlFromBook(int currentPage, int pageRowCount, String hql) throws Exception {
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
     * @param bee
     * @return
     */
    public int updateBook(BookExtendEntity bee)  throws Exception{
        Transaction tran = null;
        try {
            session = HibernateUtil.getSession();
            tran = session.beginTransaction();
            session.merge(bee);
            tran.commit();
            return 1;
        } catch(HibernateException ex) {
            tran.rollback();
            throw ex;
        }
    }

    public int saveBook(BookExtendEntity bee)  throws Exception{
        Transaction tran = null;
        try {
            session = HibernateUtil.getSession();
            tran = session.beginTransaction();
            session.save(bee);
            tran.commit();
            return 1;
        } catch(HibernateException ex) {
            tran.rollback();
            throw ex;
        }
    }

    /**
     * 根据图书ID获取图书信息
     * @param bookId
     * @return
     */
    public BookExtendEntity getBookEntityById(Integer bookId)  throws Exception{
        try {
            session = HibernateUtil.getSession();
            session.beginTransaction();
            query = session.createQuery("from BookExtendEntity where book_id = ?");
            query.setInteger(0, bookId);
            List list = query.list();
            if(list!=null&&list.size()>0) return (BookExtendEntity)list.get(0);
        } catch(Exception ex) {
            throw ex;
        }
        return null;
    }

}
