package com.wz.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory = null;
	private static ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	private static Session session;
	// private static SessionFactory sessionFactory;
	// public static Session currentSession() throws HibernateException {
	// Session s = (Session) session.get();
	// // Open a new Session, if this Thread has none yet
	// if (s == null) {
	// s = sessionFactory.openSession();
	// session.set(s);
	// }
	// return s;
	// }
	//
	// public static void closeSession() throws HibernateException {
	// Session s = (Session) session.get();
	// session.set(null);
	// if (s != null)
	// s.close();
	// }

	static{
        try {
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
            System.out.println("创建sesionFactory成功");
        } catch (Exception ex) {
        	throw new RuntimeException("创建sesionFactory出错 : " + ex.getMessage(), ex);
        }
    }
	public static Session getSession() {
		session = threadLocal.get();
        if (session == null || !session.isOpen()) {
            session = sessionFactory.getCurrentSession();
        }
        threadLocal.set(session);
        return session;
	}
}
