package model;

import java.util.ArrayList;
import java.util.Map;

import bean.Room;
import bean.User;
import dao.RoomsDAO;
import exception.SwackException;

public class RoomModel {
	public String createNextId() throws SwackException {
		String lastId = new RoomsDAO().selectMaxRoomId();
		// まだユーザが一人も登録されていない場合は1を返す
		if (lastId == null) {
			return "R0000";
		}

		// いまの最大ユーザ番号に1足したIDを返す
		String numStr = lastId.substring(1);
		int i = Integer.parseInt(numStr) + 1;
		return String.format("R%04d", i);
	}
	
	public String createRoom(String userid, String roomname, boolean directed, boolean privated) throws SwackException {
		RoomsDAO rd = new RoomsDAO();
		String nextrid = createNextId();
		rd.createRoom(userid, nextrid, roomname, directed, privated);
		rd.joinRoom(nextrid, userid);
		return nextrid;
	}

	public String createRoom(String userid, String roomname, boolean privated) throws SwackException {
		return createRoom(userid, roomname, false, privated);
	}

	public ArrayList<User> getNotJoinedUsers(String roomid) throws SwackException {
		return new RoomsDAO().getNotJoinedUsers(roomid);
	}

	public void joinRoom(String roomid, String userid) throws SwackException {
		new RoomsDAO().joinRoom(roomid, userid);
	}

	public void joinMultipleUsersToRoom(String roomid, String[] userids) throws SwackException {
		new RoomsDAO().joinRoomWithMultiUsers(roomid, userids);
	}

	public void leaveRoom(String roomid, String userid) throws SwackException {
		new RoomsDAO().leaveRoom(roomid, userid);
	}

	public boolean isJoined(String roomid, String userid) throws SwackException {
		// TODO 自動生成されたメソッド・スタブ
		return new RoomsDAO().isJoined(roomid, userid);
	}
	
	public boolean nameExists(String roomname) throws SwackException {
		return new RoomsDAO().nameExists(roomname);
	}
	
	public String get2usersDMRoom(String userid1, String userid2) throws SwackException {
		return new RoomsDAO().get2usersDMRoom(userid1, userid2);
	}
	
	public Map<String, Room> getJoined2UsersDMRooms(String userid) throws SwackException {
		return new RoomsDAO().getJoined2UsersDMRooms(userid);
	}
}
