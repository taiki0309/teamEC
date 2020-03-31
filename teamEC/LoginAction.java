package com.internousdev.galaxy.action;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.galaxy.dao.CartInfoDAO;
import com.internousdev.galaxy.dao.UserInfoDAO;
import com.internousdev.galaxy.dto.CartInfoDTO;
import com.internousdev.galaxy.util.InputChecker;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport implements SessionAware{
		private String userId;
		private String password;
		private boolean idFlg;
		private List<String>userIdErrorMessageList;
		private List<String>passwordErrorMessageList;
		private String isNotUserInfoMessage;
		private List<CartInfoDTO> cartInfoDTOList;
		private int totalPrice;
		private Map<String,Object>session;

		public String execute() {
				String result = ERROR;
				UserInfoDAO userInfoDAO = new UserInfoDAO();
				session.remove("idFlg");

				if(session.containsKey("createUserFlag")
								&&Integer.parseInt(session.get("createUserFlag").toString())==1) {
						userId = session.get("userIdForCreateUser").toString();

						session.remove("userIdForCreateUser");
						session.remove("createUserFlag");

				}else {
						InputChecker inputChecker = new InputChecker();
						userIdErrorMessageList = inputChecker.doCheck("ユーザーID", userId, 1, 8, true, false, false, true, false, false);
						passwordErrorMessageList = inputChecker.doCheck("パスワード", password, 1, 16, true, false, false, true, false, false);

						if(userIdErrorMessageList.size()>0
										|| passwordErrorMessageList.size()>0) {
								session.put("loginFlg",0);
								return result;
						}
						//認証処理
						if(!userInfoDAO.isExistsUserInfo(userId, password)){
								isNotUserInfoMessage = "ユーザーIDまたはパスワードが異なります。";
								return result;
						}
				}
				//セッションタイムアウト
				if(!session.containsKey("tempUserId")) {
					return "sessionTimeout";
				}

				CartInfoDAO cartInfoDAO = new CartInfoDAO();
				//カートの情報をユーザーに紐付ける
				String tempUserId=session.get("tempUserId").toString();
				List<CartInfoDTO>cartInfoDTOListForTempUser = cartInfoDAO.getCartList(tempUserId);
				if(cartInfoDTOListForTempUser !=null && cartInfoDTOListForTempUser.size()>0) {
						boolean cartresult = changeCartInfo(cartInfoDTOListForTempUser,tempUserId);
						if(!cartresult) {
								return "DBError";
						}
				}
				//ユーザー情報をsessionに登録し、tempUserIdを削除する。
				session.put("userId", userId);
				session.put("loginFlg", 1);
				if(idFlg) {
					session.put("idFlg",true);
				}
				session.remove("tempUserId");
				//次の遷移先を設定
				if(session.containsKey("cartFlg")
							&& Integer.parseInt(session.get("cartFlg").toString())==1) {
						//カート画面に表示する情報を取得
						session.remove("cartFlg");
						cartInfoDTOList = cartInfoDAO.getCartList(userId);
						totalPrice = cartInfoDAO.getTotalPrice(userId);
						result = "cart";
				}else {
					result = SUCCESS;
				}

				return result;
		}

		/**
		 * DBのカートを取得/更新する
		 * @param cartInfoDTOListForTempUser 仮ユーザーIDに紐づくカート情報
		 * @param tempUserId 仮ユーザーID
		 */
		private boolean changeCartInfo(List<CartInfoDTO>cartInfoDTOListForTempUser,String tempUserId) {
				int count = 0;
				CartInfoDAO cartInfoDAO = new CartInfoDAO();
				boolean result = false;

				for(CartInfoDTO dto:cartInfoDTOListForTempUser) {
					if(cartInfoDAO.searchProductId(userId,dto.getProductId())) {
						count +=cartInfoDAO.updateProductCount(userId, dto.getProductId(), dto.getProductCount());
						cartInfoDAO.deleteCartProduct(tempUserId,dto.getProductId());
					}else {
						count += cartInfoDAO.updateTemporaryId(userId,tempUserId,dto.getProductId());
					}
				}

				if(count == cartInfoDTOListForTempUser.size()){
					result = true;
				}
				return result;
		}

		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId=userId;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password=password;
		}
		public boolean getIdFlg() {
			return idFlg;
		}
		public void setIdFlg(boolean idFlg) {
			this.idFlg=idFlg;
		}
		public List<String> getUserIdErrorMessageList() {
			return userIdErrorMessageList;
		}
		public void setUserIdErrorMessageList(List<String>userIdErrorMessageList) {
			this.userIdErrorMessageList=userIdErrorMessageList;
		}
		public List<String> getPasswordErrorMessageList() {
			return passwordErrorMessageList;
		}
		public void setPasswardErrorMessageList(List<String>passwordErrorMessageList) {
			this.passwordErrorMessageList=passwordErrorMessageList;
		}
		public String getIsNotUserInfoMessage() {
			return isNotUserInfoMessage;
		}
		public void setIsNotUserInfoMessage(String isNotUserInfoMessage) {
			this.isNotUserInfoMessage=isNotUserInfoMessage;
		}
		public List<CartInfoDTO>getCartInfoDTOList(){
			return cartInfoDTOList;
		}
		public void setCartInfoDTOList(List<CartInfoDTO>cartInfoDTOList) {
			this.cartInfoDTOList=cartInfoDTOList;
		}
		public int getTotalPrice() {
			return totalPrice;
		}
		public void setTotalPrice(int totalPrice) {
			this.totalPrice=totalPrice;
		}
		public Map<String,Object>getSession(){
			return session;
		}
		public void setSession(Map<String,Object>session) {
			this.session=session;
		}
	}
