package com.cp.common;

import com.cp.controller.SearchController;
import com.cp.controller.UserController;
import com.cp.model.Cpdict;
import com.cp.model.Dict;
import com.cp.model.User;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;

public class ComConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		loadPropertyFile("db_config.properties");
		me.setViewType(ViewType.JSP);
		me.setDevMode(false);
	}

	@Override
	public void configRoute(Routes me) {
//		me.add("/search", SearchController.class, "/search"); //首页，可自行检索
		me.add("/", SearchController.class, "/search");
		//me.add("/index", SearchController.class, "/search"); //首页，可自行检索
		me.add("/user", UserController.class);
	}

	@Override
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim(), getProperty("driverName"));
		me.add(c3p0Plugin);
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		arp.addMapping("dict", "dict_id", Dict.class);
		arp.addMapping("cpdict", "entry_id", Cpdict.class);
		arp.addMapping("cp_user", "user_id", User.class);
	}

	@Override
	public void configInterceptor(Interceptors me) {
		
	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("ctx"));
		
	}

}
