package bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * チャットログ情報を管理するBean
 */
public class ChatLog implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** チャットログID */
	private int chatLogId;
	/** ルームID */
	private String roomId;
	/** ユーザID */
	private String userId;
	/** ユーザ名 */
	private String userName;
	/** 退会済か */
	private boolean isWithdrawal;
	/** メッセージ */
	private String message;
	/** 投稿日時 */
	private Timestamp createdAt;
	
	private boolean isFavorited;

	public ChatLog() {
		// for JSP
	}

	public ChatLog(int chatLogId, String roomId, String userId, String userName, boolean isWithdrawal, String message, Timestamp createdAt, boolean isFavorited) {
		this.chatLogId = chatLogId;
		this.roomId = roomId;
		this.userId = userId;
		this.userName = userName;
		this.message = message;
		this.createdAt = createdAt;
		this.isWithdrawal = isWithdrawal;
		this.isFavorited = isFavorited;
	}

	public int getChatLogId() {
		return chatLogId;
	}

	public String getRoomId() {
		return roomId;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getMessage() {
		return message;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public boolean isWithdrawal() {
		return isWithdrawal;
	}

	public boolean isFavorited() {
		return isFavorited;
	}

}