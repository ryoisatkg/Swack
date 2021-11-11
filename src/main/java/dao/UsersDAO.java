package dao;

import static parameter.DAOParameters.*;
import static parameter.Messages.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.User;
import exception.SwackException;

public class UsersDAO {

	/**
	 * 指定されたメールアドレス、パスワードに合致する1人目のユーザを返す
	 * @param mailAddress メアド
	 * @param password パスワード
	 * @return 合致した一人目のユーザ
	 * @throws SwackException SQLExceptionが発生したら投げる
	 */
	public User select(String mailAddress, String password) throws SwackException {
		String sql = "SELECT USERID, USERNAME FROM USERS WHERE MAILADDRESS = ? AND PASSWORD = ? AND NOT is_withdrawal";
		User user = null;
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, mailAddress);
			pStmt.setString(2, password);

			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				String userId = rs.getString("USERID");
				String userName = rs.getString("USERNAME");
				// mask password <- ????
				user = new User(userId, userName, mailAddress, "********", null, false);
			}

			rs.close();
			pStmt.close();
		} catch (SQLException e) {
			throw new SwackException(ERR_DB_PROCESS, e);
		}
		return user;
	}

	public String getLastAccessRoomId(String userId) throws SwackException {
		String sql = "SELECT LASTACCESSROOMID FROM USERS WHERE USERID = ?";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, userId);

			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				String lastAccessRoomId = rs.getString("LASTACCESSROOMID");
				rs.close();
				pStmt.close();
				return lastAccessRoomId;
			}

			rs.close();
			pStmt.close();
		} catch (SQLException e) {
			throw new SwackException(ERR_DB_PROCESS, e);
		}
		return null;
	}

	public void updateLastAccessRoomId(String userid, String lastAccessRoomId) throws SwackException {
		var sql = "UPDATE users SET lastAccessRoomId = ?\n"
				+ "WHERE userid = ?";

		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, lastAccessRoomId);
			pStmt.setString(2, userid);

			int result = pStmt.executeUpdate();
			if (result != 1) {
				System.out.println(result);
				pStmt.close();
				throw new SwackException(ERR_DB_PROCESS + ": 追加に失敗？(insert)");
			}

			pStmt.close();
		} catch (SQLException e) {
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * 指定されたメールアドレスのユーザが存在するかどうかチェックする
	 * @param mailAddress 検索対象
	 * @return 存在すればtrue
	 * @throws SwackException SQLExceptionが発生したら投げる
	 */
	public boolean exists(String mailAddress) throws SwackException {
		String sql = "SELECT mailaddress FROM users WHERE mailaddress = ? AND NOT is_withdrawal limit 1;";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, mailAddress);

			ResultSet rs = pStmt.executeQuery();

			boolean result = rs.next();

			rs.close();
			pStmt.close();

			return result;
		} catch (SQLException e) {
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * 指定したメールアドレスが自分以外と重複しているかどうかを検索する
	 * (ユーザ情報変更のとき、自分のメールアドレスを自分と同じものに変更をかけると重複判定されることを避けるため)
	 * @param mailAddress
	 * @param userid
	 * @return
	 * @throws SwackException
	 */
	public boolean existsWithoutMe(String mailAddress, String userid) throws SwackException {
		String sql = "SELECT mailaddress FROM users WHERE mailaddress = ? AND userid <> ? AND NOT is_withdrawal limit 1;";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, mailAddress);
			pStmt.setString(2, userid);

			ResultSet rs = pStmt.executeQuery();

			boolean result = rs.next();

			rs.close();
			pStmt.close();

			return result;
		} catch (SQLException e) {
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * 現在最大のユーザIDを取得する
	 * @return 現在の最大ユーザID, ユーザが存在しなければnull
	 * @throws SwackException SQLExceptionが発生したら投げる
	 */
	public String selectMaxUserId() throws SwackException {
		String sql = "SELECT max(userid) as maxuserid FROM users";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);
			ResultSet rs = pStmt.executeQuery();

			String name = rs.next() ? rs.getString("maxuserid") : null;

			rs.close();
			pStmt.close();
			return name;
		} catch (SQLException e) {
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * 新しいユーザを登録する
	 * @param user 新しいユーザ
	 * @throws SwackException 挿入失敗時、その他SQLException時
	 */
	public void insert(User user) throws SwackException {
		String sql = "INSERT INTO users (userid, username, mailaddress, password) values (?, ?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, user.getUserId());
			pStmt.setString(2, user.getUserName());
			pStmt.setString(3, user.getMailAddress());
			pStmt.setString(4, user.getPassword());

			if (pStmt.executeUpdate() != 1) {
				pStmt.close();
				throw new SwackException(ERR_DB_PROCESS + ": 追加に失敗？(insert)");
			}

			pStmt.close();
		} catch (SQLException e) {
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * ユーザ情報を指定された情報に書き換える
	 * @param userid
	 * @param username
	 * @param mailaddress
	 * @param password
	 * @throws SwackException
	 */
	public void updateUserInfo(String userid, String username, String mailaddress, String password)
			throws SwackException {
		var sql = "UPDATE users SET username = ?, mailaddress = ?, password = ?\n"
				+ "WHERE userid = ? AND NOT is_withdrawal";

		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, username);
			pStmt.setString(2, mailaddress);
			pStmt.setString(3, password);
			pStmt.setString(4, userid);

			if (pStmt.executeUpdate() != 1) {
				pStmt.close();
				throw new SwackException(ERR_DB_PROCESS + ": 追加に失敗？(insert)");
			}

			pStmt.close();
		} catch (SQLException e) {
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	public ArrayList<User> getUsersWithoutMe(String userid) throws SwackException {
		String sql = "SELECT userid, username FROM users WHERE userid <> ? AND NOT is_withdrawal";

		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, userid);

			var users = new ArrayList<User>();
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				String userId = rs.getString("USERID");
				String userName = rs.getString("USERNAME");
				users.add(new User(userId, userName, false));
			}

			rs.close();
			pStmt.close();
			return users;
		} catch (SQLException e) {
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	public void withdrawalUser(String userid) throws SwackException {
		var sql = "UPDATE users SET mailaddress = NULL, password = '', lastaccessroomid = NULL, IS_WITHDRAWAL = true WHERE userid = ?";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, userid);

			if (pStmt.executeUpdate() != 1) {
				pStmt.close();
				throw new SwackException(ERR_DB_PROCESS + ": 削除に失敗？(insert)");
			}

			pStmt.close();
		} catch (SQLException e) {
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}
}