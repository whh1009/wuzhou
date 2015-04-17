package com.wz.interceptor;

import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.wz.entity.UserEntity;
import com.wz.util.StringUtil;

public class AuthorityInterceptor extends AbstractInterceptor {
	public String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String intercept(ActionInvocation ai) throws Exception {
		Map map = ai.getInvocationContext().getSession();
		if(map==null) { //session没有内容
			return "noLogin";
		}
		UserEntity ue = (UserEntity)map.get("userEntity");
		if(ue==null) {
			return "noLogin";
		}
		String roleMenuXml = StringUtil.ObjectToString(map.get("roleMenuXml"));
		if("".equals(roleMenuXml)) {
			return "noLogin";
		} else {
			//获取当前请求的action
			String actionName = ai.getProxy().getActionName();
			if("addUserPage".equals(actionName)) {//如果有用户列表权限就放行注册用户
				if(roleMenuXml.contains("userList")) {
					return ai.invoke();
				} else {
					return "noLogin";
				}
			}
			if(roleMenuXml.contains("/"+actionName+".action")) {
				System.out.println("可以访问 ："+actionName);
				return ai.invoke();
			} else {
				return "noLogin";
			}
		}
	}

}
