package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class SetCharacterEncodingFilter implements Filter {
	protected String encoding = null;
	protected FilterConfig filterConfig = null;

	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}

	class Request extends HttpServletRequestWrapper {

		public Request(HttpServletRequest request) {
			super(request);
		}

		// 从 ISO 字符转到 配置中的字符.
		public String toChi(String input) {
			try {
				if(input == null) return "";
				if("ISO-8859-1".equals(getHttpServletRequest().getCharacterEncoding().toUpperCase())){
					return new String(input.getBytes("ISO-8859-1"), encoding);
				}else if("GBK".equals(getHttpServletRequest().getCharacterEncoding().toUpperCase())){
					return new String(input.getBytes("GBK"), encoding);
				}else{
					return input;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		private HttpServletRequest getHttpServletRequest() {
			return (HttpServletRequest) super.getRequest();
		}

		public String getParameter(String name) {
			return toChi(getHttpServletRequest().getParameter(name));
		}

		public String[] getParameterValues(String name) {
			String values[] = getHttpServletRequest().getParameterValues(name);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					values[i] = toChi(values[i]);
				}
			}
			return values;
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (encoding != null) {
			request.setCharacterEncoding(encoding);
		} 
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.encoding = filterConfig.getInitParameter("encoding");
	}
}
