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

@WebServlet("/MainServlet")
public class MainServlet extends LoginCheckServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getSession().getAttribute("user") == null) {
			response.sendRedirect("LoginServlet");
			return;
		}
		String roomId = request.getParameter("roomId");
		roomId = roomId == null ? "" : roomId;

		User user = (User) (request.getSession().getAttribute("user"));
		String message = request.getParameter("message");

		ChatModel cm = new ChatModel();

		try {
			if (!cm.roomExists(roomId)) {
				response.sendRedirect("MainServlet");
				return;
			}

			if (message == null || message.length() == 0) {
				request.setAttribute("errormessage", "メッセージが空白です");
				response.sendRedirect("MainServlet");
				return;
			}

			if (!new RoomModel().isJoined(roomId, user.getUserId())) {
				response.sendRedirect("MainServlet");
				return;
			}

			cm.saveChatlog(roomId, user.getUserId(), message);

			response.sendRedirect("MainServlet");
			return;
		} catch (SwackException e) {
			System.out.println("err");
			System.out.println(e);
			request.setAttribute("errormessage", Messages.ERR_SYSTEM);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getSession().getAttribute("user") == null) {
			response.sendRedirect("LoginServlet");
			return;
		}
		User user = (User) (request.getSession().getAttribute("user"));

		ChatModel cm = new ChatModel();

		// 自分が見られるルームを取得
		try {
			var rooms = cm.getJoinedRoomList(user.getUserId());
			request.setAttribute("viewableRooms", rooms);
		} catch (SwackException e) {
			System.out.println("err");
			System.out.println(e + "rooms");
			request.setAttribute("errormessage", Messages.ERR_SYSTEM);
		}

		// ダイレクトメッセージのルームを取得
		try {
			var DMs = cm.getDMList(user.getUserId());
			request.setAttribute("directMessages", DMs);
		} catch (SwackException e) {
			System.out.println("err");
			System.out.println(e + "dms");
			request.setAttribute("errormessage", Messages.ERR_SYSTEM);
		}
		
		if(request.getParameter("favorite") == null) {
			this.setChatList(user.getUserId(), request);
		}

		
		request.setAttribute("favorite", request.getParameter("favorite") != null);
		request.setAttribute("loadingCheck", request.getParameter("loadingCheck"));
		request.setAttribute("isRoot", user.getUserId().equals("U0000"));
		request.getRequestDispatcher("/WEB-INF/jsp/main.jsp").forward(request, response);
	}

	/**
	 * requestから現在ルームを取得し、現在ルームの情報、チャット一覧などをrequestの属性にセットする
	 */
	private void setChatList(String userid, HttpServletRequest request) {
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
				boolean joined = new RoomModel().isJoined(roomId, userid);
				request.setAttribute("isJoined", joined);
			}
		} catch (SwackException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

}
