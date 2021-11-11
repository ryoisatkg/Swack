package bean;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	/** ユーザID */
	private String userId;
	/** ユーザ名 */
	private String userName;
	/** メールアドレス */
	private String mailAddress;
	/** パスワード */
	private String password;
	/** 前回表示したページ **/
	private String lastAccessRoomId;
	/** 退会していればtrue*/
	private boolean isWithdrawal;

	public User() {
		// for JSP
	}

	public User(String userId, String userName, String mailAddress, String password, String lastAccessRoomId,
			boolean isWithdrawl) {
		this.userId = userId;
		this.userName = userName;
		this.mailAddress = mailAddress;
		this.password = password;
		this.lastAccessRoomId = lastAccessRoomId;
		this.isWithdrawal = isWithdrawl;
	}

	public User(String userId, String userName, String mailAddress, String password, boolean isWithdrawl) {
		this(userId, userName, mailAddress, password, null, isWithdrawl);
	}

	public User(String userId, String userName, boolean isWithdrawl) {
		this(userId, userName, null, null, null, isWithdrawl);
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public String getPassword() {
		return password;
	}

	public String getLastAccessRoomId() {
		return lastAccessRoomId;
	}

	public boolean isWithdrawal() {
		return isWithdrawal;
	}

}
