package model;

import java.util.ArrayList;

import bean.User;
import dao.RoomsDAO;
import dao.UsersDAO;
import exception.SwackException;

/**
 * ログイン認証を実行するクラス
 */
public class UserModel {

	/**
	 * ログイン認証を行う
	 * 
	 * @param mailAddress メールアドレス
	 * @param password    パスワード
	 * @return ユーザ情報(ログインできなかった場合はnull)
	 */
	public boolean exists(String mailAddress) throws SwackException {
		return new UsersDAO().exists(mailAddress);
	}
	
	public boolean existsWithoutMe(String mailAddress, String userid) throws SwackException {
		return new UsersDAO().existsWithoutMe(mailAddress, userid);
	}
	
	public ArrayList<User> getUsersWithoutMe(String userid) throws SwackException {
		return new UsersDAO().getUsersWithoutMe(userid);
	}

	public String createUserId() throws SwackException {
		String lastId = new UsersDAO().selectMaxUserId();
		// まだユーザが一人も登録されていない場合は1を返す
		if (lastId == null) {
			return "U0000";
		}

		// いまの最大ユーザ番号に1足したIDを返す
		String numStr = lastId.substring(1);
		int i = Integer.parseInt(numStr) + 1;
		return String.format("U%04d", i);
	}

	public void registerUsers(User user) throws SwackException {
		new UsersDAO().insert(user);
		// 全員が必ず参加するeveryoneチャンネルにも登録する
		new RoomsDAO().joinRoom("R0000", user.getUserId());
	}
	
	public String getLastAccessRoomId(String userid) throws SwackException {
		return new UsersDAO().getLastAccessRoomId(userid);
	}
	
	public void updateLastAccessRoomId(String userid, String lastAccessRoomId) throws SwackException {
		new UsersDAO().updateLastAccessRoomId(userid, lastAccessRoomId);
	}

	
	public void updateUserInfo(String userid, String username, String mailaddress, String password) throws SwackException {
		new UsersDAO().updateUserInfo(userid, username, mailaddress, password);
	}
	
	public void withdrawalUser(String userid) throws SwackException {
		new UsersDAO().withdrawalUser(userid);
	}

}