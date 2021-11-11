package dao;

import static parameter.DAOParameters.*;
import static parameter.Messages.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.ChatLog;
import exception.SwackException;

public class FavoriteDAO {
	public void addFavorite(String userid, int chatlogid) throws SwackException {
		String sql = "INSERT INTO favorite(chatlogid, userid, created_at) VALUES(?, ?, CURRENT_TIMESTAMP)";

		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setInt(1, chatlogid);
			pStmt.setString(2, userid);

			if (pStmt.executeUpdate() != 1) {
				pStmt.close();
				throw new SwackException(ERR_DB_PROCESS + ": 追加に失敗？(Favorite)");
			}

			pStmt.close();
		} catch (SQLException e) {
			System.out.println("Favorite");
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	public void removeFavorite(String userid, int chatlogid) throws SwackException {
		String sql = "DELETE FROM favorite WHERE userid = ? AND chatlogid = ?";

		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, userid);
			pStmt.setInt(2, chatlogid);

			if (pStmt.executeUpdate() != 1) {
				pStmt.close();
				throw new SwackException(ERR_DB_PROCESS + ": 削除に失敗？(favorite)");
			}

			pStmt.close();
		} catch (SQLException e) {
			System.out.println("Favorite");
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	public boolean isFavorited(String userid, int chatlogid) throws SwackException {
		String sql = "SELECT chatlogid FROM favorite WHERE userid = ? AND chatlogid = ? LIMIT 1";

		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, userid);
			pStmt.setInt(2, chatlogid);
			var rs = pStmt.executeQuery();

			boolean result = rs.next();
			rs.close();
			pStmt.close();

			return result;

		} catch (SQLException e) {
			System.out.println("Favorite");
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	public List<ChatLog> getFavoriteChatlogs(String userid) throws SwackException {
		String sql = "SELECT\n"
				+ "        chatlog.chatlogid\n"
				+ "        ,chatlog.roomid\n"
				+ "        ,chatlog.userid\n"
				+ "        ,users.username\n"
				+ "        ,users.is_withdrawal\n"
				+ "        ,chatlog.message\n"
				+ "        ,chatlog.created_at\n"
				+ "    FROM\n"
				+ "        favorite JOIN chatlog\n"
				+ "            ON favorite.chatlogid = chatlog.chatlogid\n"
				+ "            JOIN users\n"
				+ "            ON chatlog.userid = users.userid\n"
				+ "    WHERE\n"
				+ "    	favorite.userid = ?\n"
				+ "    ORDER BY\n"
				+ "        favorite.created_at;";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, userid);
			var rs = pStmt.executeQuery();

			ArrayList<ChatLog> c = new ArrayList<ChatLog>();
			while (rs.next()) {
				c.add(new ChatLog(rs.getInt("chatlogid"), rs.getString("roomid"), rs.getString("userid"),
						rs.getString("username"), rs.getBoolean("is_withdrawal"), rs.getString("message"),
						rs.getTimestamp("created_at"), true));
			}
			rs.close();
			pStmt.close();

			return c;
		} catch (SQLException e) {
			System.out.println("Favorite");
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}
}
