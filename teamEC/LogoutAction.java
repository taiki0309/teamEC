package com.internousdev.galaxy.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class LogoutAction extends ActionSupport implements SessionAware{
		private Map<String,Object>session;

		public String execute() {
				String userId = String.valueOf(session.get("userId"));
				String tempIdFlg = String.valueOf(session.get("idFlg"));
				boolean idFlg = "null".equals(tempIdFlg)?false:Boolean.valueOf(tempIdFlg);
				session.clear();
				if(idFlg) {
					session.put("idFlg",idFlg);
					session.put("userId",userId);
				}

		return SUCCESS;

		}

		public Map<String,Object>getSession(){
				return session;
		}
		public void setSession(Map<String,Object>session) {
				this.session=session;
		}
}
