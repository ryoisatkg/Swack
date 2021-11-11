package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;
import exception.SwackException;
import model.ChatModel;
import model.RoomModel;
import model.UserModel;
import parameter.Messages;

/**
 * Servlet implementation class ViewRoomChatlogServlet
 */
@WebServlet("/ViewRoomChatlogServlet")
public class ViewRoomChatlogServlet extends LoginCheckServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = (User) (request.getSession().getAttribute("user"));
		String userid = user.getUserId();
		
		ChatModel cm = new ChatModel();
		// 指定されたルームのチャットを取得する
		String roomId = request.getParameter("roomId");
		try {
			if (roomId == null || !cm.roomExists(roomId)) {
				roomId = new UserModel().getLastAccessRoomId(userid);
			}

			if (roomId == null || !cm.roomExists(roomId)) {
				roomId = "R0000";
			}
		} catch (SwackException e) {
			System.out.println(e);
			request.setAttribute("errormessage", Messages.ERR_SYSTEM);
			return;
		}

		// 現在のルームの情報を記録する
		try {
			new UserModel().updateLastAccessRoomId(userid, roomId);
			var currentRoomInfo = cm.getRoomInfo(roomId);
			if (currentRoomInfo != null && currentRoomInfo.isDirected()) {
				request.setAttribute("currentRoomInfo", cm.getDirectMailRoomInfo(roomId, userid));
			} else {
				request.setAttribute("currentRoomInfo", currentRoomInfo);
			}
		} catch (SwackException e) {
			System.out.println("err");
			System.out.println(e);
			request.setAttribute("errormessage", Messages.ERR_SYSTEM);
		}

		try {
			if (roomId != null) {
				var chatlogs = new ChatModel().getChatLogList(userid, roomId);
				request.setAttribute("chatloglist", chatlogs);
			}
		} catch (SwackException e) {
			System.out.println("err");
			System.out.println(e);
			request.setAttribute("errormessage", Messages.ERR_SYSTEM);
		}

		try {
			if (roomId != null) {
				boolean joined = new RoomModel().isJoined(roomId, userid);
				request.setAttribute("isJoined", joined);
			}
		} catch (SwackException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		request.getRequestDispatcher("/WEB-INF/jsp/viewchatlogs.jsp").forward(request, response);
	}
	
}
