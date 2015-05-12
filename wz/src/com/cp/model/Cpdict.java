package com.cp.model;

import java.util.ArrayList;
import java.util.List;

import com.cp.util.StringUtil;
import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public class Cpdict extends Model<Cpdict> {
	public static Cpdict dictDao = new Cpdict();
	
	public List<Cpdict> search(String dictIds, String keyWords, int columnNum) {
		if(StringUtil.isBlank(dictIds)) return null;
		if(StringUtil.isBlank(keyWords)) return null;
		keyWords = keyWords.replaceAll("[^\u4E00-\u9FA5 +#]+","");
		String entryCon = " entry = '"+keyWords+"' "; //默认检索
		if(keyWords.indexOf("+")>-1 || keyWords.indexOf("#")>-1) { //模糊检索
			entryCon = " entry like '"+keyWords.replace("#", "_").replace("+", "%")+"' ";
		}
		dictIds = StringUtil.ignoreComma(dictIds);
		int len = dictIds.split(",").length;
		String sql = "";
		if(columnNum==0) {
			if(len==1)  {
				sql = "select srctb.dict_id, srctb.entry_content from cpdict as srctb inner join cpdict as temp on temp.entry_id = srctb.entry_id and srctb."+entryCon+" and temp.dict_id = "+dictIds+" order by srctb.entry_len,convert(srctb.entry using GBK),srctb.entry_id";
			} else {
				sql = "select srctb.dict_id, srctb.entry_content from cpdict as srctb inner join cpdict as temp on temp.entry_id = srctb.entry_id and srctb."+entryCon+" and temp.dict_id in ("+dictIds+") order by srctb.entry_len,convert(srctb.entry using GBK),srctb.entry_id";
			}
		} else {
			if(len==1)
				sql = "select dict_id, entry_content from cpdict where dict_id = "+dictIds+" and match(entry_content) against ('"+keyWords+"' in boolean mode)";
			else {
				dictIds = dictIds.replace(",", " or dict_id = ");
				sql = "select dict_id, entry_content from cpdict where (dict_id = "+dictIds+") and match(entry_content) against ('"+keyWords+"' in boolean mode)";
			}
		}
		return this.find(sql);
	}
	
	
	public void tt() {
		String sql = "select entry from cpdict";
		List<Cpdict> list = this.find(sql);
		for(Cpdict cp : list) {
			String entry = cp.getStr("entry");
			entry = entry.replaceAll("[\u4300-\u9fa5]", "").trim();
			if(entry.length()>0&&entry.contains("&")) {
				System.out.println("["+entry+"]");
			}
		}
		
	}
	
	
}
