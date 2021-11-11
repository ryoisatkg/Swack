package model;

import java.util.List;

import bean.ChatLog;
import bean.Room;
import dao.ChatDAO;
import exception.SwackException;

public class ChatModel {
	public List<ChatLog> getChatLogList(String userid, String roomid) throws SwackException {
		return new ChatDAO().getChatLogs(userid, roomid);
	}

	/**
	 * そのユーザIDが参加している
	 * @param userid
	 * @return
	 * @throws SwackException
	 */
	public List<Room> getJoinedRoomList(String userid) throws SwackException {
		return new ChatDAO().getJoinedRooms(userid);
	}

	/**
	 * そのユーザIDが参加しているDMのリストを取得する
	 * @param userid
	 * @return
	 * @throws SwackException
	 */
	public List<Room> getDMList(String userid) throws SwackException {
		return new ChatDAO().getDirectMessages(userid);
	}

	public Room getRoomInfo(String roomid) throws SwackException {
		return new ChatDAO().getRoomInfo(roomid);
	}
	
	public List<Room> getViewableRoomInfosWithJoined(String userid) throws SwackException {
		// TODO
		return new ChatDAO().getViewableRoomInfosWithJoined(userid);
	}

	public Room getDirectMailRoomInfo(String roomid, String userid) throws SwackException {
		return new ChatDAO().getDirectMessageRoomInfo(roomid, userid);
	}

	public void saveChatlog(String roomid, String userid, String message) throws SwackException {
		new ChatDAO().saveChatlog(roomid, userid, message);
	}

	public boolean roomExists(String roomid) throws SwackException {
		return roomid != null && new ChatDAO().roomExists(roomid);
		
	}
	
	public void deleteChatlog(int chatlogid) throws SwackException {
		new ChatDAO().deleteChatlog(chatlogid);
	}
	
	public void updateChatlog(int chatlogid, String message) throws SwackException {
		new ChatDAO().updateChatlog(chatlogid, message);
	}
	
	public ChatLog getChatlog(int chatlogid, String userid) throws SwackException {
		return new ChatDAO().getChatlog(chatlogid, userid);
	}
}
