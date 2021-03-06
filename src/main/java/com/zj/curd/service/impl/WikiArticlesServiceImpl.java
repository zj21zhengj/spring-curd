package com.zj.curd.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zj.curd.controller.WiKiContriller;
import com.zj.curd.dao.WikiArticlesDao;
import com.zj.curd.entity.WkArticles;
import com.zj.curd.entity.WkArticlesauthor;
import com.zj.curd.service.WikiArticlesService;

@Service("wikiArticlesService")
public class WikiArticlesServiceImpl implements WikiArticlesService{
	private static Logger logger = Logger.getLogger(WikiArticlesServiceImpl.class); 
	@Autowired
	private WikiArticlesDao wikiArticlesDao;
	
	@Override
	public PageInfo<WkArticlesauthor> ListArticles(HttpServletRequest request,Integer pn,Integer status) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pn, 5);
		String str_qrys = request.getParameter("qry");
		String tag = request.getParameter("tag");
		List<String> str_qry = null;
		logger.debug("str_qry:"+str_qrys);
		if (str_qrys != null) {
			String[] splited = str_qrys.split("\\s+");//这样写就可以了 多个空格
			str_qry = Arrays.asList(splited);
		}
		List<WkArticlesauthor> wkArticles = wikiArticlesDao.ListArticles(status,str_qry,tag);
		for (WkArticlesauthor wk:wkArticles) {
			String currentUser = (String) request.getSession().getAttribute("user_code");
			String author = wk.getCreateUser();
			//如果当前人员和作者是同一人就能删除
			if(currentUser.equals(author)) {
				wk.setCanDeal(true);
			    wk.setCanModi(true);
			}
			else {
				wk.setCanDeal(false);
				wk.setCanModi(false);
				String canModiUsers = wk.getCanmodiUsers();
				//关于是否有权限编辑的判断
				if (canModiUsers!=null && canModiUsers!="") {
					List<String> modiUserList = Arrays.asList(canModiUsers.split(","));
					if(modiUserList.contains(currentUser))
						wk.setCanModi(true);
				}
			}
		}
		PageInfo page = new PageInfo(wkArticles);
		return page;
	}

	@Override
	public WkArticlesauthor getWkArticle(String artId) {
		// TODO Auto-generated method stub
		
		WkArticlesauthor wkArticles = wikiArticlesDao.getArticleById(artId);
		return wkArticles;
	}
	

	@Override
	public Map saveWkArticle(WkArticles wkArticle) {
		// TODO Auto-generated method stub
		Map<String,Object>  map = new HashMap<>();
		int i = wikiArticlesDao.saveArticle(wkArticle);
		map.put("code", 101);
		map.put("msg", "失败");
		if (i >0) {
		map.put("code", 200);
		map.put("msg", "成功");
		}
		return map;
	}

	@Override
	public Map updateArticle(WkArticles wkArticle) {
		// TODO Auto-generated method stub
		Map<String,Object>  map = new HashMap<>();
		int i = wikiArticlesDao.updateArticle(wkArticle);
		map.put("code", 101);
		map.put("msg", "失败");
		if (i >0) {
		map.put("code", 200);
		map.put("msg", "成功");
		}
		return map;
	}

	@Override
	public Map deleteArticle(String id) {
		Map<String,Object>  map = new HashMap<>();
		int i = wikiArticlesDao.deleteArticle(id);
		map.put("code", 101);
		map.put("msg", "失败");
		if (i >0) {
		map.put("code", 200);
		map.put("msg", "成功");
		}
		return map;
	}

	@Override
	public int updateMathchTime(String artId) {
		// TODO Auto-generated method stub
		int a = wikiArticlesDao.updateMathchTime(artId);
		return a;
	}

	//热门文章
	@Override
	public List<WkArticles> hotWkArticles(Integer status) {
		// TODO Auto-generated method stub
		return wikiArticlesDao.hotWkArticles(status);
	}

	//相关文章
	@Override
	public List<WkArticles> listrelaWkArticles(WkArticles wkArticle) {
		
		List<String> KeywordsList = new ArrayList<String>();
		String artId = wkArticle.getArtId();
		System.out.println("***"+artId);
		if (wkArticle.getArtKeywords() != null) {
			String Keywords = wkArticle.getArtKeywords();
			String[] Keywordsed = Keywords.split("\\s+");//这样写就可以了 多个空格
			KeywordsList = Arrays.asList(Keywordsed);
		}
		
		List<WkArticles> wkArticles = wikiArticlesDao.listrelaWkArticles(0, KeywordsList, artId);
		return wkArticles;
	}

	@Override
	public List<String> getTags(Integer status) {
		// TODO Auto-generated method stub
		List<String> tags = wikiArticlesDao.getTags(0);
		return tags;
	}


	@Override
	public Map<String, Object> saveModiUsers(String artId, String canmodiUsers) {
		Map<String,Object>  map = new HashMap<>();
		int i = wikiArticlesDao.saveModiUsers(artId,canmodiUsers);
		map.put("code", 101);
		map.put("msg", "失败");
		if (i >0) {
		map.put("code", 200);
		map.put("msg", "成功");
		}
		return map;
	}



	

}
