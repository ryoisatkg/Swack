package dao;

import static parameter.DAOParameters.*;
import static parameter.Messages.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bean.Room;
import bean.User;
import exception.SwackException;

public class RoomsDAO {
	/**
	 * roomidで指定したルームにuseridで指定したユーザを登録する
	 * 
	 * @param roomid
	 * @param userid
	 * @throws SwackException
	 */
	public String selectMaxRoomId() throws SwackException {
		String sql = "SELECT max(roomid) as maxroomid FROM rooms";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);
			ResultSet rs = pStmt.executeQuery();

			String name = rs.next() ? rs.getString("maxroomid") : null;

			rs.close();
			pStmt.close();
			return name;
		} catch (SQLException e) {
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * ルームをDBに登録する
	 * roomidは主キーなので重複しない値にする必要がある
	 * @param userid
	 * @param roomid
	 * @param roomname
	 * @param privated
	 * @throws SwackException
	 */
	public void createRoom(String userid, String roomid, String roomname, boolean directed, boolean privated) throws SwackException {
		String sql = "insert into rooms (roomid, roomname, createduserid, directed, privated)\n"
				+ "values(?, ?, ?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, roomid);
			pStmt.setString(2, roomname);
			pStmt.setString(3, userid);
			pStmt.setBoolean(4, directed);
			pStmt.setBoolean(5, privated);
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

	public void leaveRoom(String roomid, String userid) throws SwackException {
		String sql = "DELETE FROM joinroom WHERE roomid = ? and userid = ?";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, roomid);
			pStmt.setString(2, userid);

			if (pStmt.executeUpdate() != 1) {
				pStmt.close();
				throw new SwackException(ERR_DB_PROCESS + ": 削除に失敗？(joinRoom)");
			}

			pStmt.close();
		} catch (SQLException e) {
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * 指定したユーザを指定したルームに参加させる
	 * @param roomid
	 * @param userid
	 * @throws SwackException
	 */
	public void joinRoom(String roomid, String userid) throws SwackException {
		String sql = "INSERT INTO joinroom (roomid, userid) values (?, ?)";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, roomid);
			pStmt.setString(2, userid);

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

	public void joinRoomWithMultiUsers(String roomid, String[] userids) throws SwackException {

		/*
		 * INSERT INTO joinroom(roomid, userid)
		 * SELECT ?, userid
		 * FROM (VALUES
		 *    (?),
		 *    (?),
		 *    ... (useridの数だけプレースホルダが作られる)
		 *    (?),
		 *    (?)
		 * ) as userids(userid)
		 * 
		 * のようなクエリを作る
		 */
		String sqlHead = "INSERT INTO joinroom(roomid, userid)\n"
				+ "SELECT ?, userid\n"
				+ "FROM (VALUES";
		ArrayList<String> sql_values = new ArrayList<String>();

		String sqlEnd = ") as userids(userid)";

		for (int i = 0; i < userids.length; i++) {
			sql_values.add("(?)");
		}

		String sqlMid = String.join(",", sql_values);

		String sql = sqlHead + sqlMid + sqlEnd;

		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, roomid);

			// 動的に作ったプレースホルダに値をセットする
			for (int i = 0; i < userids.length; i++) {
				pStmt.setString(2 + i, userids[i]);
			}

			if (pStmt.executeUpdate() != userids.length) {
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
	 * 指定されたルームに指定されたユーザが参加済かどうかを調べる
	 * @param roomid
	 * @param userid
	 * @return
	 * @throws SwackException
	 */
	public boolean isJoined(String roomid, String userid) throws SwackException {
		String sql = "SELECT roomid FROM joinroom WHERE roomid = ? AND userid = ? LIMIT 1";

		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, roomid);
			pStmt.setString(2, userid);
			var rs = pStmt.executeQuery();

			boolean result = rs.next();
			rs.close();
			pStmt.close();

			return result;
		} catch (SQLException e) {
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * 指定したルームにまだ参加していないユーザの一覧を取得する
	 * @param roomid
	 * @return
	 * @throws SwackException
	 */
	public ArrayList<User> getNotJoinedUsers(String roomid) throws SwackException {
		String sql = "select users.userid, users.username \n"
				+ "from users\n"
				+ "where users.userid not in ("
				+ "	select userid\n"
				+ "	from joinroom\n"
				+ "	where roomid = ?"
				+ ")\n"
				+ "and users.userid <> 'U0000' -- root user\n"
				+ "and NOT users.is_withdrawal";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, roomid);
			var rs = pStmt.executeQuery();
			var chatlogs = new ArrayList<User>();

			while (rs.next()) {
				var c = new User(
						rs.getString("userid"),
						rs.getString("username"),
						null, null, false);
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

	public boolean nameExists(String roomname) throws SwackException {
		String sql = "select roomname from rooms where roomname = ?";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, roomname);
			var rs = pStmt.executeQuery();

			boolean result = rs.next();
			rs.close();
			pStmt.close();

			return result;
		} catch (SQLException e) {
			System.out.println("DM");
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * 2人だけのDMルームで、指定した二人のみが参加しているルームを検索する
	 * 
	 * @param userid1
	 * @param userid2
	 * @return roomid, 存在しなければnull
	 * @throws SwackException 
	 */
	public String get2usersDMRoom(String userid1, String userid2) throws SwackException {
		String sql = "WITH searchusers AS (\n"
				+ "    SELECT\n"
				+ "            userid\n"
				+ "        FROM\n"
				+ "            (\n"
				+ "            VALUES (?)\n"
				+ "            ,(?)\n"
				+ "            ) AS s (userid)\n"
				+ ")\n"
				+ ",roomcount AS (\n"
				+ "    SELECT\n"
				+ "            roomid\n"
				+ "            ,COUNT (userid) as cnt\n"
				+ "        FROM\n"
				+ "            joinroom\n"
				+ "        GROUP BY\n"
				+ "            roomid\n"
				+ "        	\n"
				+ ") SELECT\n"
				+ "        rooms.roomid\n"
				+ "    FROM\n"
				+ "        rooms JOIN roomcount\n"
				+ "            ON rooms.roomid = roomcount.roomid JOIN joinroom\n"
				+ "            ON rooms.roomid = joinroom.roomid\n"
				+ "    WHERE\n"
				+ "        directed\n"
				+ "        AND roomcount.cnt = 2\n"
				+ "        AND joinroom.userid IN (\n"
				+ "            SELECT\n"
				+ "                    userid\n"
				+ "                FROM\n"
				+ "                    searchusers\n"
				+ "        )\n"
				+ "    GROUP BY\n"
				+ "        rooms.roomid\n"
				+ "    HAVING\n"
				+ "        COUNT (userid) = 2";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, userid1);
			pStmt.setString(2, userid2);
			var rs = pStmt.executeQuery();

			String result = null;
			if (rs.next()) {
				result = rs.getString("roomid");
			}
			rs.close();
			pStmt.close();

			return result;
		} catch (SQLException e) {
			System.out.println("DM");
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}
	
	/**
	 * 自分を含む1対1のDM部屋を検索する
	 * @param userid
	 * @return
	 * @throws SwackException
	 */
	public Map<String, Room> getJoined2UsersDMRooms(String userid) throws SwackException {
		String sql = "WITH me AS (\n"
				+ "    SELECT\n"
				+ "            userid\n"
				+ "        FROM\n"
				+ "            (\n"
				+ "            VALUES (?)\n"
				+ "            ) AS s (userid)\n"
				+ ")\n"
				+ ",joinedDMRoomPartners AS (\n"
				+ "    SELECT\n"
				+ "            rooms.roomid AS roomid\n"
				+ "            ,themjoined.userid AS partner\n"
				+ "        FROM\n"
				+ "            me\n"
				+ "            ,rooms JOIN joinroom AS mejoined\n"
				+ "                ON rooms.roomid = mejoined.roomid JOIN joinroom AS themjoined\n"
				+ "                ON rooms.roomid = themjoined.roomid\n"
				+ "        WHERE\n"
				+ "            mejoined.userid = me.userid\n"
				+ "            AND themjoined.userid <> me.userid\n"
				+ "            AND rooms.directed\n"
				+ ")\n"
				+ ",countValues AS (\n"
				+ "    SELECT\n"
				+ "            roomid\n"
				+ "            ,COUNT (*) AS cnt\n"
				+ "        FROM\n"
				+ "            joinroom\n"
				+ "        GROUP BY\n"
				+ "            roomid\n"
				+ ") SELECT\n"
				+ "		userid,\n"
				+ "        rooms.roomid,\n"
				+ "        rooms.roomname, \n"
				+ "        rooms.directed, \n"
				+ "        rooms.privated,\n"
				+ "        cnt as memberCount\n"
				+ "    FROM\n"
				+ "        users LEFT JOIN joinedDMRoomPartners AS partner\n"
				+ "            ON users.userid = partner.partner JOIN countValues\n"
				+ "            ON partner.roomid = countValues.roomid\n"
				+ "            JOIN rooms ON partner.roomid = rooms.roomid\n"
				+ "    WHERE\n"
				+ "        cnt IS NULL\n"
				+ "        OR cnt = 2";
		try (Connection conn = DriverManager.getConnection(DB_ENDPOINT, DB_USERID, DB_PASSWORD)) {
			var pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, userid);
			var rs = pStmt.executeQuery();
			HashMap<String, Room> result = new HashMap<String, Room>();
			while (rs.next()) {
				String uid = rs.getString("userid");
				
				Room r = new Room(rs.getString("roomid"),
						rs.getString("roomname"),
						rs.getBoolean("directed"),
						rs.getBoolean("privated"),
						rs.getInt("memberCount"));
				
				result.put(uid, r);
			}
			rs.close();
			pStmt.close();

			return result;
		} catch (SQLException e) {
			System.out.println("DM");
			System.out.println(e);
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}
}
