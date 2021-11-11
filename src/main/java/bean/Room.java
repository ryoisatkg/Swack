package bean;

import java.io.Serializable;

public class Room implements Serializable {
	private static final long serialVersionUID = 1L;

	private String roomId;
	private String roomName;
	private boolean directed;
	private boolean privated;
	private boolean joined;
	/** そのルームに参加しているメンバーの数 */
	private int memberCount;

	public Room() {
		// jsp
	}

	
	public Room(String roomid, String roomname, boolean directed, boolean privated, int memberCount, boolean joined) {
		super();
		this.roomId = roomid;
		this.roomName = roomname;
		this.directed = directed;
		this.privated = privated;
		this.memberCount = memberCount;
		this.joined = joined;
	}
	
	public Room(String roomid, String roomname, boolean directed, boolean privated, int memberCount) {
		this(roomid, roomname, directed, privated, memberCount, false);
	}

	public Room(String roomid, String roomname, boolean directed, boolean privated) {
		this(roomid, roomname, directed, privated, 0, false);
	}

	public String getRoomId() {
		return roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public boolean isDirected() {
		return directed;
	}

	public boolean isPrivated() {
		return privated;
	}

	public int getMemberCount() {
		return memberCount;
	}

	public boolean isJoined() {
		return joined;
	}
}
