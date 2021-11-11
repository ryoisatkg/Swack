package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Room;
import bean.User;
import exception.SwackException;
import model.ChatModel;
import model.RoomModel;

/**
 * メンバー招待画面を制御するServlet 本Servletの実行にはログインを必要とする
 */
@WebServlet("/JoinMemberServlet")
public class JoinMemberServlet extends LoginCheckServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String roomid = request.getParameter("roomId");
		Room r;
		try {
			r = new ChatModel().getRoomInfo(roomid);
		} catch (SwackException e) {
			System.out.println(e);
			response.sendRedirect("/MainServlet");
			return;
		}

		ArrayList<User> users;
		try {
			users = new RoomModel().getNotJoinedUsers(roomid);
		} catch (SwackException e) {
			System.out.println(e);
			response.sendRedirect("/MainServlet");
			return;
		}

		request.setAttribute("roomInfo", r);
		request.setAttribute("users", users);
		request.getRequestDispatcher("/WEB-INF/jsp/joinmember.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] uids = request.getParameterValues("needsJoinUsers");
		String rid = request.getParameter("roomId");

		if (uids == null || rid == null) {
			// TODO: エラーを出す
			response.sendRedirect("MainServlet");
			return;
		}

		try {
			if (!new ChatModel().roomExists(rid)) {
				// TODO: エラーを出す
				response.sendRedirect("MainServlet");
				return;
			}
			
			new RoomModel().joinMultipleUsersToRoom(rid, uids);
		} catch (SwackException e) {
			System.out.println(e);
			response.sendRedirect("MainServlet");
			return;
		}

		response.sendRedirect("MainServlet");
		return;
	}
}