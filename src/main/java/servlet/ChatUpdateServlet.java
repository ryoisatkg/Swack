package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.ChatLog;
import bean.User;
import exception.SwackException;
import model.ChatModel;

/**
 * Servlet implementation class ChatUpdateServlet
 */
@WebServlet("/ChatUpdateServlet")
public class ChatUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");
		String userid = u.getUserId();
		int cid;
		try {
			cid = Integer.parseInt(request.getParameter("chatLogId"));
		} catch (NumberFormatException e) {
			response.sendRedirect("ViewRoomChatlogServlet");
			return;
		}

		ChatLog chatlog = null;
		try {
			chatlog = new ChatModel().getChatlog(cid, userid);
		} catch (SwackException e) {
			System.out.println(e);
			e.printStackTrace();
			response.sendRedirect("ViewRoomChatlogServlet");
			return;
		}

		if (chatlog == null) {
			// チャットログが存在しなかったら戻す
			response.sendRedirect("MainServlet");
			return;
		}

		
		if (userid.equals("U0000") || isPostedUserId(u.getUserId(), cid)) {
			request.setAttribute("chatlog", chatlog);
			request.getRequestDispatcher("/WEB-INF/jsp/editmessage.jsp").forward(request, response);
			return;
		} else {
			response.sendRedirect("ViewRoomChatlogServlet");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");
		String userid = u.getUserId();
		String chatlogId = request.getParameter("chatLogId");
		String message = request.getParameter("message");
		System.out.println(chatlogId);
		if (userid.equals("U0000") || isPostedUserId(u.getUserId(), Integer.parseInt(chatlogId))) {
			try {
				new ChatModel().updateChatlog(Integer.parseInt(chatlogId), message);
			} catch (SwackException e) {
				e.printStackTrace();
			}
		}
		response.sendRedirect("ViewRoomChatlogServlet");
	}

	private boolean isPostedUserId(String userid, int chatlogid) {
		String chatuid = null;
		try {
			chatuid = new ChatModel().getChatlog(chatlogid, userid).getUserId();

			return chatuid != null && userid.equals(chatuid);
		} catch (NumberFormatException e) {

			e.printStackTrace();
		} catch (SwackException e) {

			e.printStackTrace();
		}
		return false;
	}
}
