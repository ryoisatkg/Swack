package dao;

import static parameter.DAOParameters.*;
import static parameter.Messages.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.ChatLog;
import bean.Room;
import exception.SwackException;

public class ChatDAO {
	/**
	 * ルームの詳細な情報を取る
	 * @param roomid
	 * @return
	 * @throws SwackException
	 */
	public Room getRoomInfo(String roomid) throws SwackException {
		String sql = "SELECT\n"
				+ "        rooms.roomid AS id\n"
				+ "        ,rooms.roomname AS name\n"
				+ "        ,rooms.privated AS private\n"
				+ "        ,rooms.directed AS direct\n"
				+ "        ,COUNT(joinroom.userid) AS count\n"
				+ "    FROM rooms JOIN joinroom ON rooms.roomid = joinroom.roomid JOIN users ON joinroom.userid = users.userid\n"
				+ "    WHERE rooms.roomid = ?\n"
				+ "	   AND NOT users.is_withdrawal\n"
				+ "    GROUP BY id, name, private, direct;";

		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, roomid);
			var rs = pStmt.executeQuery();

			Room r = null;
			if (rs.next()) {
				r = new Room(rs.getString("id"), rs.getString("name"), rs.getBoolean("direct"),
						rs.getBoolean("private"), rs.getInt("count"));
			}
			rs.close();
			pStmt.close();

			return r;
		} catch (SQLException e) {
			System.out.println("ROOM");
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}
	
	public List<Room> getViewableRoomInfosWithJoined(String userid) throws SwackException {
		String sql = "WITH count_roomusers AS (\n"
				+ "    SELECT\n"
				+ "            joinroom.roomid AS id\n"
				+ "            ,COUNT (joinroom.userid) AS cnt\n"
				+ "        FROM\n"
				+ "            joinroom,\n"
				+ "            users\n"
				+ "        WHERE\n"
				+ "            NOT users.is_withdrawal"
				+ "        GROUP BY\n"
				+ "            id\n"
				+ ")\n"
				+ ",viewable_rooms AS (\n"
				+ "    SELECT\n"
				+ "            rooms.roomid AS id\n"
				+ "            ,rooms.roomname AS name\n"
				+ "            ,rooms.privated AS private\n"
				+ "            ,rooms.directed AS direct\n"
				+ "            ,(\n"
				+ "                joinroom.userid IS NOT NULL\n"
				+ "            ) AS joined\n"
				+ "        FROM\n"
				+ "            rooms LEFT JOIN joinroom\n"
				+ "                ON joinroom.userid = ?\n"
				+ "            AND rooms.roomid = joinroom.roomid\n"
				+ "        WHERE\n"
				+ "            NOT rooms.directed"
				+ "            AND (NOT rooms.privated\n"
				+ "            OR joinroom.userid IS NOT NULL)\n"
				+ ") SELECT\n"
				+ "        viewable_rooms.id\n"
				+ "        ,name\n"
				+ "        ,private\n"
				+ "        ,direct\n"
				+ "        ,joined\n"
				+ "        ,COALESCE (\n"
				+ "            count_roomusers.cnt\n"
				+ "            ,0\n"
				+ "        ) AS cnt\n"
				+ "    FROM\n"
				+ "        viewable_rooms LEFT JOIN count_roomusers\n"
				+ "            ON viewable_rooms.id = count_roomusers.id;\n";
		
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, userid);
			var rs = pStmt.executeQuery();

			ArrayList<Room> r = new ArrayList<Room>();
			while (rs.next()) {
				r.add(new Room(rs.getString("id"), rs.getString("name"), rs.getBoolean("direct"),
						rs.getBoolean("private"), rs.getInt("cnt"), rs.getBoolean("joined")));
			}
			rs.close();
			pStmt.close();

			return r;
		} catch (SQLException e) {
			System.out.println("ROOM");
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * DM用のフォーマットで部屋情報を取得する
	 * DMに参加している人数はは常に2とする
	 * @param roomid
	 * @param userid
	 * @return
	 * @throws SwackException
	 */
	public Room getDirectMessageRoomInfo(String roomid, String userid) throws SwackException {
		String sql = "-- ユーザーが参加しているDMのroomid一覧を取得する\n"
				+ "WITH user_joined_rooms AS (\n"
				+ "	SELECT rooms.roomid AS id\n"
				+ "    FROM joinroom\n"
				+ "		JOIN rooms \n"
				+ "				ON joinroom.roomid = rooms.roomid\n"
				+ "    WHERE rooms.directed\n"
				+ "    AND joinroom.userid = ? )\n"
				+ "   \n"
				+ "SELECT rooms.roomid AS id, ( users.username || 'とのDM' ) AS uname FROM user_joined_rooms \n"
				+ "	JOIN joinroom \n"
				+ "		ON user_joined_rooms.id = joinroom.roomid\n"
				+ "	JOIN users\n"
				+ "		ON joinroom.userid = users.userid\n"
				+ "	JOIN rooms\n"
				+ "		ON rooms.roomid = joinroom.roomid\n"
				+ "WHERE joinroom.userid <> ? \n" + "AND rooms.roomid = ?";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, userid);
			pStmt.setString(2, userid);
			pStmt.setString(3, roomid);
			var rs = pStmt.executeQuery();
			Room r = null;
			if (rs.next()) {
				// DM はルーム内に2人しかいなく、privateでありdirectedがtrueであるという仕様に基づく
				r = new Room(rs.getString("id"), rs.getString("uname"), true, true, 2);
			}
			rs.close();
			pStmt.close();

			return r;
		} catch (SQLException e) {
			System.out.println("DM");
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * useridで指定されたユーザが参加しているルームの一覧を取得する
	 * 
	 * @param userid   検索する
	 * @param directed trueならDMを取得, falseなら参加ルームを取得
	 * @return ルーム一覧
	 * @throws SwackException SQLExceptionをラップする
	 */
	public ArrayList<Room> getJoinedRooms(String userid) throws SwackException {
		String sql = "select rooms.roomid as id, rooms.roomname as name, rooms.privated as private \n"
				+ "from rooms join \n"
				+ "joinroom on rooms.roomid = joinroom.roomid \n"
				+ "where joinroom.userid = ? \n"
				+ "and not rooms.directed \n"
				+ "order by name";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, userid);
			var rs = pStmt.executeQuery();
			var rooms = new ArrayList<Room>();

			while (rs.next()) {
				// String roomid, String roomname, boolean directed, boolean privated, int memberCount, boolean joined
				var r = new Room(rs.getString("id"), rs.getString("name"), false, rs.getBoolean("private"),0);
				rooms.add(r);
			}
			rs.close();
			pStmt.close();

			return rooms;
		} catch (SQLException e) {
			System.out.println("ROOM");
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * useridで指定されたユーザが参加しているダイレクトメッセージ(DM)の一覧を取得する
	 * 
	 * @param userid   検索する
	 * @param directed trueならDMを取得, falseなら参加ルームを取得
	 * @return ルーム一覧
	 * @throws SwackException SQLExceptionをラップする
	 */
	public ArrayList<Room> getDirectMessages(String userid) throws SwackException {
		// DMを探すならdirectedとprivatedは必ずtrueなので、 directedだけ読めばいい
		String sql = "WITH user_joined_rooms AS (SELECT\n"
				+ "        rooms.roomid AS id\n"
				+ "    FROM\n"
				+ "        joinroom\n"
				+ "            JOIN rooms ON joinroom.roomid = rooms.roomid\n"
				+ "            	AND joinroom.userid = ?\n"
				+ "    WHERE rooms.directed)\n"
				+ "    \n"
				+ "SELECT rooms.roomid AS id, users.username AS uname FROM user_joined_rooms \n"
				+ "	JOIN joinroom \n"
				+ "		ON user_joined_rooms.id = joinroom.roomid\n"
				+ "	JOIN users\n"
				+ "		ON joinroom.userid = users.userid\n"
				+ "	JOIN rooms\n"
				+ "		ON rooms.roomid = joinroom.roomid\n"
				+ "WHERE joinroom.userid <> ? \n"
				+ "order by uname";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, userid);
			pStmt.setString(2, userid);
			var rs = pStmt.executeQuery();
			var rooms = new ArrayList<Room>();

			while (rs.next()) {
				var r = new Room(rs.getString("id"), rs.getString("uname"), true, true);
				rooms.add(r);
			}
			rs.close();
			pStmt.close();

			return rooms;
		} catch (SQLException e) {
			System.out.println("DM");
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * ルームIDで指定されたルームのチャットをすべて取得する
	 * 
	 * @param roomid
	 * @return
	 * @throws SwackException
	 */
	public ArrayList<ChatLog> getChatLogs(String userid, String roomid) throws SwackException {
		String sql = "SELECT\n"
				+ "        chatlog.chatlogid\n"
				+ "        ,users.userid AS uid\n"
				+ "        ,users.is_withdrawal\n"
				+ "        ,users.username AS uname\n"
				+ "        ,chatlog.message\n"
				+ "        ,chatlog.created_at\n"
				+ "        ,(favorite.chatlogid IS NOT NULL) AS favorited\n"
				+ "    FROM\n"
				+ "        chatlog LEFT JOIN users\n"
				+ "            ON chatlog.userid = users.userid\n"
				+ "            LEFT JOIN favorite\n"
				+ "            ON chatlog.chatlogid = favorite.chatlogid AND favorite.userid = ?\n"
				+ "    WHERE\n"
				+ "        roomid = ?\n"
				+ "    ORDER BY\n"
				+ "        created_at;";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, userid);
			pStmt.setString(2, roomid);
			var rs = pStmt.executeQuery();
			var chatlogs = new ArrayList<ChatLog>();

			while (rs.next()) {
				var c = new ChatLog(
						rs.getInt("chatlogid"),
						roomid,
						rs.getString("uid"),
						rs.getString("uname"),
						rs.getBoolean("is_withdrawal"),
						rs.getString("message"),
						rs.getTimestamp("created_at"),
						rs.getBoolean("favorited"));
				chatlogs.add(c);
			}
			rs.close();
			pStmt.close();

			return chatlogs;
		} catch (SQLException e) {
			System.out.println("DM");
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * 引数で指定されたルームにチャットを追加する
	 * @param roomid
	 * @param userid
	 * @param message
	 * @throws SwackException
	 */
	public void saveChatlog(String roomid, String userid, String message) throws SwackException {
		String sql = "insert into chatlog (chatlogid, roomid, userid, message, created_at) values (nextval('CHATLOGID_SEQ'), ?, ?, ?, CURRENT_TIMESTAMP)";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, roomid);
			pStmt.setString(2, userid);
			pStmt.setString(3, message);

			if (pStmt.executeUpdate() != 1) {
				pStmt.close();
				throw new SwackException(ERR_DB_PROCESS + ": 追加に失敗？(joinRoom)");
			}

			pStmt.close();
		} catch (SQLException e) {
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * db上に存在するroomidかどうか調べる
	 * @param roomid
	 * @return
	 * @throws SwackException
	 */
	public boolean roomExists(String roomid) throws SwackException {
		// DMを探すならdirectedとprivatedは必ずtrueなので、 directedだけ読めばいい
		String sql = "SELECT roomid FROM rooms WHERE roomid = ?";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, roomid);
			var rs = pStmt.executeQuery();

			boolean result = rs.next();
			rs.close();
			pStmt.close();

			return result;
		} catch (SQLException e) {
			System.out.println("error");
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * 指定されたchatlogidのチャットログを削除する
	 * @param chatlogid
	 * @throws SwackException
	 */
	public void deleteChatlog(int chatlogid) throws SwackException {
		String sql = "delete from chatlog\n"
				+ "where chatlogid = ?;";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setInt(1, chatlogid);

			if (pStmt.executeUpdate() != 1) {
				pStmt.close();
				throw new SwackException(ERR_DB_PROCESS + ": 削除に失敗？(deleteChatlog)");
			}

			pStmt.close();
		} catch (SQLException e) {
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * 指定したchatlogidに対応するチャットログを指定されたメッセージ内容へ変更する
	 * @param chatlogid
	 * @param message
	 * @throws SwackException
	 */
	public void updateChatlog(int chatlogid, String message) throws SwackException {
		String sql = "update chatlog\n"
				+ "set message = ?\n"
				+ "where chatlogid = ?;";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, message);
			pStmt.setInt(2, chatlogid);

			if (pStmt.executeUpdate() != 1) {
				pStmt.close();
				throw new SwackException(ERR_DB_PROCESS + ": 更新に失敗？(updateChatlog)");
			}

			pStmt.close();
		} catch (SQLException e) {
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * 指定したchatlogidに対応するチャットログを取得する
	 * @param chatlogid
	 * @return
	 * @throws SwackException
	 */
	public ChatLog getChatlog(int chatlogid, String userid) throws SwackException {
		// DMを探すならdirectedとprivatedは必ずtrueなので、 directedだけ読めばいい
		String sql = "SELECT\n"
				+ "        chatlog.chatlogid\n"
				+ "        ,users.userid AS uid\n"
				+ "        ,users.is_withdrawal\n"
				+ "        ,users.username AS uname\n"
				+ "        ,chatlog.roomid\n"
				+ "        ,chatlog.message\n"
				+ "        ,chatlog.created_at\n"
				+ "        ,(\n"
				+ "            favorite.chatlogid IS NOT NULL\n"
				+ "        ) AS favorited\n"
				+ "    FROM\n"
				+ "        chatlog JOIN users\n"
				+ "            ON chatlog.userid = users.userid LEFT JOIN favorite\n"
				+ "            ON chatlog.chatlogid = favorite.chatlogid\n"
				+ "        AND favorite.userid = ?\n"
				+ "    WHERE\n"
				+ "        chatlog.chatlogid = ?";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, userid);
			pStmt.setInt(2, chatlogid);
			var rs = pStmt.executeQuery();
			ChatLog c = null;

			if (rs.next()) {
				c = new ChatLog(
						rs.getInt("chatlogid"),
						rs.getString("roomid"),
						rs.getString("uid"),
						rs.getString("uname"),
						rs.getBoolean("is_withdrawal"),
						rs.getString("message"),
						rs.getTimestamp("created_at"),
						rs.getBoolean("favorited"));
			}
			rs.close();
			pStmt.close();

			return c;
		} catch (SQLException e) {
			System.out.println("DM");
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}
}
