package com.cp.interceptor;

import com.cp.model.User;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
/**
 * 拦截器
 * 只有登录过的用户才能访问controller
 * @author wanghonghui
 *
 */
public class LoginInterceptor implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {
		User user = (User)ai.getController().getSessionAttr("user");
		if(user==null) {//没有登录，返回登录页面
			ai.getController().render("/Login.jsp");
		} else {
			ai.invoke();
		}
	}

}
