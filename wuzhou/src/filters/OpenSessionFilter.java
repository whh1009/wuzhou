package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.wz.hibernate.HibernateUtil;

public class OpenSessionFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("OpenSessionFilter destroy");
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("OpenSessionFilter doFilter");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		new HibernateUtil();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
